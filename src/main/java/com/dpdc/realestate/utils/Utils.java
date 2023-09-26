package com.dpdc.realestate.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {
    private final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
    public DateFormat getDateFormatter(){
        return formatter;
    }


    public static boolean isSameDay(Instant time1, Instant time2) {
        // Kiểm tra xem hai thời điểm có cùng thứ hay không
        LocalDate date1 = time1.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = time2.atZone(ZoneId.systemDefault()).toLocalDate();
        return date1.equals(date2);
    }

    public static Integer calculateAppointmentShift(Instant appointmentDate) {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedDateTime = appointmentDate.atZone(zoneId);

        // Định dạng ZonedDateTime thành chuỗi dễ đọc
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedTime = zonedDateTime.format(formatter);

        System.out.println("Thời gian hiện tại: " + formattedTime);
        LocalTime startTime = appointmentDate.atZone(ZoneId.systemDefault()).toLocalTime();

        // Thời gian bắt đầu của các ca làm việc
        LocalTime ca1StartTime = LocalTime.of(8, 0);
        LocalTime ca1EndTime = LocalTime.of(10, 0);

        LocalTime ca2StartTime = LocalTime.of(13, 0);
        LocalTime ca2EndTime = LocalTime.of(15, 0);

        LocalTime ca3StartTime = LocalTime.of(16, 0);
        LocalTime ca3EndTime = LocalTime.of(18, 0);

        if (startTime.isAfter(ca1StartTime) && startTime.isBefore(ca1EndTime)) {
            return 1;
        } else if (startTime.isAfter(ca2StartTime) && startTime.isBefore(ca2EndTime)) {
            return 2;
        } else if (startTime.isAfter(ca3StartTime) && startTime.isBefore(ca3EndTime)) {
            return 3;
        } else {
            return -1;
        }
    }
}
