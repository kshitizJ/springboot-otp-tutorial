package com.springboototp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import com.springboototp.emailTemplate.EmailTemplate;
import com.springboototp.service.EmailService;
import com.springboototp.service.OTPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OTPController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/generateOtp")
    public String generateOTP() throws MessagingException {

        // getting the authentication from SecurityContextHolder class
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // getting the name of the authenticated user
        String username = auth.getName();

        // generating a random OTP
        int otp = otpService.generatedOTP(username);

        // Generate the template to send the OTP
        EmailTemplate template = new EmailTemplate("SendOtp.html");

        // Making a map to pass the username and otp in the email template
        Map<String, String> replacements = new HashMap<>();
        replacements.put("user", username); // adding username
        replacements.put("otp", String.valueOf(otp)); // adding the generated OTP

        // getting the parsed message form the EmailTemplate's instance
        String message = template.getTemplate(replacements);

        // sending the OTP via email
        emailService.sendOTPMessage("coolkshitiz786@gmail.com", "OTP - Springboot", message);

        // return the otpPage
        return "otppage";
    }

    @RequestMapping(value = "/validateOtp", method = RequestMethod.GET)
    public @ResponseBody String validateOTP(@RequestParam("otpNum") int otpNum) {

        // success message
        final String SUCCESS = "Entered OTP is valid...";

        // error message
        final String FAIL = "Entered OTP is NOT valid. Please try again...";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // validate the OTP
        if (otpNum > 0) {

            // getting the OTP from the server
            int serverOTP = otpService.getOTP(username);

            if (serverOTP > 0) {

                if (otpNum == serverOTP) {

                    // if both the OTP matches then we clear the OTP from the cache
                    otpService.clearOTP(username);

                    // return the success message
                    return SUCCESS;

                } else {

                    // return fail message because both the OTP doesn't matches
                    return FAIL;

                }

            } else {

                // return fail message because serverOTP is 0 or less than 0
                return FAIL;

            }
        } else {

            // return fail message because incoming otpNum is 0 or less than 0
            return FAIL;

        }
    }

}
