package com.softmania.feeease.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Students {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String fatherName;
	private String motherName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate dob;
	private String contactNo;
	private String standard;
	private String section;
	@ManyToOne
	@JoinColumn(name = "session_id", referencedColumnName = "id", nullable = false)
	private AcademicSession session;
	private double feesAmount;
	private boolean isEnabled;
	@ManyToOne
	@JoinColumn(name = "school_id", referencedColumnName = "id", nullable = false)
	private School school;
}