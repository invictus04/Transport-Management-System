package com.TMS.Transport.Management.System.service.interfaces;

import com.TMS.Transport.Management.System.dto.BidDto;
import com.TMS.Transport.Management.System.dto.responses.BidResponseDto;
import com.TMS.Transport.Management.System.entity.enums.BidStatus;

import java.util.List;
import java.util.UUID;

public interface BidService {

    BidDto createBid(BidDto bidDto);

    List<BidDto> getBids(UUID loadId, UUID transporterId, BidStatus status);

    BidDto getBidById(UUID bidId);

    BidDto rejectBid(UUID bidId);
}
