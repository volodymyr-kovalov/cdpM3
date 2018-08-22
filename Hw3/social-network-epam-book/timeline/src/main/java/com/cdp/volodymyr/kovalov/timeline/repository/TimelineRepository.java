package com.cdp.volodymyr.kovalov.timeline.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cdp.volodymyr.kovalov.timeline.entity.Timeline;

public interface TimelineRepository extends CrudRepository<Timeline, Long> {

    List<Timeline> findByOwner(String owner);
}
