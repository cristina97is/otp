package com.promoit.otp.notification;

import org.slf4j.*;import org.springframework.stereotype.Service;import java.nio.file.*;import java.time.LocalDateTime;
@Service
public class FileNotificationService implements NotificationSender {
 private static final Logger logger=LoggerFactory.getLogger(FileNotificationService.class);
 public void sendCode(String destination,String code){try{Files.writeString(Path.of("otp-codes.txt"),LocalDateTime.now()+" | "+destination+" | code="+code+System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);logger.info("OTP saved to file for {}",destination);}catch(Exception e){throw new RuntimeException("Failed to save OTP to file",e);}}
}
