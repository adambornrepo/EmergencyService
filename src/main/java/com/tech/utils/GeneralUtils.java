package com.tech.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralUtils {

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

}
