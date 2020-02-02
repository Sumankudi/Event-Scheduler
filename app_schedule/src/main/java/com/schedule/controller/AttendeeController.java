package com.schedule.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedule.model.EventType;
import com.schedule.model.ScheduledEvent;
import com.schedule.repository.AttendeeRepository;
import com.schedule.repository.EventOwnerRepository;
import com.schedule.util.EventValidations;

/**
 * CONTROLLER HELPS IN SCHEDULE SOME EVENTS AND 
 * CAN APPROVED OR REJECTED BY EVENT OWNER
 * 
 * @author skudikala
 * 
 */
@RestController
public class AttendeeController extends BaseController implements EventValidations {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttendeeController.class);

	@Autowired
	private AttendeeRepository attendeeRepository;

	@Autowired
	private EventOwnerRepository eventOwnerRepository;

	@Value("${event.already.exist}")
	private String scheduledEventAlreadyExist;

	@Value("${invalid.scheduled.time}")
	private String invalidScheduledTime;

	@Value("${no.eventtype.found}")
	private String noEventFound;

	/**
	 * METHOD HELPS IN LIST OUT THE ALL THE SCHEDULED EVENTS BASED ON REQUESTED
	 * USERS
	 * 
	 * @param email
	 * @return List<ScheduledEvent>
	 */
	@GetMapping("/event")
	public ResponseEntity<List<ScheduledEvent>> getScheduledEvents(@RequestParam String email) {
		List<ScheduledEvent> list = null;

		if (StringUtils.isEmpty(email)) {
			list = attendeeRepository.findAll();
		} else {
			list = attendeeRepository.findAllByemail(email);
		}

		return new ResponseEntity<List<ScheduledEvent>>(list, HttpStatus.OK);
	}

	/**
	 * METHOD TO SCHEDULE EVENTS BASED ON ATTENDEE REQUEST. METHOS WILL PERFORM
	 * VALIDATION ON SCHEDULED TIME AND EXISTING EVENTS.
	 * 
	 * @param scheduledTO
	 * @return response entity
	 */
	@PostMapping("/event")
	public ResponseEntity<?> scheduleEvent(@Valid @RequestBody ScheduledEvent scheduledTO) {

		EventType eventType = eventOwnerRepository.findByEventName(scheduledTO.getEventType());
		List<ScheduledEvent> scheduledEvents = attendeeRepository.findScheduledEvents(scheduledTO.getEmail(),
				scheduledTO.getEventType());

		boolean alreadyExist = validateExistingSchedule(scheduledEvents, eventType);

		if (alreadyExist) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info(scheduledEventAlreadyExist);
			return new ResponseEntity<>(scheduledEventAlreadyExist, HttpStatus.NOT_ACCEPTABLE);
		} else if (validateScheduleTime(scheduledTO, eventType)) {
			if (LOGGER.isInfoEnabled())
				LOGGER.info(invalidScheduledTime);
			return new ResponseEntity<>(invalidScheduledTime, HttpStatus.NOT_ACCEPTABLE);
		}

		/**** Adding additional details to ScheduledEvent */
		scheduledTO.setStatus("Pending");
		scheduledTO.setCreatedBy(scheduledTO.getEmail());

		return new ResponseEntity<>(attendeeRepository.save(scheduledTO), HttpStatus.OK);
	}

	/**
	 * METHOD HELPS IN APPROVE OR REJECTE THE SCHEDLED EVENTS BY EVENT OWNER.
	 * 
	 * @param scheduleId
	 * @return ScheduledEvent
	 */
	@PutMapping("/event/{scheduleId}")
	public ResponseEntity<?> updateScheduledEvents(@PathVariable long scheduleId, @RequestParam String status,
			@RequestHeader String ownerId) {

		Optional<ScheduledEvent> scheduledEvent = attendeeRepository.findById(scheduleId);

		ScheduledEvent event = null;
		if (scheduledEvent.isPresent()) {
			event = scheduledEvent.get();
			event.setStatus(status);
			event.setModifiedBy(ownerId);
		} else {
			return new ResponseEntity<>(noEventFound, HttpStatus.OK);
		}

		return new ResponseEntity<>(attendeeRepository.save(event), HttpStatus.OK);
	}

}
