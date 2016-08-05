package com.github.talbotgui.minibootrestjpa.rest.controleur;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);

	@ResponseBody
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> defaultErrorHandler(final HttpServletRequest req, final Exception e) {
		LOG.error("Erreur traitée sur la requête " + req.getRequestURI(), e);
		return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
