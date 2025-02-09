package com.softmania.feeease.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "academic_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AcademicSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String sessionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    @ManyToOne
    @JoinColumn(name = "school_id", referencedColumnName = "id", nullable = false)
    private School school;
}