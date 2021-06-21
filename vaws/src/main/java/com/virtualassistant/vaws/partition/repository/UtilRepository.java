package com.virtualassistant.vaws.partition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.virtualassistant.vaws.partition.model.Partition;
import com.virtualassistant.vaws.partition.model.Util;



public interface UtilRepository extends JpaRepository<Util, Integer> {

	@Query(value ="select time from Util where value = :value",nativeQuery=true)
	String findByValue(@Param("value") int value);
}
