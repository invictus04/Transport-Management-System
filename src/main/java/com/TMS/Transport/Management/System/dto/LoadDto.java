package com.TMS.Transport.Management.System.dto;

import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import com.TMS.Transport.Management.System.entity.enums.WeightUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadDto {

    private UUID loadId;

    private  String shipperId;

    private String loadingCity;

    private String unloadingCity;

    private LocalDateTime loadingDate;

    private String productType;

    private double weight;

    private WeightUnit weightUnit;

    private String truckType;

    private int noOfTrucks;

    private LoadStatus status;

    private LocalDateTime datePosted;

}
