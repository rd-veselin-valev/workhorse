package com.example.workhorse.data.entity;

import com.example.workhorse.data.enums.DeviceManufacturer;
import com.example.workhorse.data.enums.DeviceStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Specification extends BaseEntity {
    private String serialNumber;

    private String ip;

    @Enumerated(EnumType.STRING)
    private DeviceManufacturer manufacturer;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status;
}
