package com.TMS.Transport.Management.System.controller;

import com.TMS.Transport.Management.System.dto.BidDto;
import com.TMS.Transport.Management.System.dto.responses.BidResponseDto;
import com.TMS.Transport.Management.System.entity.enums.BidStatus;
import com.TMS.Transport.Management.System.service.interfaces.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/bid")
    public ResponseEntity<BidDto> submitBid(@RequestBody BidDto bidDto){
        BidDto createdBid = bidService.createBid(bidDto);
        return new ResponseEntity<>(createdBid, HttpStatus.CREATED);
    }

    @GetMapping("/bid")
    public ResponseEntity<List<BidDto>> getBids(@RequestParam(required = false) UUID loadId, @RequestParam(required = false) UUID transporterId, @RequestParam(required = false)BidStatus status){
        List<BidDto> bids = bidService.getBids(loadId, transporterId, status);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/bid/{bidId}")
    public ResponseEntity<BidDto> getDetails(@PathVariable UUID bidId){
        return ResponseEntity.ok(bidService.getBidById(bidId));
    }

    @PatchMapping("/bid/{bidId}/reject")
    public ResponseEntity<BidDto> rejectBid(@PathVariable UUID bidId) {
        return ResponseEntity.ok(bidService.rejectBid(bidId));
    }


}
