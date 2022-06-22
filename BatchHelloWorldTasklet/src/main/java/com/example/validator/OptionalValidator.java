package com.example.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class OptionalValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String key = "option";
        String option = parameters.getString(key);

        if (!StringUtils.hasLength(option)) {
            return;
        }

        try {
            Integer.parseInt(option);
        } catch (NumberFormatException e) {
            String errorMessage = "not number: value=" + key;
            throw new JobParametersInvalidException(errorMessage);
        }
    }
}
