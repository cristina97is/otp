package com.promoit.otp.notification;

import jakarta.mail.*;import jakarta.mail.internet.*;import org.slf4j.*;import org.springframework.stereotype.Service;import java.util.*;
@Service
public class EmailNotificationService implements NotificationSender{
 private static final Logger logger=LoggerFactory.getLogger(EmailNotificationService.class); private final String username,password,fromEmail; private final Session session;
 public EmailNotificationService(){Properties c=load();username=c.getProperty("email.username","");password=c.getProperty("email.password","");fromEmail=c.getProperty("email.from",username);session=Session.getInstance(c,new Authenticator(){protected PasswordAuthentication getPasswordAuthentication(){return new PasswordAuthentication(username,password);}});}
 private Properties load(){try{Properties p=new Properties();var in=getClass().getClassLoader().getResourceAsStream("email.properties");if(in!=null)p.load(in);return p;}catch(Exception e){throw new RuntimeException("Failed to load email config",e);}}
 public void sendCode(String to,String code){try{Message m=new MimeMessage(session);m.setFrom(new InternetAddress(fromEmail));m.setRecipient(Message.RecipientType.TO,new InternetAddress(to));m.setSubject("Your OTP Code");m.setText("Your verification code is: "+code);Transport.send(m);logger.info("Email OTP sent to {}",to);}catch(Exception e){logger.error("Email OTP failed",e);throw new RuntimeException("Failed to send email",e);}}
}
