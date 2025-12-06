package com.TMS.Transport.Management.System.dto;

import com.TMS.Transport.Management.System.entity.enums.BidStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidDto {

    private UUID bidId;

    private UUID loadId;

    private UUID transporterId;

    private Double proposedRate;

    private Integer trucksOffered;

    private BidStatus status;

    private LocalDateTime submittedAt;
}
