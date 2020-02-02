package com.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.schedule.controller.AttendeeController;
import com.schedule.model.EventType;
import com.schedule.model.ScheduledEvent;
import com.schedule.repository.AttendeeRepository;
import com.schedule.repository.EventOwnerRepository;

@SpringBootTest
class AttendeeControllerTests {

	@InjectMocks
	@Spy
	AttendeeController attendeeController;

	@Mock
	EventOwnerRepository eventOwnerRepository;

	@Mock
	AttendeeRepository attendeeRepository;

	private ScheduledEvent scheduledEvent;
	private EventType event;

	private void setUp() {
		scheduledEvent = new ScheduledEvent();
		scheduledEvent.setEventType("Interview");
		scheduledEvent.setEmail("attende@mail.com");
		scheduledEvent.setMessage("Scheduling Java Interview");
		scheduledEvent.setScheduleStartTime(LocalDateTime.now().plus(Duration.ofMinutes(15)));

		event = new EventType();
		event.setEventName("Interview");
		event.setId(1);
		event.setEventStartTime(LocalDateTime.now());
		event.setEventEndTime(LocalDateTime.now().plus(Duration.ofMinutes(30)));
		event.setCreatedBy("owner@mail.com");

		ReflectionTestUtils.setField(attendeeController, "scheduledEventAlreadyExist",
				"Scheduled event already in the system.");
		ReflectionTestUtils.setField(attendeeController, "invalidScheduledTime", "Please choose valid scheduled time.");
		ReflectionTestUtils.setField(attendeeController, "noEventFound", "scheduled event not found to update.");
	}

	@Test
	public void testGetScheduledEvents() {
		setUp();
		List<ScheduledEvent> list = new ArrayList<>();
		list.add(scheduledEvent);

		when(attendeeRepository.findAll()).thenReturn(list);
		ResponseEntity<?> result = attendeeController.getScheduledEvents("");
		List<ScheduledEvent> scheduledlist = (List<ScheduledEvent>) result.getBody();

		assertThat(scheduledlist.size()).isEqualTo(1);
		assertThat(scheduledlist.get(0).getEventType()).isEqualTo(scheduledEvent.getEventType());
	}

	@Test
	public void testGetScheduledEventsByEmail() {
		setUp();
		List<ScheduledEvent> list = new ArrayList<>();
		list.add(scheduledEvent);

		when(attendeeRepository.findAllByemail(any(String.class))).thenReturn(list);
		ResponseEntity<?> result = attendeeController.getScheduledEvents("attende@mail.com");
		List<ScheduledEvent> scheduledlist = (List<ScheduledEvent>) result.getBody();

		assertThat(scheduledlist.size()).isEqualTo(1);
		assertThat(scheduledlist.get(0).getEventType()).isEqualTo(scheduledEvent.getEventType());
	}

	@Test
	public void testAlreadyExistScheduleEvent() {
		setUp();
		List<ScheduledEvent> scheduledEvents = new ArrayList<>();
		scheduledEvents.add(scheduledEvent);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(attendeeRepository.save(any(ScheduledEvent.class))).thenReturn(scheduledEvent);

		when(eventOwnerRepository.findByEventName(any(String.class))).thenReturn(event);
		when(attendeeRepository.findScheduledEvents(any(String.class), any(String.class))).thenReturn(scheduledEvents);

		ResponseEntity<?> response = attendeeController.scheduleEvent(scheduledEvent);

		assertThat(((String) response.getBody())).isEqualTo("Scheduled event already in the system.");
	}

	@Test
	public void testInvalidTimescheduleEvent() {
		setUp();
		List<ScheduledEvent> scheduledEvents = new ArrayList<>();
		scheduledEvent.setScheduleStartTime(LocalDateTime.now().minus(Duration.ofMinutes(15)));
		scheduledEvents.add(scheduledEvent);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(attendeeRepository.save(any(ScheduledEvent.class))).thenReturn(scheduledEvent);

		when(eventOwnerRepository.findByEventName(any(String.class))).thenReturn(event);
		when(attendeeRepository.findScheduledEvents(any(String.class), any(String.class))).thenReturn(scheduledEvents);

		ResponseEntity<?> response = attendeeController.scheduleEvent(scheduledEvent);

		assertThat(((String) response.getBody())).isEqualTo("Please choose valid scheduled time.");
	}

	@Test
	public void testScheduleEvent() {
		setUp();
		List<ScheduledEvent> scheduledEvents = new ArrayList<>(0);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(attendeeRepository.save(any(ScheduledEvent.class))).thenReturn(scheduledEvent);

		when(eventOwnerRepository.findByEventName(any(String.class))).thenReturn(event);
		when(attendeeRepository.findScheduledEvents(any(String.class), any(String.class))).thenReturn(scheduledEvents);

		ResponseEntity<?> response = attendeeController.scheduleEvent(scheduledEvent);

		assertThat(((ScheduledEvent) response.getBody()).getEmail()).isEqualTo("attende@mail.com");
	}

	@Test
	public void testUpdateScheduledEvents() {
		setUp();
		Optional<ScheduledEvent> optEvent = Optional.of(scheduledEvent);
		scheduledEvent.setStatus("Approved");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		when(attendeeRepository.findById(any(Long.class))).thenReturn(optEvent);
		when(attendeeRepository.save(any(ScheduledEvent.class))).thenReturn(scheduledEvent);
		
		ResponseEntity<ScheduledEvent> response = (ResponseEntity<ScheduledEvent>) attendeeController.updateScheduledEvents(1L, "Approved", "owner@mail.com");

		assertThat(response.getBody().getStatus()).isEqualTo("Approved");
	}

}
