package com.cdp.volodymyr.kovalov.social.network.epam.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;

public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    Boolean isUsernameExist(@Param("username") String username);

    List<User> findByUsernameIn(List<String> usernames);
}
