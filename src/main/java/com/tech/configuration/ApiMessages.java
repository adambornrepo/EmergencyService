package com.tech.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ApiMessages {

    private final MessageSource messageSource;

    public String getMessage(String message) {
        return getMessage(message, null, "Error: Message not found", Locale.getDefault());
    }

    public String getMessage(String message, Object[] args) {
        return getMessage(message, args, "Error: Message not found", Locale.getDefault());
    }

    public String getMessage(String message, Object[] args, String defaultMessage) {
        return getMessage(message, args, defaultMessage, Locale.getDefault());
    }

    public String getMessage(String message, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(message, args, defaultMessage, locale);
    }
}
