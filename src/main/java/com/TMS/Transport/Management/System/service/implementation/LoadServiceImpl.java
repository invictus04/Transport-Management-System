package com.TMS.Transport.Management.System.service.implementation;

import com.TMS.Transport.Management.System.dto.BidDto;
import com.TMS.Transport.Management.System.dto.LoadDto;
import com.TMS.Transport.Management.System.dto.responses.LoadResponseDto;
import com.TMS.Transport.Management.System.entity.BidEntity;
import com.TMS.Transport.Management.System.entity.LoadEntity;
import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import com.TMS.Transport.Management.System.exception.InvalidStatusTransitionException;
import com.TMS.Transport.Management.System.exception.ResourceNotFoundException;
import com.TMS.Transport.Management.System.repository.BidRepository;
import com.TMS.Transport.Management.System.repository.LoadRepository;
import com.TMS.Transport.Management.System.service.interfaces.LoadService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoadServiceImpl implements LoadService {

    private final LoadRepository loadRepository;
    private final BidRepository bidRepository;
    private final ModelMapper modelMapper;

    private BidDto convertToDto(BidEntity bid) {
        BidDto dto = modelMapper.map(bid, BidDto.class);

        if (bid.getLoad() != null) {
            dto.setLoadId(bid.getLoad().getLoadId());
        }
        if (bid.getTransporter() != null) {
            dto.setTransporterId(bid.getTransporter().getTransporterId());
        }
        return dto;
    }


    @Override
    @Transactional
    public LoadResponseDto createLoad(LoadDto loadDto) {
        LoadEntity load = modelMapper.map(loadDto, LoadEntity.class);
        load.setStatus(LoadStatus.POSTED);
        load.setDatePosted(LocalDateTime.now());

        load.setLoadId(null);

        LoadEntity savedLoad = loadRepository.save(load);
        return modelMapper.map(savedLoad, LoadResponseDto.class);
    }

    @Override
    public Page<LoadDto> getAllLoads(String shipperId, LoadStatus status, Pageable pageable) {
        Specification<LoadEntity> spec = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(shipperId != null && !shipperId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("shipperId"), shipperId));
            }

            if(status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        } );

        Page<LoadEntity> loadPage = loadRepository.findAll(spec, pageable);

        return loadPage.map(entity -> modelMapper.map(entity, LoadDto.class));
    }

    @Override
    public LoadDto getLoadById(UUID loadId) {
        LoadEntity load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with ID: " + loadId));

        return modelMapper.map(load, LoadDto.class);
    }

    @Override
    @Transactional
    public String cancelLoad(UUID loadId) {
        LoadEntity load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load does not exist, Cannot perform cancellation"));

        if(load.getStatus() == LoadStatus.BOOKED) {
            throw new InvalidStatusTransitionException("Cannot cancel a load that is already BOOKED.");
        }

        load.setStatus(LoadStatus.CANCELLED);
        loadRepository.save(load);
        return "Load Cancelled Successfully";

    }

    @Override
    public List<BidDto> getBestBids(UUID loadId) {
        if (!loadRepository.existsById(loadId)) {
            throw new ResourceNotFoundException("Load not found with ID: " + loadId);
        }

        List<BidEntity> bids = bidRepository.findByLoad_LoadId(loadId);

        return  bids.stream()
                .sorted(Comparator.comparingDouble(this::calculateBidScore). reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private double calculateBidScore(BidEntity bid){
        double rate = bid.getProposedRate();
        double rating = bid.getTransporter().getRating();

        if(rate <= 0) return 0.0;

        return (1.0 / rate) * 0.7 + (rating / 5.0) * 0.3;
    }
}
