package com.nanoka.warehouse.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nanoka.warehouse.Model.Entity.User;


public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Modifying()
    @Query("update User u set u.name=:name where u.id = :id")
    void updateUser(@Param(value = "id") Integer id,   @Param(value = "name") String name);

    Boolean existsByUsername(String username);
}
