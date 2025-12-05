package com.TMS.Transport.Management.System.entity;

import com.TMS.Transport.Management.System.entity.enums.BidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bid")
public class BidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bid_id")
    private UUID bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id", nullable = false)
    private LoadEntity loadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id", nullable = false)
    private TransporterEntity transporterId;

    @Column(name = "proposed_rate")
    private double proposedRate;

    @Column(name = "truck_offered")
    private int trucksOffered;

    @Enumerated(EnumType.STRING)
    private BidStatus status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
}
