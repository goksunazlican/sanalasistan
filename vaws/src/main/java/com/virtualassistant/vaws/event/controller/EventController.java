package com.virtualassistant.vaws.event.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtualassistant.vaws.event.dto.EventDto;
import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.model.EventStatusEnum;
import com.virtualassistant.vaws.event.service.EventService;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.partition.repository.PartitionRepository;
import com.virtualassistant.vaws.partition.service.PartitionService;
import com.virtualassistant.vaws.plan.model.Plan;
import com.virtualassistant.vaws.plan.service.PlanService;

@RestController
@RequestMapping("/api/user")
public class EventController {
	@Autowired
	EventService eventService;
	
	@Autowired
	PlanService planService;
	
	@Autowired
	PartitionRepository partitionRepository;
	
	@Autowired
	PartitionService partitionService;

	@PostMapping("/readAllEvent")
	public ResponseEntity<?> readAllEvent(Integer userId) {
		try {
			System.out.println("userid: "+userId);
			List<Event> eventList = eventService.findEventAll(userId);
			return ResponseEntity.ok().body(eventList);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/findEvent")
	public ResponseEntity<?> findEvent(@RequestParam(name="id") Integer eventId) {
		try {
			
			Optional<Event> currentEvent = eventService.findEventById(eventId);
			return ResponseEntity.ok().body(currentEvent);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/saveEvent")
	public ResponseEntity<?> saveUser(@RequestBody Event payload) {
		try {
			//check suitable time for event 
			if(payload.getStatus() != EventStatusEnum.INHOLD.toString()) {
			eventService.saveEvent(payload);	
			}else {
			eventService.saveInholdEvent(payload);
			}
			return ResponseEntity.ok().body("Event kaydedildi.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/deleteEvent")
	public ResponseEntity<?> deleteEvent(Event event) {
		try {
			
			eventService.deleteEventById(event);
			return ResponseEntity.ok().body("Etkinlik kaydı silindi.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/updateStatus")
	public ResponseEntity<?> updateStatus(Integer eventId, String status) {
		try {
			EventStatusEnum statusType = null;
			switch ((String) status) {
			case "COMPLETED":
				statusType = EventStatusEnum.COMPLETED;
				break;
			case "INPROGRESS":
				statusType = EventStatusEnum.INPROGRESS;
				break;
			case "CANCELLED":
				statusType = EventStatusEnum.CANCELLED;
				break;
			case "INHOLD":
				statusType = EventStatusEnum.INHOLD;
				break;  
			}			
			eventService.updateStatus(eventId, statusType);
			return ResponseEntity.ok().body("Etkinlik statüsü güncellendi.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/swapAndTerminateOldEvent") //yeni eventi kaydedip eski eventi sonlandırdık.
	public ResponseEntity<?> swapEvent(@RequestBody Event payload) {
		try {
			eventService.terminateExistEvent(payload);	
			eventService.saveEvent(payload);
			return ResponseEntity.ok().body("Etkinlik statüsü güncellendi.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/swapAndMoveOldEvent") //yeni eventi kaydedip eski eventin taşınması.
	public ResponseEntity<?> swapAndMoveEvent(@RequestBody EventDto eventDto) {
		try {
			Event event = eventDto.getOldEvents().get(0);
			
			partitionService.updatePlanPartition(event.getPlan(), event);
			eventService.deleteEventById(event);
			eventService.saveEvent(eventDto.getOldEvents().get(0)); //save old event on new space.	
			eventService.saveEvent(eventDto.getNewEvent());
			return ResponseEntity.ok().body("Etkinlik statüsü güncellendi.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	
	@PostMapping("/showAvaiblePartition") //eski evente uygun yer gösterilmesi.
	public ResponseEntity<?> showAvaiblePartition(@RequestBody Event newEvent) {
		try {
			List<Event> eventList = eventService.findEventFromPlan(newEvent);
			for(Event event:eventList) {
				eventService.updateStatus(event.getId(), EventStatusEnum.INHOLD);
			}
			List<Partition> partitionList= partitionRepository.findAllByExistAndPlanId(eventList.get(0).getPlan().getPlan_id());
			EventDto eventDao = new EventDto();
			eventDao.setNewEvent(newEvent);
			eventDao.setOldEvents(eventList);
			eventDao.setPartitionList(partitionList);
			return ResponseEntity.ok().body(eventDao);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	

}
