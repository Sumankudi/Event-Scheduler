package com.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.schedule.controller.OwnerController;
import com.schedule.model.EventType;
import com.schedule.repository.EventOwnerRepository;

@SpringBootTest
class OwnerControllerTests {

	private EventType event;

	@InjectMocks
	@Spy
	OwnerController ownerController;

	@Mock
	EventOwnerRepository eventOwnerRepository;

	private void setUp() {
		event = new EventType();
		event.setEventName("Interview");
		event.setId(1);
		event.setEventStartTime(LocalDateTime.now());
		event.setEventEndTime(LocalDateTime.now().plus(Duration.ofMinutes(30)));
		event.setCreatedBy("owner@mail.com");

		ReflectionTestUtils.setField(ownerController, "eventNotFound", "Event types not found.");
		ReflectionTestUtils.setField(ownerController, "eventDeleteSucess", "Event type deleted successfully.");
	}

	@Test
	public void testFindAll() {
		setUp();
		List<EventType> evetTypes = new ArrayList<>();
		evetTypes.add(event);

		when(eventOwnerRepository.findAll()).thenReturn(evetTypes);
		ResponseEntity<?> result = ownerController.getEventTypes();
		List<EventType> list = (List<EventType>) result.getBody();

		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0).getEventName()).isEqualTo(event.getEventName());
	}

	@Test
	public void testFindNoEvents() {
		setUp();
		List<EventType> list = new ArrayList<>(0);
		when(eventOwnerRepository.findAll()).thenReturn(list);

		ResponseEntity<?> result = ownerController.getEventTypes();
		String message = (String) result.getBody();

		assertThat(message).isEqualTo("Event types not found.");
	}

	@Test
	public void testAddEventType() {
		setUp();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(eventOwnerRepository.save(any(EventType.class))).thenReturn(event);
		EventType eventType = ownerController.createEventType(event, "owner@mail.com");

		assertThat(eventType.getEventName()).isEqualTo("Interview");
	}

	@Test
	public void testDeleteEventType() {
		setUp();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		doNothing().when(eventOwnerRepository).deleteById(any(Long.class));
		ResponseEntity<String> message = ownerController.deleteEventType(1L);

		assertThat((String) message.getBody()).isEqualTo("Event type deleted successfully.");
	}

}
