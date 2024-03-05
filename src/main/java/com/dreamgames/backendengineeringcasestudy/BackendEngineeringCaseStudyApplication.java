package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.business.constants.UserDefaultValues;
import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.BusinessException;
import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.ProblemDetails;
import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.ValidationProblemDetails;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@SpringBootApplication
@RestControllerAdvice
@EnableScheduling
public class BackendEngineeringCaseStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendEngineeringCaseStudyApplication.class, args);

        System.out.println("**********************************************************************");
        System.out.println("************* Server is started. Listening port 8080 ... *************");
        System.out.println("**********************************************************************");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetails handleBusinessException(BusinessException businessException) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setMessage(businessException.getMessage());

        return problemDetails;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetails handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ValidationProblemDetails validationProblemDetails = new ValidationProblemDetails();
        validationProblemDetails.setMessage("VALIDATION.EXCEPTION");
        validationProblemDetails.setValidationErrors(new HashMap<String,String>());

        methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> validationProblemDetails
                        .getValidationErrors()
                        .put(fieldError.getField(), fieldError.getDefaultMessage()));

        return validationProblemDetails;
    }

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }


}

