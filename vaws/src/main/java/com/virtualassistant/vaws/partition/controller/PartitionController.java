package com.virtualassistant.vaws.partition.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.partition.repository.PartitionRepository;
import com.virtualassistant.vaws.partition.service.PartitionService;
import com.virtualassistant.vaws.plan.model.Plan;

@RestController
@RequestMapping("/api/partition")
public class PartitionController {
	
	@Autowired
	PartitionService partitionService;
	@Autowired
	PartitionRepository partitionRepository;
	
	@GetMapping("/findAvaiblePartition")
	public ResponseEntity<?> findAvaiblePartition(Integer planId) {
		try {
			List<Partition> list = partitionService.findAvaiblePartition(planId);
			//list=partitionRepository.findAll();
			return ResponseEntity.ok().body(list.size());
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
}
