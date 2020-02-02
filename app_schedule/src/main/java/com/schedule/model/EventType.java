package com.schedule.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author skudikala
 *
 */

@Entity
@Table(name = "event_type")
public class EventType extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "event_name")
	private String eventName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "event_start_time")
	private LocalDateTime eventStartTime;
	
	@Column(name = "event_end_time")
	private LocalDateTime eventEndTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "modified_by")
	private String modifiedBy;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the eventStartTime
	 */
	public LocalDateTime getEventStartTime() {
		return eventStartTime;
	}

	/**
	 * @param eventStartTime the eventStartTime to set
	 */
	public void setEventStartTime(LocalDateTime eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	/**
	 * @return the eventEndTime
	 */
	public LocalDateTime getEventEndTime() {
		return eventEndTime;
	}

	/**
	 * @param eventEndTime the eventEndTime to set
	 */
	public void setEventEndTime(LocalDateTime eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventName, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EventType other = (EventType) obj;
		return Objects.equals(eventName, other.eventName)
				&& id == other.id;
	}

}
