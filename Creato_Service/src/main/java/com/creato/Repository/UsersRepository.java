package com.creato.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.creato.Entities.UserEntity;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Integer> {

	@Query("select u from UserEntity u where u.status = 1")
	public List<UserEntity> getAllUsers();

	@Query("select u from UserEntity u where u.username = :username and u.status = :status")
	public UserEntity findByUsername(@Param("username") String username, @Param("status") int status);
	
	@Query("select u from UserEntity u where u.username = :username")
	public UserEntity findByUsername(@Param("username") String username);
	
	@Query("select u from UserEntity u where u.username = :username and u.password = :password")
	public UserEntity findByCreds(@Param("username") String username, @Param("password") String password);

}
