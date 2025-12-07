package com.TMS.Transport.Management.System.controller;

import com.TMS.Transport.Management.System.dto.BidDto;
import com.TMS.Transport.Management.System.dto.LoadDto;
import com.TMS.Transport.Management.System.dto.responses.LoadResponseDto;
import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import com.TMS.Transport.Management.System.repository.LoadRepository;
import com.TMS.Transport.Management.System.service.interfaces.LoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LoadController {

    private final LoadService loadService;

    @PostMapping("/load")
    public ResponseEntity<LoadResponseDto> createLoad(@RequestBody LoadDto loadDto){
        LoadResponseDto createdLoad = loadService.createLoad(loadDto);
        return new ResponseEntity<>(createdLoad, HttpStatus.CREATED);
    }

    @GetMapping("/load")
    public ResponseEntity<Page<LoadDto>> gelAllLoads(@RequestParam String shipperId, @RequestParam LoadStatus status, @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<LoadDto> loads = loadService.getAllLoads(shipperId,status, pageable);
        return ResponseEntity.ok(loads);
    }

    @GetMapping("/load/{loadId}")
    public  ResponseEntity<LoadDto> getLoad(@PathVariable UUID loadId) {
        return ResponseEntity.ok(loadService.getLoadById(loadId));
    }

    @PatchMapping("/load/{loadId}/cancel")
    public ResponseEntity<String> cancelLoad(@PathVariable UUID loadId) {
        return ResponseEntity.ok(loadService.cancelLoad(loadId));
    }



    @GetMapping("/load/{loadId}/best-bids")
    public ResponseEntity<List<BidDto>> getBestBids(@PathVariable UUID loadId){
        List<BidDto> bestBids = loadService.getBestBids(loadId);
        return ResponseEntity.ok(bestBids);
    }




}
