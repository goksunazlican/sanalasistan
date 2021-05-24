package com.virtualassistant.vaws.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virtualassistant.vaws.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//@Query("Select e from User e where User.mail =  :email  AND delete_at IS NULL")
//User findByEmail(@Param("email") String email);
//
//@Query("Select e from User e where User.id = :id AND delete_at IS NULL")
//User findByUserId(@Param("id") int id);
//
Boolean existsByUsernameAndDeletedAtIsNull(String username);
}
