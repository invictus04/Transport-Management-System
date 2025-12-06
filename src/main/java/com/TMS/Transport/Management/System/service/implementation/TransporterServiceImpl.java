package com.TMS.Transport.Management.System.service.implementation;

import com.TMS.Transport.Management.System.dto.AvailableTrucksDto;
import com.TMS.Transport.Management.System.dto.TransporterDto;
import com.TMS.Transport.Management.System.entity.AvailableTrucks;
import com.TMS.Transport.Management.System.entity.TransporterEntity;
import com.TMS.Transport.Management.System.exception.ResourceNotFoundException;
import com.TMS.Transport.Management.System.repository.TransporterRepository;
import com.TMS.Transport.Management.System.service.interfaces.TransporterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransporterServiceImpl implements TransporterService {

    private final TransporterRepository transporterRepository;
    private final ModelMapper modelMapper;

    @Override
    public TransporterDto createTransporter(TransporterDto transporterDto) {
        TransporterEntity transporterEntity = modelMapper.map(transporterDto, TransporterEntity.class);

        if(transporterEntity.getRating() > 5.0){
            transporterEntity.setRating(5.0);
        }

        TransporterEntity savedTransporter = transporterRepository.save(transporterEntity);
        return modelMapper.map(savedTransporter,TransporterDto.class);
    }

    @Override
    public TransporterDto getTransporterById(UUID transporterId) {
        TransporterEntity transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Transported does not exists with id: " + transporterId));
        return modelMapper.map(transporter, TransporterDto.class);
    }

    @Override
    @Transactional
    public TransporterDto updateTransporterTrucks(UUID transporterId, List<AvailableTrucksDto> trucksDtos) {
        TransporterEntity transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Transported does not exists with id: " + transporterId));

        List<AvailableTrucks> newTruckList = trucksDtos.stream()
                .map(dto -> modelMapper.map(dto, AvailableTrucks.class))
                .collect(Collectors.toList());

        transporter.getAvailableTrucks().clear();
        transporter.getAvailableTrucks().addAll(newTruckList);

        TransporterEntity updatedTransporter = transporterRepository.save(transporter);
        return modelMapper.map(updatedTransporter, TransporterDto.class);
    }
}
