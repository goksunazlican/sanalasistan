package com.virtualassistant.vaws.plan.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.service.EventService;
import com.virtualassistant.vaws.plan.model.DailyPlan;
import com.virtualassistant.vaws.plan.model.Plan;
import com.virtualassistant.vaws.plan.service.PlanService;


@RestController
@RequestMapping("api/plan")
public class PlanController {
	@Autowired
	PlanService planService;
	
	@Autowired
	EventService eventService;
 	
	@PostMapping("/getPlan")
	public ResponseEntity<?> readPlan(@RequestParam(name="planId") Integer planId) {
		try {
			Optional<Plan> plan = planService.findPlan(planId);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/getPlanByUserId")
	public ResponseEntity<?> readPlanByUserId(Integer userId) {
		try {
			Plan plan = planService.findByUserId(userId);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/getPlanByDateTime")
	public ResponseEntity<?> readPlanByDateTime(@RequestBody Map<String, Object> request) {
		try {
			System.out.println("Local date: "+request.get("dateTime"));
			Plan plan = planService.findByDateTime((String) request.get("dateTime"));
			List<Event> eventList = plan.getEventList();
			List<DailyPlan> dailyPlanList = new ArrayList<>();
			for(Event event: eventList) {
				DailyPlan dailyPlan = new DailyPlan();
				dailyPlan.setTitle(event.getContentTitle());
				dailyPlan.setStartDate(event.getStart_at());
				dailyPlan.setEndDate(event.getFinish_at());
				dailyPlanList.add(dailyPlan);
			}
			System.out.println("Plan list : "+dailyPlanList);
			return ResponseEntity.ok().body(dailyPlanList);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/createPlan")
	public ResponseEntity<?> createPlan(Plan plan) {
		try {
			planService.createPlan(plan);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/deletePlan")
	public ResponseEntity<?> deletePlan(Plan plan) {
		try {
			planService.deletePlan(plan);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/updatePlan")
	public ResponseEntity<?> updatePlan(Plan plan) {
		try {
			planService.updatePlan(plan);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/fillPlan")
	public ResponseEntity<?> fillPlan(Event event,Plan plan) {
		try {
			planService.fillPlan(event,plan);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/createGreedyPlan")
	public ResponseEntity<?> createGreedyPlan(@RequestBody Map<String, Object> request) {
		try {
			Plan plan = planService.findByDateTime((String) request.get("dateTime"));
			List<Event> eventList = plan.getEventList();
			planService.setEventListProfit(eventList);
			planService.greedyPlan(eventService.sortEventListByProfit(eventList),plan);
			return ResponseEntity.ok().body(plan);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	

}
