package ai.giskard.exercise.agendamanagement.services.functionalities;

import ai.giskard.exercise.agendamanagement.model.functional.AgendaEvent;
import ai.giskard.exercise.agendamanagement.model.functional.AvailableSlot;
import ai.giskard.exercise.agendamanagement.model.persistance.Appointment;
import ai.giskard.exercise.agendamanagement.model.persistance.Availability;
import ai.giskard.exercise.agendamanagement.model.persistance.User;
import ai.giskard.exercise.agendamanagement.model.persistance.enums.RepetitionType;
import ai.giskard.exercise.agendamanagement.services.repo.AppointmentRepository;
import ai.giskard.exercise.agendamanagement.services.repo.AvailabilityRepository;
import ai.giskard.exercise.agendamanagement.services.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * This service handles different functionalities of agenda
 * @author Hossain
 */
@Service
public class AgendaService {
    private final Logger logger= LoggerFactory.getLogger(AgendaService.class);

    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;
    @Autowired
    public AgendaService(UserRepository userRepository, AvailabilityRepository availabilityRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.availabilityRepository = availabilityRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Creates an availability for the selected user
     * @param userId
     * @param startDateStr
     * @param startTimeStr
     * @param endDateStr
     * @param endTimeStr
     * @param repeat signifies if this availability is one-time or repeated
     * @param length
     */
    public void createAvailability(long userId, String startDateStr, String startTimeStr, String endDateStr, String endTimeStr, String repeat, int length  ){
        logger.debug("===>createAvailability({},{}, {}, {}, {}, {}, {})",userId, startDateStr,startTimeStr, endDateStr, endTimeStr, repeat, length);
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);
        RepetitionType repetitionType = RepetitionType.valueOf(repeat);
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            logger.error("No user found with userId = {}; throwing error", userId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        else {
            Availability availability = new Availability(user.get(), startDate, startTime, endDate, endTime, repetitionType, length);
            Availability saved = availabilityRepository.save(availability);
            logger.debug("\tAvailability saved with ID = {}", saved.getId());
        }
    }

    /**
     * Get a mapping of availabilities for the selected user
     * @param userId the availability of this user would be shown
     * @param takerId the requester of appointment (to depict the conflicts)
     * @param weekNumber ISO 8601
     * @return mapping for each date, with a sorted set of available slots
     */
    public Map<LocalDate, TreeSet<AvailableSlot>> getWeeklyAvailability(long userId, long takerId, int weekNumber){
        logger.debug("===> getAvailability({}, {})", userId, weekNumber);
        LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);


        LocalDate start = week.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
        logger.debug("\tWeek#{}:{}-{}", weekNumber, start, end);

        TreeMap<LocalDate, TreeSet<AvailableSlot>> slotsMappings = new TreeMap<>();
        for(int i = 0; i < 7; i++)
            slotsMappings.put(start.plusDays(i), new TreeSet<>());

        Optional<User> receiver = userRepository.findById(userId);
        if(receiver.isEmpty()){
            logger.error("No user found with userId = {}; throwing error", userId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        Optional<User> taker = userRepository.findById(takerId);
        if(taker.isEmpty()){
            logger.error("No user found with userId = {}; throwing error", takerId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        List<Availability> generatedAvailabilities = getAvailabilities(start, end, receiver.get());

        if(generatedAvailabilities.size() == 0){
            return slotsMappings;
        }

        List<Availability> availabilities = new ArrayList<>();
        for (Availability av :
                generatedAvailabilities) {
            List<Appointment> apps = this.appointmentRepository.findAppointmentsByReceiverUserAndDateAndStartTimeIsBetweenOrderByStartTime(av.getUser(), av.getStartDate(), av.getStartTime(), av.getEndTime());
            if(apps == null || apps.size() == 0)
                availabilities.add(av);
            else {
                availabilities.add(new Availability(av.getUser(), av.getStartDate(), av.getStartTime(), av.getEndDate(), apps.get(0).getStartTime(), av.getRepetitionType(), av.getLengthInMin()));
                for (int i = 1; i < apps.size(); i++) {
                    availabilities.add(new Availability(av.getUser(), av.getStartDate(), apps.get(i-1).getEndTime(), av.getEndDate(), apps.get(i).getStartTime(), av.getRepetitionType(), av.getLengthInMin()));
                }
                availabilities.add(new Availability(av.getUser(), av.getStartDate(), apps.get(apps.size()-1).getEndTime(), av.getEndDate(), av.getEndTime() , av.getRepetitionType(), av.getLengthInMin()));
            }
        }
        for (Availability av :
                availabilities) {
            TreeSet<AvailableSlot> slots = slotsMappings.get(av.getStartDate());

            List<Appointment> apps = this.appointmentRepository.findAppointmentsByTakerUserAndDateAndStartTimeIsBetweenOrderByStartTime(taker.get(), av.getStartDate(), av.getStartTime(), av.getEndTime());
            if(apps == null || apps.size() == 0)
                slots.addAll(generateSlots(av, false));
            else {
                slots.addAll(generateSlots(new Availability(av.getUser(), av.getStartDate(), av.getStartTime(), av.getEndDate(), apps.get(0).getStartTime(), av.getRepetitionType(), av.getLengthInMin()), false));
                for (int i = 1; i < apps.size(); i++) {
                    slots.addAll(generateSlots(new Availability(av.getUser(), av.getStartDate(), apps.get(i-1).getEndTime(), av.getEndDate(), apps.get(i).getStartTime(), av.getRepetitionType(), av.getLengthInMin()), false));
                }
                slots.addAll(generateSlots(new Availability(av.getUser(), av.getStartDate(), apps.get(apps.size()-1).getEndTime(), av.getEndDate(), av.getEndTime() , av.getRepetitionType(), av.getLengthInMin()), false));
                for (Appointment app :
                        apps) {
                    slots.addAll(generateSlots(new Availability(av.getUser(), av.getStartDate(), app.getStartTime(), av.getEndDate(), app.getEndTime(), av.getRepetitionType(), av.getLengthInMin()), true));
                }
            }
            slotsMappings.put(av.getStartDate(), slots);
        }
        return slotsMappings;
    }

    /**
     * Auxiliary function to list all availabilities of user (no regard to conflicts)
     * @param start
     * @param end
     * @param receiver
     * @return
     */
    private List<Availability> getAvailabilities(LocalDate start, LocalDate end, User receiver) {
        List<Availability> availabilities = availabilityRepository.findByUserAndEndDateAfterAndStartDateBefore(receiver, start, end);
        List<Availability> generatedAvailabilities = new ArrayList<>();
        for (Availability av:
             availabilities) {
            switch (av.getRepetitionType()) {
                case NOT_REPEATED -> {

                    generatedAvailabilities.add(av);

                }
                case DAILY -> {
                    LocalDate date = start;
                    if(date.isBefore(av.getStartDate()))
                        date = av.getStartDate();
                    while(date.isBefore(end) && date.isBefore(av.getEndDate())){
                        generatedAvailabilities.add(new Availability(receiver, date,av.getStartTime(), date, av.getEndTime(),  RepetitionType.NOT_REPEATED, av.getLengthInMin()));
                        date = date.plusDays(1);
                    }

                }
                case WEEKLY -> {
                    int dow = av.getStartDate().getDayOfWeek().getValue();
                    LocalDate date = start.plusDays(dow);
                    if(date.isAfter(av.getStartDate()) && date.isBefore(av.getEndDate()))
                        generatedAvailabilities.add(new Availability(receiver, date,av.getStartTime(), date, av.getEndTime(),  RepetitionType.NOT_REPEATED, av.getLengthInMin()));
                }
                case MONTHLY -> {
                    //TODO
                }
            }
        }

        logger.debug("\tGenerated {} availabilities from DB", generatedAvailabilities.size());
        Collections.sort(generatedAvailabilities);
        return generatedAvailabilities;
    }

    /**
     * Converts availabilities to selectable time slots
     * @param availability
     * @param conflict
     * @return
     */
    private List<AvailableSlot> generateSlots(Availability availability, boolean conflict){
        ArrayList<AvailableSlot> slots = new ArrayList<>();
        int length = availability.getLengthInMin();
        LocalTime time = availability.getStartTime();
        while(time.plusMinutes(length).isBefore(availability.getEndTime())|| time.plusMinutes(length).equals(availability.getEndTime()) ){
            //    public AvailableSlot(int receiverUserId, LocalTime startTime, LocalTime endTime, LocalDate date, boolean noConflict) {
            AvailableSlot availableSlot = new AvailableSlot(availability.getUser().getId(), time, time.plusMinutes(length), availability.getStartDate(), !conflict);
            slots.add(availableSlot);
            time = time.plusMinutes(length);
        }
        return slots;
    }

    /**
     * This function books an appointment between taker and receiver at the selected time
     * @param takerId
     * @param receiverId
     * @param dateStr
     * @param startTimeStr
     * @param endTimeStr
     */
    public void createAppointment (long takerId, long receiverId, String dateStr, String startTimeStr, String endTimeStr){
        logger.debug("===> createAppointment({}, {}, {}, {}, {})", takerId, receiverId, dateStr, startTimeStr, endTimeStr);


        LocalDate date = LocalDate.parse(dateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        Optional<User> taker = userRepository.findById(takerId);
        if(taker.isEmpty()){
            logger.error("No user (taker) found with userId = {}; throwing error", takerId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        Optional<User> receiver = userRepository.findById(receiverId);
        if(receiver.isEmpty()){
            logger.error("No user (receiver) found with userId = {}; throwing error", takerId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        //TODO check availabilities
        Appointment appointment = new Appointment(taker.get(), receiver.get(), date, startTime, endTime);
        Appointment saved = appointmentRepository.save(appointment);
        logger.debug("\tAppointment created with ID = {}", saved.getId());
    }

    public Map<LocalDate, TreeSet<AgendaEvent>> getWeeklyAgenda(long userId, int weekNumber){
        logger.debug("getWeeklyAgenda({}, {})", userId, weekNumber);
        LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
        LocalDate start = week.with(DayOfWeek.MONDAY);
        TreeMap<LocalDate, TreeSet<AgendaEvent>> agenda = new TreeMap<>();
        for(int i = 0; i < 7; i++)
            agenda.put(start.plusDays(i), new TreeSet<>());
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            logger.error("No user found with userId = {}; throwing error", userId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        List<Availability> availabilities = getAvailabilities(start, start.plusDays(6), user.get());

        for (Availability a :
                availabilities) {
            TreeSet<AgendaEvent> set = agenda.get(a.getStartDate());
            set.add(new AgendaEvent(a, a.getRepetitionType().name()));
            agenda.put(a.getStartDate(),set );
        }

        for (LocalDate date :
                agenda.keySet()) {
            List<Appointment> appointments = appointmentRepository.findAppointmentsByDateAndUser(date, user.get()
            );
            TreeSet<AgendaEvent> events = agenda.get(date);
            appointments.stream()
                    .map(app -> new AgendaEvent(app, String.format("Meeting between %s and %s",
                            app.getTakerUser().getName(),
                            app.getReceiverUser().getName()).replace(user.get().getName(), "you")))
                    .forEachOrdered(events::add);
        }

        return agenda;

    }

}
