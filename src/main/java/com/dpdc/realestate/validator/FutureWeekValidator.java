package com.dpdc.realestate.validator;

import com.dpdc.realestate.validator.anotation.FutureWeek;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class FutureWeekValidator implements ConstraintValidator<FutureWeek, Instant> {

    @Override
    public void initialize(FutureWeek constraintAnnotation) {
    }

    @Override
    public boolean isValid(Instant appointmentDate, ConstraintValidatorContext context) {
        try {
            // Đảm bảo rằng appointmentDate đang ở múi giờ UTC
            Instant utcAppointmentDate = appointmentDate.atZone(ZoneId.of("UTC")).toInstant();

            // Lấy ngày hiện tại với múi giờ UTC (GMT)
            Instant now = Instant.now();

            // Tìm thời điểm bắt đầu của tuần sau (ngày thứ Hai tiếp theo)
            Instant nextMonday = now.atZone(ZoneId.of("UTC"))
                    .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                    .toInstant();

            // Tìm thời điểm kết thúc của tuần sau (ngày Chủ Nhật tiếp theo)
            Instant nextSunday = nextMonday.plus(7, ChronoUnit.DAYS);

            // So sánh utcAppointmentDate với nextMonday và nextSunday
            boolean start = utcAppointmentDate.isAfter(nextMonday);
            boolean end = utcAppointmentDate.isBefore(nextSunday);
            return start && end;
        } catch (Exception ex) {
            // Ghi log cho ngoại lệ
            ex.printStackTrace();
            // Bạn có thể thay đổi xử lý ngoại lệ tùy theo yêu cầu của bạn, ví dụ, trả về false nếu có lỗi.
            return false;
        }
    }
}