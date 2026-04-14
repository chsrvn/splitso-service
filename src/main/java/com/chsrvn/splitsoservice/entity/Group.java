package com.chsrvn.splitsoservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(length = 50)
    private String type;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "creat_dttm", updatable = false)
    private OffsetDateTime creatDttm = OffsetDateTime.now();

    @UpdateTimestamp
    @Column(name = "chg_dttm")
    private OffsetDateTime chgDttm;
}
