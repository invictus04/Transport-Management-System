package com.TMS.Transport.Management.System.service.interfaces;

import com.TMS.Transport.Management.System.dto.AvailableTrucksDto;
import com.TMS.Transport.Management.System.dto.TransporterDto;
import com.TMS.Transport.Management.System.dto.responses.TransporterResponseDto;

import java.util.List;
import java.util.UUID;

public interface TransporterService {

    TransporterResponseDto createTransporter(TransporterDto transporterDto);

    TransporterDto getTransporterById(UUID transporterId);

    TransporterDto updateTransporterTrucks(UUID transporterId, List<AvailableTrucksDto> trucksDtos);
}
