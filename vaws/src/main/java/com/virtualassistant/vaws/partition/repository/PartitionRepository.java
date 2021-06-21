package com.virtualassistant.vaws.partition.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	@Query(value ="select * from Partition where event_id = :event_id and exist = true",nativeQuery=true)
	List<Partition> findByEventId(@Param("event_id") int event_id);

	@Modifying
	@Query(value ="update Partition u set u.exist = false, u.event_id = null where u.event_id = :event_id and u.exist = true",nativeQuery=true)
	void setFalseOldEvent(@Param("event_id") int event_id);

	@Modifying
	@Query(value ="update Partition u set u.exist = false, u.event_id = null where u.plan_id = :plan_id ",nativeQuery=true)
	void setFalseFullPartition(@Param("plan_id") int plan_id);
}
