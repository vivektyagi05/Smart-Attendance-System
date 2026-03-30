package com.smart.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.attendance.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findTop2ByOrderByEventDateAsc();
}