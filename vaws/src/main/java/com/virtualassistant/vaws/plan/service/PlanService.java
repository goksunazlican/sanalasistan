package com.virtualassistant.vaws.plan.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.plan.model.Plan;

public interface PlanService {
	Optional<Plan> findPlan(int id);
	Plan findByUserId(long userId);
	void createPlan(Plan plan);
	void deletePlan(Plan plan);
	void fillPlan(Event event, Plan plan);
	void updatePlan(Plan plan);
	boolean checkSuitableTime(Event event);
	Integer checkHour(LocalDateTime time);
	void createNewPlan(Event event);
	void updateEventListInPlan(Plan plan, Event event);
	void setEventListProfit(List<Event> eventList);
	void greedyPlan(List<Event> eventList, Plan plan);
	Plan findByDateTime(String dateTime);
	
	}
