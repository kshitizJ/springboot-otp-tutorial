package com.springboototp.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOTPMessage(String to, String subject, String message) throws MessagingException {

        // creating a mimemessage
        MimeMessage msg = javaMailSender.createMimeMessage();

        // creating a helper for the msg instance
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        // setting the receivers mail id
        helper.setTo(to);

        // setting the subject of the mail
        helper.setSubject(subject);

        // setting the text of the mail
        helper.setText(message, true);

        // sending the mail using javaMailSender instance
        javaMailSender.send(msg);
    }

}
