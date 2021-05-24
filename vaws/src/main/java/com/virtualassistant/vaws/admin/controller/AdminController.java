package com.virtualassistant.vaws.admin.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtualassistant.vaws.user.model.User;
import com.virtualassistant.vaws.user.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
//	
	@GetMapping("/readAllUser")
	public ResponseEntity<?> readUserAll() {
		try {			
			List<User> userList = userService.findUserAll();
			return ResponseEntity.ok().body(userList);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/findUserById")
	public ResponseEntity<?> findUser(int userId) {
		try {			
			Optional<User> user = userService.findByUserId(userId);
			return ResponseEntity.ok().body(user);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/deleteUser")
	public ResponseEntity<?> deleteUser(int userId) {
		try {			
			userService.deleteUserByUserId(userId);
			return ResponseEntity.ok().body("Kullanıcı silindi.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
		
	}
}
