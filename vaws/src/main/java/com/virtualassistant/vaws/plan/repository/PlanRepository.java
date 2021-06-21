package com.virtualassistant.vaws.plan.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.model.EventStatusEnum;
import com.virtualassistant.vaws.plan.model.Plan;

@Repository
public interface PlanRepository  extends JpaRepository<Plan, Integer> {
	Plan findByUserId(long id);
	//void fillPlan(Event event, Plan plan);
	@Query(value ="select * from Plan where date_time = :dateTime",nativeQuery=true)
	Plan findAllByLocalDate(@Param("dateTime") LocalDate dateTime);
	
	@Query(value ="select * from Plan where date_time = :dateTime",nativeQuery=true)
	Plan findAllByLocalDate(@Param("dateTime") String dateTime);
}
