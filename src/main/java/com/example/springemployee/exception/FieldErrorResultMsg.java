package com.example.springemployee.exception;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class FieldErrorResultMsg {
    public FieldErrorResultMsg() {
        super();
    }

    public static String getMsgError(BindingResult result) {
        String errorMsg = "";
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                errorMsg = "Error in Filed : " + key + " - Because : " + value;
            }

        }
        return errorMsg;
    }
}
