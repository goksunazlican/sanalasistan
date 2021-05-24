package com.virtualassistant.vaws.event.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.model.EventStatusEnum;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.virtualassistant.vaws.event.repository.EventRepository;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.partition.service.PartitionService;
import com.virtualassistant.vaws.plan.model.Plan;
import com.virtualassistant.vaws.plan.repository.PlanRepository;
import com.virtualassistant.vaws.plan.service.PlanService;

@Service
public class EventServiceImpl implements EventService {
	@Autowired
	EventRepository eventRepository;

	@Autowired
	PlanRepository planRepository;

	@Autowired
	PlanService planService;

	@Autowired
	PartitionService partitionService;

	@Override
	public List<Event> findEventAll(Integer userId) {
		return eventRepository.findAllByUserId(userId);
	}

	@Override
	public Optional<Event> findEventById(Integer Id) {
		return eventRepository.findById(Id);
	}

	@Override
	public void saveEvent(Event payload) {
		// check for plan -> true : add event to plan(update plan and save event) !->
		// false: ask the daily or weekly? check free space for this plan or check for
		// week
		createPlanIfNotExist(payload);
		Plan plan = planRepository.findAllByLocalDate(payload.getFinish_at().toLocalDate());
		if (planService.checkSuitableTime(payload)) { 
			payload.setPlan(plan);
			eventRepository.save(payload);
			List<Event> eventList = new ArrayList<>();
				if (plan.getEventList() != null) {
					eventList.add(payload);
				}
			plan.setEventList(eventList);
			partitionService.updatePartitonList(payload);
			planService.updatePlan(plan);

		} else {
			System.out.println("This date is not avaible!");
		}
	}

	@Override
	public void createPlanIfNotExist(Event payload) {
		Plan plan = planRepository.findAllByLocalDate(payload.getFinish_at().toLocalDate());
		if (plan == null) {
			planService.createNewPlan(payload);
		}
	}

	@Override
	public void deleteEventById(Event event) {
		eventRepository.delete(event);
		;

	}

	@Override
	@Transactional
	public void updateStatus(Integer Id, EventStatusEnum status) {
		eventRepository.updateStatusById(status, Id);
	}

	@Override
	public void terminateExistEvent(Event event) {
		Plan plan = planRepository.findAllByLocalDate(event.getFinish_at().toLocalDate());
		removeEventFromPlan(event);
		planService.updateEventListInPlan(plan, event);

	}

	@Override
	public void removeEventFromPlan(Event event) {
		List<Event> removeWillEventList = findEventFromPlan(event);
		Plan plan = planRepository.findAllByLocalDate(event.getFinish_at().toLocalDate());
		List<Event> eventList = plan.getEventList();
		for (Event item : removeWillEventList) {
			if (eventList.contains(item)) {
				eventList.remove(item);
				deleteEventById(item);
			}
		}

	}

	@Override
	public List<Event> findEventFromPlan(Event newEvent) {
		Plan plan = planRepository.findAllByLocalDate(newEvent.getFinish_at().toLocalDate());
		List<Event> eventList = plan.getEventList();
		List<Event> removeWillEventList = new ArrayList<>();
		for (Event item : eventList) {
			if (item.getStart_at().isAfter(newEvent.getStart_at())
					&& item.getFinish_at().isBefore(newEvent.getFinish_at())) {
				removeWillEventList.add(item);
			} else if (item.getStart_at().isEqual(newEvent.getStart_at())
					&& item.getFinish_at().isEqual(newEvent.getFinish_at())) {
				removeWillEventList.add(item);
			}
		}
		return removeWillEventList;

	}

	@Override
	public List<Event> sortEventListByProfit(List<Event> eventList) {
		Comparator<Event> compareByProfit = (Event o1, Event o2) -> o1.getProfit().compareTo(o2.getProfit());
		Collections.sort(eventList, compareByProfit);
		return eventList;
	}

	@Override
	public void saveInholdEvent(Event payload) {
		createPlanIfNotExist(payload);
		Plan plan = planRepository.findAllByLocalDate(payload.getFinish_at().toLocalDate());
		payload.setPlan(plan);
		eventRepository.save(payload);
		
		List<Event> eventList = new ArrayList<>();
		if (plan.getEventList() != null) {
			eventList.add(payload);
		}
		plan.setEventList(eventList);
		planService.updatePlan(plan);

	}

}
