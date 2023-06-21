package com.tech.utils;

import com.tech.entites.concretes.Address;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GeneralUtils {

    private static final StringBuilder sb = new StringBuilder();

    public static LocalDateTime convertMillisToLocalDateTime(long millis) {
        if (millis == 0) return null;

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(millis),
                ZoneId.systemDefault()
        );
    }

    public static List<String> buildMedicineList(String medicines) {
        return Arrays.stream(medicines.split("¨!¨")).collect(Collectors.toList());
    }

    public static Integer calculateAge(LocalDate birthDate) {
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

    public static String mergeAddressFields(Address address) {
        return String.join(", ", address.getStreet(), address.getCity(), address.getState(), address.getZipCode());
    }

    public static synchronized String capitalize(String text) {
        if (!StringUtils.hasText(text)) return text;
        sb.setLength(0);

        for (String word : text.split("\\s")) {
            if (StringUtils.hasText(word)) {
                sb
                        .append(word.substring(0, 1).toUpperCase(Locale.US))
                        .append(word.substring(1).toLowerCase(Locale.US))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

}
