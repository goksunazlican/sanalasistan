package com.virtualassistant.vaws.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.virtualassistant.vaws.user.model.LoginRequest;
import com.virtualassistant.vaws.user.model.User;
import com.virtualassistant.vaws.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/readAllUser")
	public ResponseEntity<?> readUserAll(User user) {
		try {
			
			List<User> userList = userService.findUserAll();
			return ResponseEntity.ok().body(userList);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@PostMapping("/readUser")
	public ResponseEntity<?> readUser (User user) {
		try {			
			Integer id = (int) user.getId();
			Optional<User> currentUser = userService.findByUserId(id);
			return ResponseEntity.ok().body(currentUser);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	
	@CrossOrigin
	@PostMapping("/saveUser")
	public ResponseEntity<?> saveUser(@RequestBody User payload) {
		try {
			System.out.println("Gelen kullanıcı "+payload.toString());
			userService.saveUser(payload);
			return ResponseEntity.ok().body("Kullanıcı kaydedildi.");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
	@PostMapping("/loginUser")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest payload) {
		try {
			System.out.println("Giriş yapan kullanıcı "+payload.getUsername());
			User user= userService.userLogin(payload.getUsername(), payload.getPassword());
			return ResponseEntity.ok().body(user);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("İşleminiz şu an gerçekleştirilemiyor");
		}
	}
}