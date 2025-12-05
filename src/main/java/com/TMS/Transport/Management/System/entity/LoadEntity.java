package com.TMS.Transport.Management.System.entity;

import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import com.TMS.Transport.Management.System.entity.enums.WeightUnit;
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
@Table(name = "load", indexes = {
        @Index(name = "idx_load_shipper_status", columnList = "shipper_id, status")
})
public class LoadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "load_id")
    private UUID loadId;

    @Column(name = "shipper_id")
    private String shipperId;

    @Column(name = "loading_city")
    private String loadingCity;

    @Column(name = "unloading_city")
    private String unloadingCity;

    @Column(name = "loading_date")
    private LocalDateTime loadingDate;

    @Column(name = "product_type")
    private String productType;

    private double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_unit")
    private WeightUnit weightUnit;

    @Column(name = "truck_type")
    private String truckType;

    @Column(name = "no_of_trucks")
    private int noOfTrucks;

    @Enumerated(EnumType.STRING)
    private LoadStatus status;

    @Column(name = "date_posted")
    private LocalDateTime datePosted;

    @Version
    private Long version;

}
