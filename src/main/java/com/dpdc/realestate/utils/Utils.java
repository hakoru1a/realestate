package com.dpdc.realestate.utils;

import com.dpdc.realestate.exception.RejectException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileOutputStream;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
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
    public static Instant parseStringToInstant(Long dateTimeLong) {
        Instant instant = Instant.ofEpochMilli(dateTimeLong);

        // Đặt múi giờ cho việc chuyển đổi
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        // Chuyển đổi Instant thành ZonedDateTime trong múi giờ đã chỉ định
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        // Trả về ZonedDateTime hoặc Instant tùy thuộc vào nhu cầu của bạn
        return zonedDateTime.toInstant();
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

    public boolean isNotEmptyFile(MultipartFile file) {
        return file != null && !file.isEmpty() && file.getSize() > 0;
    }

    public boolean isVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            fileName = fileName.toLowerCase();
            return fileName.endsWith(".mp4") || fileName.endsWith(".avi")
                    || fileName.endsWith(".mov") || fileName.endsWith("webm")
                    || fileName.endsWith(".mkv") || fileName.endsWith(".mk3d")
                    || fileName.endsWith(".qt");
        }

        return false;
    }

    public File multipartToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
            return file;
        } catch (Exception err) {
            System.out.println("Video error");
        }
        return null;
    }


    public static void isValidDateNextWeek(Instant appointmentDate){
        // Đảm bảo rằng appointmentDate đang ở múi giờ UTC
        Instant utcAppointmentDate = appointmentDate.atZone(ZoneId.of("Etc/GMT+7")).toInstant();
        // Lấy ngày hiện tại với múi giờ UTC (GMT)
        Instant now = Instant.now();

        // Tìm thời điểm bắt đầu của tuần sau (ngày thứ Hai tiếp theo)
        Instant nextMonday = now.atZone(ZoneId.of("Etc/GMT+7"))
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .toInstant();

        // Tìm thời điểm kết thúc của tuần sau (ngày Chủ Nhật tiếp theo)
        Instant nextSunday = nextMonday.plus(7, ChronoUnit.DAYS);

        // So sánh utcAppointmentDate với nextMonday và nextSunday
        boolean start = utcAppointmentDate.isAfter(nextMonday);
        boolean end = utcAppointmentDate.isBefore(nextSunday);
        boolean isValid = start && end;
        if(!isValid){
            throw new RejectException("Lịch hẹn phải là tuần sau");
        }
    }
}
