package es.uji.ei1027.ovi.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {
    
    public void notifyUser(String targetUserDni, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("=================================================");
        System.out.println("🔔 [NOTIFICACIÓ EN TEMPS REAL] -> DNI: " + targetUserDni);
        System.out.println("   Hora: " + timestamp);
        System.out.println("   Missatge: " + message);
        System.out.println("=================================================");
    }
}
