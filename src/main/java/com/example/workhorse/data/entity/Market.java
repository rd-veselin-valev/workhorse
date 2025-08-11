package com.example.workhorse.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Market extends BaseEntity {
    @Column(name = "market_id")
    private UUID marketId;

    private String address;

    private String city;

    private String country;

    private String brand;
}
