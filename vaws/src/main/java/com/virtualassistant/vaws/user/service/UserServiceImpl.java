package com.virtualassistant.vaws.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtualassistant.vaws.user.model.User;
import com.virtualassistant.vaws.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
@Autowired
UserRepository userRepository;

	@Override
	public List<User> findUserAll() {
		return userRepository.findAll();		
	}

	@Override
	public Optional<User> findByUserId(int id) {
		return userRepository.findById(id);
	}
//
//
	@Override
	public void deleteUserByUserId(int userId) {
		userRepository.deleteById(userId);;
	}
//
//
	@Override
	public void saveUser(User payload) throws Exception {
		
		Boolean result = userRepository.existsByUsernameAndDeletedAtIsNull(payload.getUsername());
		if (result == true) {
			throw new Exception("Bu kullanıcı adı daha önce kullanılmış.");
		}
		if (payload.getFirstName() == null || payload.getLastName() == null || payload.getEmail() == null || payload.getPassword() == null) {
			throw new Exception("Boş bırakılmış zorunlu alan var.");
		}
		
		
		userRepository.save(payload);
		
		
		
	}

	@Override
	public User userLogin(String username, String password) throws Exception{
		User user=userRepository.findUserByUsernameAndDeletedAtIsNull(username);
		if(user.getPassword().toString().equals(password)) {
			return user;
		}else {
			throw new Exception("Kayıtlı kullanıcı bulunamadı");
		}
		
	}

}
