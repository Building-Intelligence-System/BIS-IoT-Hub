package com.device.server.repository;

import com.device.server.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Integer> {
}
