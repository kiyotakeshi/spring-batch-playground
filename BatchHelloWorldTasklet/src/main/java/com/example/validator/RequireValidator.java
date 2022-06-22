package com.example.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class RequireValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String key = "require";
        String require = parameters.getString(key);

        if (!StringUtils.hasLength(require)) {
            String errorMessage = "not entered:" + key;
            throw new JobParametersInvalidException(errorMessage);
        }
    }
}
