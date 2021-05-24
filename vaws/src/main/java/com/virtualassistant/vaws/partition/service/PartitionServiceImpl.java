package com.virtualassistant.vaws.partition.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.partition.repository.PartitionRepository;
import com.virtualassistant.vaws.plan.model.Plan;
import com.virtualassistant.vaws.plan.service.PlanService;
@Service
public class PartitionServiceImpl implements PartitionService{

	@Autowired
	PlanService planService;
	@Autowired
	PartitionRepository partitionRepository;
	
	@Override
	public List<Partition> createPartitionList(Plan plan) {
		List<Partition> partitionList = new ArrayList<>();
				
		for(int i = 1; i<= 48 ; i++ ) {
			Partition partition = new Partition();
			partition.setTime(i);
			partition.setExist(false);
			partition.setPlanDate(plan.getDateTime());
			partition.setPlan_id(plan.getPlan_id());
			partitionRepository.save(partition);
			partitionList.add(partition);
		}
		return partitionList;
	
	}
	@Override
	@Transactional
	public void updatePartitonList(Event payload) {
		List<Partition> list = partitionRepository.findAllByPlanId(payload.getPlan().getPlan_id());
		
		int started_at = planService.checkHour(payload.getStart_at());
		int finish_at = planService.checkHour(payload.getFinish_at());
		
		for (Partition item : list) {
			if (item.getTime() >= started_at && item.getTime() < finish_at) {
				item.setExist(true);
				item.setEvent_id(payload.getId());
				partitionRepository.save(item);
			}
			
		}
		
	}
	
	@Override
	public void updatePlanPartition(Plan plan, Event event) {
		int started_at = planService.checkHour(event.getStart_at());
		int finish_at = planService.checkHour(event.getFinish_at());
		
		List<Partition> partitionList = partitionRepository.findAllByPlanId(plan.getPlan_id());
		for(Partition item: partitionList) {
			if(item.getTime() >= started_at && item.getTime() < finish_at) {
				item.setExist(false);
			}
		}
	}
	@Override
	public List<Partition> findAvaiblePartition(Integer planId) {
		
		List<Partition> partitionList = partitionRepository.findAllByExistAndPlanId(planId);
		
		return partitionList;
	}

}
