package com.TMS.Transport.Management.System.dto;

import com.TMS.Transport.Management.System.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private UUID bookingId;

    private LoadDto load;

    private BidDto bid;

    private  TransporterDto transporter;

    private int allocatedTrucks;

    private double finalRate;

    private BookingStatus status;

    private LocalDateTime bookedAt;
}
