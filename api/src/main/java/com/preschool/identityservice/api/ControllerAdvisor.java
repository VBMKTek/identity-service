package com.preschool.identityservice.api;

import com.preschool.libraries.base.common.ControllerAdvisorAbstract;
import com.preschool.libraries.base.exception.ApplicationException;
import com.preschool.libraries.base.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ControllerAdvisorAbstract {}
