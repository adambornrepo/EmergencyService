package com.tech.payload.annotations.validator;

import com.tech.payload.annotations.custom.TCKN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TcknValidator implements ConstraintValidator<TCKN, String> {

    private final int TCKN_LENGTH = 11;

    @Override
    public boolean isValid(String tckn, ConstraintValidatorContext context) {
        if (tckn.length() != 11) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("TCKN must be " + TCKN_LENGTH + " characters")
                    .addConstraintViolation();
            return false;
        }
        if (!tckn.matches("\\d+")) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("TCKN must be numbers only")
                    .addConstraintViolation();
            return false;
        }

        long tcknDigits = Long.parseLong(tckn);
        int lastDigit = (int) (tcknDigits % 10);
        int controlDigit = (int) ((tcknDigits / 10) % 10);
        int firstDigit = (int) (tcknDigits / 10000000000L);
        int odd = 0;
        int even = 0;
        tcknDigits /= 100;
        while (tcknDigits > 0) {
            boolean evenDigit = ((int) Math.log10(tcknDigits) + 1) % 2 == 0;
            if (evenDigit) even += tcknDigits % 10;
            else odd += tcknDigits % 10;
            tcknDigits /= 10;
        }
        boolean control1 = ((odd * 7) + (even * 9)) % 10 == controlDigit;
        boolean control2 = ((odd * 7) - even) % 10 == controlDigit;
        boolean control3 = (odd + even + controlDigit) % 10 == lastDigit;
        boolean control4 = (odd * 8) % 10 == lastDigit;
        boolean control5 = firstDigit != 0;

        return control1 && control2 && control3 && control4 && control5;
    }
}
