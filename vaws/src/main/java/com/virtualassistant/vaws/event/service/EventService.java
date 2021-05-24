package com.virtualassistant.vaws.event.service;

import java.util.List;
import java.util.Optional;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.model.EventStatusEnum;
import com.virtualassistant.vaws.partition.model.Partition;

public interface EventService {

	List<Event> findEventAll(Integer userId);	
	Optional<Event> findEventById(Integer Id);	
	void saveEvent(Event payload);	
	void deleteEventById(Event event);	
	void updateStatus(Integer Id, EventStatusEnum status);
	void createPlanIfNotExist(Event payload);
	void terminateExistEvent(Event payload);
	void removeEventFromPlan(Event event);
	List<Event> findEventFromPlan(Event newEvent);
	List<Event>  sortEventListByProfit(List<Event> eventList);
	void saveInholdEvent(Event payload);
	
}

