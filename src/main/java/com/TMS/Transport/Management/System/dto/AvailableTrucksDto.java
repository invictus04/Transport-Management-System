package com.TMS.Transport.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTrucksDto {

    private String truckType;

    private int count;
}
