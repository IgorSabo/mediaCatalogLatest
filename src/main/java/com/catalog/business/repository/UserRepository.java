package com.catalog.business.repository;

import com.catalog.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	public Optional<User> findByUsername(String userName);

	public Optional<User> findByEmail(String email);

	public Optional<User> findById(int id);
}
