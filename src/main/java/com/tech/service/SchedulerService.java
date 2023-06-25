package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.exception.custom.SchedulerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;

    @Scheduled(cron = "${scheduler.cron.pattern.expired}")
    public void updateExpiredAppointmentsAndProceduresPeriodically() {
        try {
            coordinationService.updateStatusForExpiredAppointmentsAndProcedures();
        } catch (Exception e) {
            log.error("Error updating expired appointments and procedures : " + e.getMessage());
            throw new SchedulerException(String.format(apiMessages.getMessage("error.scheduler.update.expired"), e.getMessage()));
        }
    }

    @Scheduled(cron = "${scheduler.cron.pattern.applied}")
    public void updateAppliedAppointmentsAndProceduresPeriodically() {
        try {
            coordinationService.updateStatusForAppliedProcedures();
        } catch (Exception e) {
            log.error("Error updating applied procedures : " + e.getMessage());
            throw new SchedulerException(String.format(apiMessages.getMessage("error.scheduler.update.applied"), e.getMessage()));
        }
    }


}
