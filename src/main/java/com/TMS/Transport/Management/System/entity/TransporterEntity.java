package com.TMS.Transport.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transporter")
public class TransporterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transporter_id")
    private UUID transporterId;

    @Column(name = "company_name")
    private String companyName;

    private double rating;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "available_trucks", joinColumns = @JoinColumn(name = "transporter_id"))
    private List<AvailableTrucks> availableTrucks = new ArrayList<>();


}
