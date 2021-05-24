package com.virtualassistant.vaws.plan.controller;

import java.util.List;
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
	public ResponseEntity<?> createGreedyPlan(@RequestBody Plan plan) {
		try {
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
