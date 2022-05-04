package cgx.core.time;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeTool {

    private static final DateTimeFormatter COMMON_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime parseCommonTime(String timeStr) {
        return LocalDateTime.parse(timeStr, COMMON_PATTERN);
    }

    public String toCommonTimeStr(LocalDateTime time) {
        return COMMON_PATTERN.format(time);
    }
    
    public long curMilli() {
        return System.currentTimeMillis();
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDateTime milliToLocalDateTime(long milli) {
        return Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
