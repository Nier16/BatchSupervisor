package fr.ag2rlamondiale.hec.batchsupervisor.service.process;

import fr.ag2rlamondiale.hec.batchsupervisor.dto.Schedule;
import fr.ag2rlamondiale.hec.batchsupervisor.mockdata.ScheduleMockData;
import fr.ag2rlamondiale.hec.batchsupervisor.model.ScheduleInformation;
import fr.ag2rlamondiale.hec.batchsupervisor.model.Slot;
import fr.ag2rlamondiale.hec.batchsupervisor.service.data.ScheduleDataService;
import fr.ag2rlamondiale.hec.batchsupervisor.useful.Useful;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleDataService scheduleDataService;

    private final ScheduleMockData dm = new ScheduleMockData();

    @Test
    public void load() {
        Mockito.when(scheduleDataService.getBatchSchedules(dm.idBatch1))
                .thenReturn(dm.schedulesBatch1);
        scheduleService.load(dm.idBatch1);
        assertEquals(ReflectionTestUtils.getField(scheduleService, "schedules"), dm.schedulesBatch1);
    }

    @Test
    public void loadSlots() {
        int delta = 120;
        Schedule schedule = new Schedule(60, 0, 360, delta);
        LocalDateTime start = Useful.getTodayWithTime(0,30);
        LocalDateTime end = Useful.getTodayWithTime(1,30);
        List<Slot> result = Arrays.asList(
                new Slot(Useful.getTodayWithTime(0,0), delta),
                new Slot(Useful.getTodayWithTime(1,0), delta)
        );
        ReflectionTestUtils.setField(
                scheduleService,
                "schedules",
                Collections.singletonList(schedule)
        );

        scheduleService.loadSlots(start, end);

        assertEquals(ReflectionTestUtils.getField(scheduleService, "slots"), result);
    }

    @Test
    public void next() {
        ReflectionTestUtils.setField(scheduleService, "slots", Collections.singletonList(dm.slot1));
        assertEquals(scheduleService.next(), dm.slot1);
        assertNull(scheduleService.next());
    }

    @Test
    public void getScheduleInformation() {
        int delta = 120;
        String idBatch = "1";
        Schedule schedule = new Schedule(0, 60, delta);
        ScheduleInformation result = new ScheduleInformation();
        LocalDateTime time = Useful.getTodayWithTime(0, 30);
        result.setActive(new Slot(Useful.getTodayWithTime(0, 0), Useful.getTodayWithTime(1, 0), delta));
        result.setNext(new Slot(Useful.getTodayWithTime(0, 0).plusDays(1), Useful.getTodayWithTime(1, 0).plusDays(1), delta));
        result.setDescription(Collections.singletonList(null));

        Mockito.when(scheduleDataService.getBatchSchedules(idBatch))
                .thenReturn(Collections.singletonList(schedule));

        assertEquals(scheduleService.getScheduleInformation(idBatch, time), result);
    }

    @Test
    public void getActiveSlotForSchedule() {
        int delta = 120;
        Schedule schedule = new Schedule(30, 0, 60, delta);
        LocalDateTime time = Useful.getTodayWithTime(0, 0);
        LocalDateTime badTime = time.plusMinutes(15);
        Slot result = new Slot(time, delta);

        assertNull(scheduleService.getActiveSlotForSchedule(schedule, badTime));
        assertEquals(scheduleService.getActiveSlotForSchedule(schedule, time), result);
    }

    @Test
    public void getNextSlotForSchedule() {
        int delta = 120;
        Schedule schedule = new Schedule(0, 60, delta);
        LocalDateTime time = Useful.getTodayWithTime(0, 30);
        Slot result = new Slot(
                Useful.getTodayWithTime(0, 0).plusDays(1),
                Useful.getTodayWithTime(1, 0).plusDays(1),
                delta);

        assertEquals(scheduleService.getNextSlotForSchedule(schedule, time), result);
    }

    @Test
    public void checkDayAvailability(){
        Schedule schedule = new Schedule(
                Collections.singletonList(2021),
                Collections.singletonList(1),
                Collections.emptyList(),
                Arrays.asList(3,4,5),
                Collections.singletonList(1)
        );
        LocalDateTime time1 = LocalDateTime.of(2021, 1, 18,0, 0);
        LocalDateTime time2 = time1.plusDays(2);

        assertEquals(ReflectionTestUtils.invokeMethod(
                scheduleService,
                "checkDayAvailability",
                schedule,
                time1
        ), true);

        assertEquals(ReflectionTestUtils.invokeMethod(
                scheduleService,
                "checkDayAvailability",
                schedule,
                time2
        ), false);
    }

    @Test
    public void getSlotsForScheduleAndDay(){
        int delta = 120;
        Schedule schedule1 = new Schedule(60, 1320, 360, delta);
        Schedule schedule2 = new Schedule(0, 360, delta);
        LocalDateTime day1 = LocalDateTime.now().minusDays(1);
        LocalDateTime day2 = LocalDateTime.now();
        LocalDateTime start = Useful.getTodayWithTime(0,30);
        LocalDateTime end = Useful.getTodayWithTime(1,30);
        List<Slot> result1 = Arrays.asList(
                new Slot(Useful.getTodayWithTime(0,0), delta),
                new Slot(Useful.getTodayWithTime(1,0), delta)
        );
        List<Slot> result2 = Collections.singletonList(
                new Slot(Useful.getTodayWithTime(0, 0), Useful.getTodayWithTime(6, 0), delta)
        );

        assertEquals(ReflectionTestUtils.invokeMethod(
                scheduleService,
                "getSlotsForScheduleAndDay",
                schedule1,
                start,
                end,
                day1
        ), result1);

        assertEquals(ReflectionTestUtils.invokeMethod(
                scheduleService,
                "getSlotsForScheduleAndDay",
                schedule2,
                start,
                end,
                day2
        ), result2);
    }

    @Test
    public void getRealFreqStart(){
        LocalDateTime slotStart = LocalDateTime.now();
        LocalDateTime start = slotStart.plusMinutes(40);
        LocalDateTime result = slotStart.plusMinutes(30);
        int freq = 15;

        assertEquals(ReflectionTestUtils.invokeMethod(
                scheduleService,
                "getRealFreqStart",
                slotStart,
                start,
                freq), result);
    }
}