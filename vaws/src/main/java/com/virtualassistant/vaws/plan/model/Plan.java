package com.virtualassistant.vaws.plan.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.action.internal.EntityActionVetoException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.partition.model.Partition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plan implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int plan_id;
	private long userId;
	private String description;
	private LocalDate dateTime;	
	
	@JsonManagedReference
	@OneToMany(mappedBy = "plan",targetEntity = Event.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Event> eventList;


}
