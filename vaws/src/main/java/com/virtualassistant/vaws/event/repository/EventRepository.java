package com.virtualassistant.vaws.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virtualassistant.vaws.event.model.Event;
import com.virtualassistant.vaws.event.model.EventStatusEnum;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	List<Event> findAllByUserId(Integer userId);
	@Modifying
	@Query(value ="update Event u set u.status = :#{#status?.name()} where u.id = :id",nativeQuery=true)
	void updateStatusById( @Param("status") EventStatusEnum status,@Param("id") Integer id);
	
}

