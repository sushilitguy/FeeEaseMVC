package com.softmania.feeease.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String userName;
	private String password;
	@ManyToOne
	@JoinColumn(name = "school_id", referencedColumnName = "id", nullable = false)
	private School school;
	private String role;
	private boolean isEnabled;
}