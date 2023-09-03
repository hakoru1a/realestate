package com.dpdc.realestate.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dpdc.realestate.dto.request.Mail;
import com.dpdc.realestate.exception.MessageSendingException;
import com.dpdc.realestate.service.HelperService;
import com.twilio.type.PhoneNumber;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.twilio.rest.api.v2010.account.Message;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class HelperServiceImpl implements HelperService {

    @Autowired
    private Environment env;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration config;
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = this.cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            return (String) uploadResult.get("secure_url");
        } catch (IOException ex) {
            Logger.getLogger(HelperServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean deleteImage(String publicId) {
        try {
            this.cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(HelperServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Map getCloudinaryUsage() {
        try {
            return cloudinary.api().usage(ObjectUtils.emptyMap());
        } catch (Exception ex) {
            Logger.getLogger(HelperServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    @Override
    public boolean checkPublicIdExists(String publicId) {
        try {
            // Make an API call to retrieve information about the resource
            Map result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());

            // If the result is not empty, it means the resource exists
            return !result.isEmpty();
        } catch (Exception ex) {
            Logger.getLogger(HelperServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    @Async
    public void sendMessage(String toPhone, String content) {
        try {
            String fromPhone = env.getProperty("twilio.phone_number");
            Message message = Message.creator(
                    new PhoneNumber(toPhone),
                    new PhoneNumber(fromPhone),
                    content).create();
            System.out.println("SMS sent with SID: " + message.getSid());
        } catch (Exception ex) {
            System.out.println("Error sending SMS: " + ex.getMessage());
            throw new MessageSendingException("Failed to send the message");
        }
    }

    @Override
    @Async
    public void sendMail(Mail mail, Map<String, String> model){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            helper.addAttachment("logo.png", new ClassPathResource("logo.png"));

            Template t = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(mail.getToMail());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(env.getProperty("spring.mail.username"));
            javaMailSender.send(message);

        } catch (MessagingException | IOException | TemplateException ex) {
            System.out.println("Error sending Mail: " + ex.getMessage());
            throw new MessageSendingException("Failed to send the message");
        }
    }


}
