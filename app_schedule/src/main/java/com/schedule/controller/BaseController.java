/**
 * 
 */
package com.schedule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author skudikala
 *
 */
public class BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OwnerController.class);
	
	@Value("${internal.exception}")
    private String internalError;
	
	
	/**
	 * METHOD TO HANDLE DAO EXCEPTIONS
     * @param DataAccessException
     * @return User message
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleException(DataAccessException e) {
        LOGGER.error("Exception on OwnerController {}",e.getMessage());

        return new ResponseEntity<>(internalError, HttpStatus.NOT_ACCEPTABLE);
    }
}
