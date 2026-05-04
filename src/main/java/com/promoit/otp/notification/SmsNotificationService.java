package com.promoit.otp.notification;

import org.jsmpp.bean.*;import org.jsmpp.session.*;import org.slf4j.*;import org.springframework.stereotype.Service;import java.nio.charset.StandardCharsets;import java.util.*;
@Service
public class SmsNotificationService implements NotificationSender{
 private static final Logger logger=LoggerFactory.getLogger(SmsNotificationService.class); private final String host,systemId,password,systemType,sourceAddress; private final int port;
 public SmsNotificationService(){Properties p=load();host=p.getProperty("smpp.host","localhost");port=Integer.parseInt(p.getProperty("smpp.port","2775"));systemId=p.getProperty("smpp.system_id","smppclient1");password=p.getProperty("smpp.password","password");systemType=p.getProperty("smpp.system_type","OTP");sourceAddress=p.getProperty("smpp.source_addr","OTPService");}
 private Properties load(){try{Properties p=new Properties();var in=getClass().getClassLoader().getResourceAsStream("sms.properties");if(in!=null)p.load(in);return p;}catch(Exception e){throw new RuntimeException(e);}}
 public void sendCode(String dest,String code){SMPPSession s=new SMPPSession();try{s.connectAndBind(host,port,new BindParameter(BindType.BIND_TX,systemId,password,systemType,TypeOfNumber.UNKNOWN,NumberingPlanIndicator.UNKNOWN,sourceAddress));s.submitShortMessage(systemType,TypeOfNumber.UNKNOWN,NumberingPlanIndicator.UNKNOWN,sourceAddress,TypeOfNumber.UNKNOWN,NumberingPlanIndicator.UNKNOWN,dest,new ESMClass(),(byte)0,(byte)1,null,null,new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT),(byte)0,new GeneralDataCoding(Alphabet.ALPHA_DEFAULT),(byte)0,("Your code: "+code).getBytes(StandardCharsets.UTF_8));logger.info("SMS OTP sent to {}",dest);}catch(Exception e){logger.error("SMS OTP failed",e);throw new RuntimeException("Failed to send SMS",e);}finally{try{s.unbindAndClose();}catch(Exception ignored){}}}
}
