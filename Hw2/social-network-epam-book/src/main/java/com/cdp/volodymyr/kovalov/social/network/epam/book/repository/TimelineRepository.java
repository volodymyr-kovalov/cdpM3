package com.cdp.volodymyr.kovalov.social.network.epam.book.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Timeline;

public interface TimelineRepository extends CrudRepository<Timeline, Long> {

    List<Timeline> findByOwner(String owner);
}
