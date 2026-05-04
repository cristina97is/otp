package com.promoit.otp.service;

import com.promoit.otp.dao.*;
import com.promoit.otp.model.*;
import com.promoit.otp.notification.*;
import org.slf4j.*;import org.springframework.stereotype.Service;
import java.security.SecureRandom;import java.time.LocalDateTime;

@Service
public class OtpService{
 private static final Logger log=LoggerFactory.getLogger(OtpService.class); private final OtpDao otp; private final OtpConfigDao cfg; private final EmailNotificationService email; private final SmsNotificationService sms; private final TelegramNotificationService telegram; private final FileNotificationService file; private final SecureRandom rnd=new SecureRandom();
 public OtpService(OtpDao otp,OtpConfigDao cfg,EmailNotificationService email,SmsNotificationService sms,TelegramNotificationService telegram,FileNotificationService file){this.otp=otp;this.cfg=cfg;this.email=email;this.sms=sms;this.telegram=telegram;this.file=file;}
 public String generate(AuthUser u,String operationId,String emailTo,String phone,String tgDest,boolean sendEmail,boolean sendSms,boolean sendTg,boolean saveFile){OtpConfig c=cfg.get();String code=code(c.codeLength());otp.create(u.id(),operationId,code,LocalDateTime.now().plusSeconds(c.ttlSeconds()));if(sendEmail)email.sendCode(emailTo,code);if(sendSms)sms.sendCode(phone,code);if(sendTg)telegram.sendCode(tgDest==null?u.login():tgDest,code);if(saveFile)file.sendCode(operationId,code);log.info("OTP generated for user={}, operation={}, channels email={}, sms={}, telegram={}, file={}",u.login(),operationId,sendEmail,sendSms,sendTg,saveFile);return code;}
 public boolean validate(AuthUser u,String operationId,String code){var id=otp.findActiveId(u.id(),operationId,code);if(id.isPresent()){otp.markUsed(id.get());log.info("OTP validated for user={}, operation={}",u.login(),operationId);return true;}log.warn("OTP validation failed for user={}, operation={}",u.login(),operationId);return false;}
 public int expireOldCodes(){return otp.expireOldCodes();}
 private String code(int len){StringBuilder sb=new StringBuilder();for(int i=0;i<len;i++)sb.append(rnd.nextInt(10));return sb.toString();}
}
