/**
 * 
 */
package com.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.schedule.model.ScheduledEvent;

/**
 * @author skudikala
 *
 */
@Repository
public interface AttendeeRepository extends JpaRepository<ScheduledEvent, Long> {
	
	@Query("FROM ScheduledEvent WHERE email = ?1")
    List<ScheduledEvent> findAllByemail(String email);
	
	@Query("FROM ScheduledEvent WHERE email = ?1 and eventType = ?2")
	List<ScheduledEvent> findScheduledEvents(String email, String eventType);
	
}
