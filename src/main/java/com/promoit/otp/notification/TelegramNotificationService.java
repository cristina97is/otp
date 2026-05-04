package com.promoit.otp.notification;

import org.slf4j.*;import org.springframework.stereotype.Service;import java.net.*;import java.net.http.*;import java.nio.charset.StandardCharsets;import java.util.*;
@Service
public class TelegramNotificationService implements NotificationSender{
 private static final Logger logger=LoggerFactory.getLogger(TelegramNotificationService.class); private final String token,chatId,apiUrl;
 public TelegramNotificationService(){Properties p=load();token=p.getProperty("telegram.bot.token","");chatId=p.getProperty("telegram.chat.id","");apiUrl=p.getProperty("telegram.api.url","https://api.telegram.org/bot%s/sendMessage");}
 private Properties load(){try{Properties p=new Properties();var in=getClass().getClassLoader().getResourceAsStream("telegram.properties");if(in!=null)p.load(in);return p;}catch(Exception e){throw new RuntimeException(e);}}
 public void sendCode(String destination,String code){try{String text=URLEncoder.encode(destination+", your confirmation code is: "+code, StandardCharsets.UTF_8);String url=String.format(apiUrl,token)+"?chat_id="+chatId+"&text="+text;HttpResponse<String> r=HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(URI.create(url)).GET().build(),HttpResponse.BodyHandlers.ofString());if(r.statusCode()!=200)throw new RuntimeException(r.body());logger.info("Telegram OTP sent");}catch(Exception e){logger.error("Telegram OTP failed",e);throw new RuntimeException("Failed to send Telegram",e);}}
}
