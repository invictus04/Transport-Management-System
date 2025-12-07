package com.TMS.Transport.Management.System.controller;

import com.TMS.Transport.Management.System.dto.AvailableTrucksDto;
import com.TMS.Transport.Management.System.dto.TransporterDto;
import com.TMS.Transport.Management.System.dto.responses.TransporterResponseDto;
import com.TMS.Transport.Management.System.service.interfaces.TransporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransporterController {

    private final TransporterService transporterService;

    @PostMapping("/transporter")
    public ResponseEntity<TransporterResponseDto> createTransporter(@RequestBody TransporterDto transporterDto) {
        TransporterResponseDto createdTransporter = transporterService.createTransporter(transporterDto);
        return new ResponseEntity<>(createdTransporter, HttpStatus.CREATED);
    }

    @GetMapping("/transporter/{transporterId}")
    public ResponseEntity<TransporterDto> getDetails(@PathVariable UUID transporterId){
        return ResponseEntity.ok(transporterService.getTransporterById(transporterId));
    }

    @PutMapping("/transporter/{transporterId}/trucks")
    public ResponseEntity<TransporterDto> updateTrucks(@PathVariable UUID transporterId, @RequestBody List<AvailableTrucksDto> truckDtos){
        return ResponseEntity.ok(transporterService.updateTransporterTrucks(transporterId, truckDtos));
    }
}
