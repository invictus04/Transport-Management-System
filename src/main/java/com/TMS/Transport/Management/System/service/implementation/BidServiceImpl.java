package com.TMS.Transport.Management.System.service.implementation;

import com.TMS.Transport.Management.System.dto.BidDto;
import com.TMS.Transport.Management.System.dto.responses.BidResponseDto;
import com.TMS.Transport.Management.System.entity.BidEntity;
import com.TMS.Transport.Management.System.entity.LoadEntity;
import com.TMS.Transport.Management.System.entity.TransporterEntity;
import com.TMS.Transport.Management.System.entity.enums.BidStatus;
import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import com.TMS.Transport.Management.System.exception.InsufficientCapacityException;
import com.TMS.Transport.Management.System.exception.InvalidStatusTransitionException;
import com.TMS.Transport.Management.System.exception.ResourceNotFoundException;
import com.TMS.Transport.Management.System.repository.BidRepository;
import com.TMS.Transport.Management.System.repository.LoadRepository;
import com.TMS.Transport.Management.System.repository.TransporterRepository;
import com.TMS.Transport.Management.System.service.interfaces.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;
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
    public BidDto createBid(BidDto bidDto) {
            LoadEntity load = loadRepository.findById(bidDto.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + bidDto.getLoadId()));

        TransporterEntity transporter = transporterRepository.findById(bidDto.getTransporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + bidDto.getTransporterId()));

        //Rule 2 verification
        if(load.getStatus() == LoadStatus.BOOKED || load.getStatus() == LoadStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Cannot bid on a load with status: " + load.getStatus());
        }

        //Rule 1 verification
        boolean hasCapacity = transporter.getAvailableTrucks().stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(load.getTruckType()))
                .anyMatch(t -> t.getCount() >= bidDto.getTrucksOffered());

        if(!hasCapacity) {
            throw new InsufficientCapacityException("Transporter does not have enough " + load.getTruckType() + " truck available");
        }

        if(load.getStatus() == LoadStatus.POSTED) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
            loadRepository.save(load);
        }

        BidEntity bid = new BidEntity();
        bid.setLoad(load);
        bid.setTransporter(transporter);
        bid.setProposedRate(bidDto.getProposedRate());
        bid.setTrucksOffered(bidDto.getTrucksOffered());
        bid.setStatus(BidStatus.PENDING);
        bid.setSubmittedAt(LocalDateTime.now());

        BidEntity saveBid = bidRepository.save(bid);

        return convertToDto(saveBid);
    }

    @Override
    public List<BidDto> getBids(UUID loadId, UUID transporterId, BidStatus status) {
        List<BidEntity> bids = bidRepository.findByLoad_LoadIdAndTransporter_TransporterIdAndStatus(loadId, transporterId, status);
       return bids.stream().map(this::convertToDto)
               .collect(Collectors.toList());
    }

    @Override
    public BidDto getBidById(UUID bidId) {
        BidEntity bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bidId));
        return convertToDto(bid);
    }

    @Override
    public BidDto rejectBid(UUID bidId) {
        BidEntity bid = bidRepository.findById(bidId)
                .orElseThrow( () -> new ResourceNotFoundException("Bid not found with: " + bidId));

        if(bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusTransitionException("Cannot reject a bid that is already " + bid.getStatus());
        }

        bid.setStatus(BidStatus.REJECTED);
        return convertToDto(bidRepository.save(bid));
    }
}
