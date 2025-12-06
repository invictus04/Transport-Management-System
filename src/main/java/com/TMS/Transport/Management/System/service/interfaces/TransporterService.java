package com.TMS.Transport.Management.System.service.interfaces;

import com.TMS.Transport.Management.System.dto.AvailableTrucksDto;
import com.TMS.Transport.Management.System.dto.TransporterDto;

import java.util.List;
import java.util.UUID;

public interface TransporterService {

    TransporterDto createTransporter(TransporterDto transporterDto);

    TransporterDto getTransporterById(UUID transporterId);

    TransporterDto updateTransporterTrucks(UUID transporterId, List<AvailableTrucksDto> trucksDtos);
}
