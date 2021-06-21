package com.virtualassistant.vaws.partition.model;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Util implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer value;
	private String time;

}
