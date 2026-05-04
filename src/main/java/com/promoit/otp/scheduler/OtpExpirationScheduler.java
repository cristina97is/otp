package com.promoit.otp.scheduler;
import com.promoit.otp.service.OtpService;import org.slf4j.*;import org.springframework.scheduling.annotation.Scheduled;import org.springframework.stereotype.Component;
@Component
public class OtpExpirationScheduler{private static final Logger log=LoggerFactory.getLogger(OtpExpirationScheduler.class);private final OtpService service;public OtpExpirationScheduler(OtpService service){this.service=service;}@Scheduled(fixedDelayString="${app.otp.expire-scan-ms}")public void expire(){int n=service.expireOldCodes();if(n>0)log.info("Expired OTP codes: {}",n);}}
