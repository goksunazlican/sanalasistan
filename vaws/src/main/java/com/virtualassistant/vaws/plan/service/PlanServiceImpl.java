package com.virtualassistant.vaws.plan.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.plaf.metal.MetalBorders.PaletteBorder;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.repository.EventRepository;
import com.virtualassistant.vaws.event.service.EventService;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.partition.model.Util;
import com.virtualassistant.vaws.partition.repository.PartitionRepository;
import com.virtualassistant.vaws.partition.repository.UtilRepository;
import com.virtualassistant.vaws.partition.service.PartitionService;
import com.virtualassistant.vaws.plan.model.Plan;
import com.virtualassistant.vaws.plan.repository.PlanRepository;

@Service
public class PlanServiceImpl implements PlanService {
	@Autowired
	PlanRepository planRepository;
	
	@Autowired
	PartitionService partitionService;
	
	@Autowired
	PartitionRepository partitionRepository;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	UtilRepository utilRepository;
	
	@Override
	public Optional<Plan> findPlan(int id) {
		// TODO Auto-generated method stub
		return planRepository.findById(id);
	}

	@Override
	public Plan findByUserId(long userId) {
		// TODO Auto-generated method stub
		return planRepository.findByUserId(userId);
	}

	@Override
	public void createPlan(Plan plan) {
		/*checkplan = true ise  plana ekle, false uyarı verip -> öneride bulunma o gündeki boşluklar yine eklenmezse ->tüm haftayı göster */
		planRepository.save(plan);
	}

	@Override
	public void deletePlan(Plan plan) {
		planRepository.delete(plan);
	}

	@Override
	public void fillPlan(Event event, Plan plan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePlan(Plan plan) {
		planRepository.save(plan);
	}

	@Override
	public boolean checkSuitableTime(Event event) {
		Plan plan = planRepository.findAllByLocalDate(event.getFinish_at().toLocalDate());
			List<Partition> partitionList = partitionRepository.findAllByPlanId(plan.getPlan_id());
			int started_at = checkHour(event.getStart_at());
			int finish_at = checkHour(event.getFinish_at());
			
			if(partitionList != null) {
				for(int i=0; i<partitionList.size(); i++) {
					if(partitionList.get(i).getTime() >= started_at && partitionList.get(i).getTime() <= finish_at) {
						if(partitionList.get(i).getExist() == true) {
							return false;
						}
						return true;
					}
				}
			}else {
				return true;
			}
	
		return false;
	}

	@Override
	public Integer checkHour(LocalDateTime time) {
		int minute=time.getMinute();
		if(minute<30) {
			return time.getHour()*2;
		}
		return (time.getHour()*2)+1;
	}

	@Override
	public void createNewPlan(Event event) {
		Plan plan = new Plan();
		plan.setDateTime(event.getFinish_at().toLocalDate());
		plan.setUserId(event.getUserId());
		plan.setDescription(event.getFinish_at().getDayOfWeek().toString());
		planRepository.save(plan);
		partitionService.createPartitionList(plan);
		planRepository.save(plan);
		
	}

	@Override
	public void updateEventListInPlan(Plan plan, Event event) {
		
		partitionService.updatePlanPartition(plan,event);
		updatePlan(plan);
	}

	@Override
	public void setEventListProfit(List<Event> eventList) {
		for(Event item :eventList){
			if(item.getStart_at() != null) {
				Integer duration_in_unit= (item.getFinish_at().toLocalTime().toSecondOfDay() - item.getStart_at().toLocalTime().toSecondOfDay())/1800;  //event kaydolurken start_at var mı yok mu şart koy
				item.setDuration(duration_in_unit);
				Double profit = ((double)item.getPrivacyLevel()/(double)item.getDuration());
				item.setProfit(profit);
				eventRepository.save(item);
				
			}else {
				Double profit =((double)item.getPrivacyLevel()/(double)item.getDuration());
				item.setProfit(profit);
				eventRepository.save(item);
			}
		}
	}

/*	@Transactional
	@Override
	public void greedyPlan(List<Event> eventList, Plan plan) {
		partitionRepository.setFalseFullPartition(plan.getPlan_id());
		List<Partition> partitions = partitionRepository.findAllByPlanId(plan.getPlan_id());
		for(Event item: eventList) {
			Integer finish_at = checkHour(item.getFinish_at());
			Integer counter = 0;
			for(int i=0; i<finish_at; i++) {
				if(partitions.get(i).getExist() == false) {
					counter++;
				}
				
			}
			Integer duration = item.getDuration();
			if(counter >= item.getDuration()) {
				//eski yerimi partitionda false'a çeviricem
				//partitionRepository.setFalseOldEvent(item.getId());
				for(int i=0; i<finish_at;i++) {
					if(partitions.get(i).getExist() == false && duration > 0) {
						partitions.get(i).setEvent_id(item.getId());
						partitions.get(i).setExist(true);
						partitionRepository.save(partitions.get(i));
						duration--;
					}
				}
				
				//eventi güncelle
				List<Partition> partitionListOfEvent=partitionRepository.findByEventId(item.getId());
				System.out.println(item.getId()+ " : "+ partitionListOfEvent+ ":");
				Integer start = partitionListOfEvent.get(0).getTime();
				Integer finish = partitionListOfEvent.get(partitionListOfEvent.size()-1).getTime();
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
				
				String utilTimeStart = utilRepository.findByValue(start);
				String startLocalDateTime = plan.getDateTime().toString() + " " + utilTimeStart;
				LocalDateTime dateTimeStart = LocalDateTime.parse(startLocalDateTime, formatter);
				item.setStart_at(dateTimeStart);
				
				String utilTimeFinish = utilRepository.findByValue(finish+1);
				String finishLocalDateTime = plan.getDateTime().toString() + " " + utilTimeFinish;
				LocalDateTime dateTimeFinish = LocalDateTime.parse(finishLocalDateTime, formatter);
				item.setFinish_at(dateTimeFinish);
				
				eventRepository.save(item);
							
			}else {
				System.out.println("Bu event " +item.getId()+ " yerleştirilemedi!");
			}
		}
	}
	
//eski yerimizi temizlemiyoruz
	//kendi yerimizi de dolu sanıyoruz
	//eventi güncellemiyoruz
	
	@Override
	public Plan findByDateTime(String dateTime) {
		
		return planRepository.findAllByLocalDate(dateTime);
	}*/
	
	@Transactional
	@Override
	public void greedyPlan(List<Event> eventList, Plan plan) {
		List<Partition> partitions = partitionRepository.findAllByPlanId(plan.getPlan_id());
		for(Event item: eventList) {
			Integer finish_at = checkHour(item.getFinish_at());
			Integer counter = 0;
			for(int i=0; i<finish_at; i++) {
				if(partitions.get(i).getExist() == false) {
					counter++;
				}
				else if(partitions.get(i).getEvent_id() == item.getId()) { //dolu olan yer benim ise
					counter++;
				}
			}
			Integer duration = item.getDuration();
			if(counter >= item.getDuration()) {
				//eski yerimi partitionda false'a çeviricem
				partitionRepository.setFalseOldEvent(item.getId());
				for(int i=0; i<finish_at;i++) {
					if(partitions.get(i).getExist() == false && duration > 0) {
						partitions.get(i).setEvent_id(item.getId());
						partitions.get(i).setExist(true);
						partitionRepository.save(partitions.get(i));
						duration--;
					}
				}
				
				//eventi güncelle
				List<Partition> partitionListOfEvent=partitionRepository.findByEventId(item.getId());
				System.out.println(item.getId()+ " : "+ partitionListOfEvent+ ":");
				Integer start = partitionListOfEvent.get(0).getTime();
				Integer finish = partitionListOfEvent.get(partitionListOfEvent.size()-1).getTime();
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
				
				String utilTimeStart = utilRepository.findByValue(start);
				String startLocalDateTime = plan.getDateTime().toString() + " " + utilTimeStart;
				LocalDateTime dateTimeStart = LocalDateTime.parse(startLocalDateTime, formatter);
				item.setStart_at(dateTimeStart);
				
				String utilTimeFinish = utilRepository.findByValue(finish+1);
				String finishLocalDateTime = plan.getDateTime().toString() + " " + utilTimeFinish;
				LocalDateTime dateTimeFinish = LocalDateTime.parse(finishLocalDateTime, formatter);
				item.setFinish_at(dateTimeFinish);
				
				eventRepository.save(item);
							
			}else {
				System.out.println("Bu event " +item.getId()+ " yerleştirilemedi!");
			}
		}
	}
	
//eski yerimizi temizlemiyoruz
	//kendi yerimizi de dolu sanıyoruz
	//eventi güncellemiyoruz
	
	@Override
	public Plan findByDateTime(String dateTime) {
		
		return planRepository.findAllByLocalDate(dateTime);
	}
	

		
}

