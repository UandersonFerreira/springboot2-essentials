package estudo.java.springboot2.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component //javaBean
public class DateUtil {
    public String formatLocalDateTimeToDateDabaseStyle(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd MM:mm:ss").format(localDateTime);
    }
}
