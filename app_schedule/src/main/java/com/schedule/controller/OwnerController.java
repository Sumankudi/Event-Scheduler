package com.schedule.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.schedule.model.EventType;
import com.schedule.repository.EventOwnerRepository;

/**
 * CONTROLLER HELPS EVENT OWNER TO CREATE,DELETE,GET
 * THE EVENT TYPES.
 * @author skudikala
 *
 */
@RestController
public class OwnerController extends BaseController{
	private static final Logger LOGGER = LoggerFactory.getLogger(OwnerController.class);
	
	@Autowired
	private EventOwnerRepository eventOwnerRepository;
	
	@Value("${eventtype.deleted.notfound}")
    private String eventNotFound;
	
	@Value("${eventtype.deleted.successfully}")
    private String eventDeleteSucess;
	
    /**
     * METHOD HELPS TO GET THE ALL EVENT TYPES FROM DB
     * @return list of EventTypes
     */
    @GetMapping("/eventType")
	public ResponseEntity<?> getEventTypes() {
		List<EventType> list = eventOwnerRepository.findAll();
		
		if(list.size()==0) 
			return new ResponseEntity<>(eventNotFound, HttpStatus.OK);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
    
	/**
	 * METHOD HELPS TO CREATE EVENT TYPES
	 * @param eventType
	 * @param ownerId
	 * @return EventType
	 */
	@PostMapping("/eventType")
	public EventType createEventType(@Valid @RequestBody EventType eventType, @RequestHeader String ownerId) {
		eventType.setCreatedBy(ownerId);
		return eventOwnerRepository.save(eventType);
	}
	
	/**
	 * METHOD HELPS TO DELETE EVENT TYPES
	 * @param eventTypeId
	 * @return response message
	 */
	@DeleteMapping("/eventType/{eventTypeId}")
	public ResponseEntity<String> deleteEventType(@PathVariable Long eventTypeId) {
		try {
			eventOwnerRepository.deleteById(eventTypeId);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Exception on deleteEventType {}",e.getMessage());
			return new ResponseEntity<String>(eventNotFound, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>(eventDeleteSucess, HttpStatus.OK);
	}
}
