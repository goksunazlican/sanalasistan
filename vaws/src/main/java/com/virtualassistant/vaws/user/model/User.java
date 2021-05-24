package com.virtualassistant.vaws.user.model;

import java.util.Date;

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

public class User{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private Date deletedAt;
}
