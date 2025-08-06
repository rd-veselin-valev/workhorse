package com.example.workhorse.data.entity;

import com.example.workhorse.data.enums.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Market market;

    @OneToOne(fetch = FetchType.LAZY)
    private Specification specification;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;
}
