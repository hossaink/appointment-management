package ai.giskard.exercise.agendamanagement.controller;

import ai.giskard.exercise.agendamanagement.model.functional.AgendaEvent;
import ai.giskard.exercise.agendamanagement.model.functional.AvailableSlot;

import ai.giskard.exercise.agendamanagement.services.functionalities.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;

@Controller
@RequestMapping("/api/agenda")
public class AgendaController {
    private final AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping(path="/book")
    @ResponseStatus(value = HttpStatus.OK)
    public void createAppointment (
            @RequestParam long takerId,
            @RequestParam long receiverId,
            @RequestParam String dateStr,
            @RequestParam  String startTimeStr,
            @RequestParam String endTimeStr
    ) {
            agendaService.createAppointment(takerId, receiverId, dateStr, startTimeStr, endTimeStr) ;
    }

    @PostMapping(path="/availability")
    @ResponseStatus(value = HttpStatus.OK)
    public void createAppointment (
            @RequestParam long userId,
            @RequestParam String startDateStr,
            @RequestParam  String startTimeStr,
            @RequestParam String endDateStr,
            @RequestParam String endTimeStr,
            @RequestParam String repeat,
            @RequestParam int length
    ) {

        agendaService.createAvailability(userId, startDateStr, startTimeStr, endDateStr, endTimeStr, repeat, length);
    }

    @GetMapping(path="/slots")
    public @ResponseBody Map<LocalDate, TreeSet<AvailableSlot>>  getWeeklySlots(
            @RequestParam long takerId,
            @RequestParam long receiverId,
            @RequestParam int weekNumber){
        return agendaService.getWeeklyAvailability(receiverId,takerId, weekNumber);
    }

    @GetMapping(path="/week")
    public @ResponseBody Map<LocalDate, TreeSet<AgendaEvent>>  getWeeklyAgenda(
            @RequestParam long userId,
            @RequestParam int weekNumber){
        return agendaService.getWeeklyAgenda(userId,weekNumber);
    }

}
