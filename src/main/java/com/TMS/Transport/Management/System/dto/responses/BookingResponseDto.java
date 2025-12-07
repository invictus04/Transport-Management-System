package com.TMS.Transport.Management.System.dto.responses;

import com.TMS.Transport.Management.System.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private UUID bookingId;

    private UUID loadId;

    private UUID bidId;

    private UUID transporterId;

    private int allocatedTrucks;

    private double finalRate;

    private BookingStatus status;

    private LocalDateTime bookedAt;

}
