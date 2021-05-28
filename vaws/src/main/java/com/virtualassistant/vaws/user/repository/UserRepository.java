package com.virtualassistant.vaws.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virtualassistant.vaws.event.model.EventStatusEnum;
import com.virtualassistant.vaws.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  
  Boolean existsByUsernameAndDeletedAtIsNull(String username);

  User findUserByUsernameAndDeletedAtIsNull( @Param("username") String username);
  
}
