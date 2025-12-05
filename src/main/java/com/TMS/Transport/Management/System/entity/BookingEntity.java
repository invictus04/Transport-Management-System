package com.TMS.Transport.Management.System.entity;

import com.TMS.Transport.Management.System.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "booking", indexes = {
        @Index(name = "idx_booking_load", columnList = "load_id"),
        @Index(name = "idx_booking_transporter", columnList = "transporter_id"),
        @Index(name = "idx_booking_status", columnList = "status")
})
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id")
    private UUID bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id", nullable = false)
    private LoadEntity load;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = false, unique = true)
    private BidEntity bid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id", nullable = false)
    private TransporterEntity transporter;

    @Column(name = "allocated_trucks")
    private int allocatedTrucks;

    @Column(name = "final_rate")
    private double finalRate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;



}
