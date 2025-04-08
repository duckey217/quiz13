package com.example.quiz13.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.example.quiz13.constants.ResMessages;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz")
public class Quiz {
	
	
	@Id
	@Column(name = "id")
	private int id;
	
	@NotBlank(message = ResMessages.ConstantsMessage.PARAM_QUIZ_NAME_ERROR)
	@Column(name = "name")
	private String name;
	
	@NotBlank(message =ResMessages.ConstantsMessage.PARAM_DESCRIPTION_ERROR)
	@Length(max = 200,message=ResMessages.ConstantsMessage.PARAM_DESCRIPTION_LENGTH_TOO_LONG)
	@Column(name = "description")
	private String description;
	
	@NotNull(message =ResMessages.ConstantsMessage.PARAM_START_DATE_ERROR)
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@NotNull(message =ResMessages.ConstantsMessage.PARAM_END_DATE_ERROR)
	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "is_published")
	private boolean published;

	public Quiz() {
		super();
	}

	// id 是流水號不用
	public Quiz(String name, String description, LocalDate startDate, LocalDate endDate, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean isPublished() {
		return published;
	}

}
