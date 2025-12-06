package com.TMS.Transport.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransporterDto {

    private UUID transporterId;

    private String companyName;

    private  double rating;

    private List<AvailableTrucksDto> availableTrucks = new ArrayList<>();
}
