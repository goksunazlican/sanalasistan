package com.virtualassistant.vaws.event.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.plan.model.Plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto{

	private Event newEvent;
	private List<Event> oldEvents;
	private List<Partition> partitionList;
	
}
