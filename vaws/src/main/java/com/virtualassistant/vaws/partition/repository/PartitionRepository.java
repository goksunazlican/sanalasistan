package com.virtualassistant.vaws.partition.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.plan.model.Plan;

public interface PartitionRepository extends JpaRepository<Partition, Integer> {
	@Query(value ="select * from Partition where plan_id = :plan_id",nativeQuery=true)
	List<Partition> findAllByPlanId(@Param("plan_id") int plan_id);
	//List<Partition> findAllByPlanId(int plan_id);
	
	@Query(value ="select * from Partition where plan_id = :plan_id and exist = false",nativeQuery=true)
	List<Partition> findAllByExistAndPlanId(@Param("plan_id") int plan_id);

}
