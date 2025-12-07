package com.TMS.Transport.Management.System.service.interfaces;

import com.TMS.Transport.Management.System.dto.BidDto;
import com.TMS.Transport.Management.System.dto.LoadDto;
import com.TMS.Transport.Management.System.dto.responses.LoadResponseDto;
import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LoadService {
    LoadResponseDto createLoad(LoadDto loadDto);

    Page<LoadDto> getAllLoads(String shipperId, LoadStatus status, Pageable pageable);

    LoadDto getLoadById(UUID loadId);

    String cancelLoad(UUID loadId);

    List<BidDto> getBestBids(UUID loadId);
}
