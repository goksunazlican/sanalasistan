package com.virtualassistant.vaws.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.virtualassistant.vaws.user.model.User;


public interface UserService {
	List<User> findUserAll();
	Optional<User> findByUserId(int id);
	void deleteUserByUserId(int userId);
	void saveUser(User payload) throws Exception;
}
