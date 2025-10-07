package com.swp391.Warranty_EV.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_no", nullable = false, unique = true, length = 64)
    private String partNo;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "track_serial", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean trackSerial = false;

    @Column(name = "track_lot", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean trackLot = false;

    @Column(name = "uom", length = 20)
    private String uom = "EA";
}
