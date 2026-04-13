package com.chsrvn.splitsoservice.entity;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String phone;

    @Column(columnDefinition = "text")
    private String password;

    @Column(name = "avatar_url", columnDefinition = "text")
    private String avatarUrl;

    @Column(columnDefinition = "varchar(10)")
    private String currency;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDttm;

    @UpdateTimestamp
    @Column(name = "chg_dttm")
    private LocalDateTime chgDttm;

}
