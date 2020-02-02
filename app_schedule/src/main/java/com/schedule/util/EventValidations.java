package com.schedule.util;

import java.util.List;

import com.schedule.model.EventType;
import com.schedule.model.ScheduledEvent;

/**
 * @author skudikala
 *
 */
public interface EventValidations {

	default boolean validateScheduleTime(ScheduledEvent scheduledEvent, EventType eventType) {

		if (scheduledEvent.getScheduleStartTime().isBefore(eventType.getEventStartTime())
				|| scheduledEvent.getScheduleStartTime().isAfter(eventType.getEventEndTime()))
			return true;

		return false;
	}

	default boolean validateExistingSchedule(List<ScheduledEvent> scheduledEvent, EventType eventType) {

		long existingCount = scheduledEvent.stream()
				.filter(event -> ((event.getScheduleStartTime().isAfter(eventType.getEventStartTime())
						|| event.getScheduleStartTime().isEqual(eventType.getEventStartTime()))
						&& (event.getScheduleStartTime().isBefore(eventType.getEventEndTime())
								|| event.getScheduleStartTime().isEqual(eventType.getEventEndTime()))))
				.count();
		
		return existingCount > 0 ? true : false;
	}
}
