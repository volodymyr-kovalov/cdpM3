package com.cdp.volodymyr.kovalov.social.network.epam.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByRoleName(String roleName);
}
