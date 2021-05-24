package com.virtualassistant.vaws.partition.service;

import java.time.LocalDate;
import java.util.List;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.plan.model.Plan;

public interface PartitionService {
	List<Partition> createPartitionList(Plan plan);
	void updatePartitonList(Event payload);
	void updatePlanPartition(Plan plan, Event event);
	List<Partition> findAvaiblePartition(Integer planId);
}
