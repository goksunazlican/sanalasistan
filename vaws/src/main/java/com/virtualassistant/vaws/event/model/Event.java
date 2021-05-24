package com.virtualassistant.vaws.event.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.plan.model.Plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	private Integer userId;
	private String contentTitle;
	private LocalDateTime start_at;
	private LocalDateTime finish_at;
	private Integer duration;
	private Double profit;
	@Enumerated(EnumType.STRING)
	private EventStatusEnum status;
	private Integer privacyLevel;
	private String description;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="plan_id")
	private Plan plan;
	
	public String getStatus() {
	    return status.name();
	}
	
}
