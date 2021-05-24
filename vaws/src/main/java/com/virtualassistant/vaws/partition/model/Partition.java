package com.virtualassistant.vaws.partition.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.plan.model.Plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Partition implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer partition_id;
	private Integer time;
	private Boolean exist;
	private LocalDate planDate;
	private Integer plan_id;
	private Integer event_id;
}
