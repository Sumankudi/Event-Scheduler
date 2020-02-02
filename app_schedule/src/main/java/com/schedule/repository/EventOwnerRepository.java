/**
 * 
 */
package com.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.schedule.model.EventType;
import com.schedule.model.ScheduledEvent;

/**
 * @author skudikala
 *
 */
@Repository
public interface EventOwnerRepository extends JpaRepository<EventType, Long> {

	@Query("FROM EventType WHERE eventName = ?1")
    EventType findByEventName(String eventName);
}
