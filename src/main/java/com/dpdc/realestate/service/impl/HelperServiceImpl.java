package com.dpdc.realestate.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dpdc.realestate.dto.request.Mail;
import com.dpdc.realestate.exception.MessageSendingException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Document;
import com.dpdc.realestate.models.entity.Media;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.DocumentRepository;
import com.dpdc.realestate.repository.MediaRepository;
import com.dpdc.realestate.service.HelperService;
import com.dpdc.realestate.service.YoutubeService;
import com.dpdc.realestate.utils.Utils;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private YoutubeService youtubeService;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("resource_type", "auto");
            options.put("folder", env.getProperty("cloudinary.folder_property"));
            Map uploadResult = this.cloudinary.uploader().upload(file.getBytes(), options);
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
    public void deleteMedia(Integer id) {
        EntityCheckHandler.checkEntityExistById(mediaRepository, id);
        mediaRepository.deleteById(id);
    }

    @Override
    public void deleteDocument(Integer id) {
        EntityCheckHandler.checkEntityExistById(documentRepository, id);
        documentRepository.deleteById(id);
    }

    @Override
    public void uploadMedia(List<MultipartFile> files, Property property) throws IOException {
        List<Media> medias = new ArrayList<>();
        for (MultipartFile file: files) {
            Media media = new Media();
            if (utils.isVideoFile(file)){

                media.setMediaType("VIDEO");
                media.setUrl(youtubeService.uploadFile(utils.multipartToFile(file),
                        Instant.now().toString(), "Đây là description" ));
            }
            else {

                media.setMediaType("IMAGE");
                media.setUrl(uploadImage(file));
            }
            media.setMediaName(file.getName());
            media.setProperty(property);
            medias.add(media);
        }
        mediaRepository.saveAll(medias);
    }

    @Override
    public void uploadImages(MultipartFile file, Property property) throws IOException {
        Media media = new Media();
        if (utils.isVideoFile(file)){
            if (mediaRepository.countMediaByPropertyIdAndMediaType(property.getId(), "%video%") >= 2) {
                throw new RejectException("Quá số lượng video được upload");
            }
            File f = utils.multipartToFile(file);
            media.setUrl(youtubeService.uploadFile(f, Instant.now().toString()+property.getId(),
                    "Thuộc quyền sở hữu của cty abc"));
        }
        else {
            if (mediaRepository.countMediaByPropertyIdAndMediaType(property.getId(), "%image%") >= 10) {
                throw new RejectException("Quá số lượng hình ảnh upload");
            }
            media.setUrl(uploadImage(file));
        }
        media.setMediaName(file.getName());
        media.setMediaType(file.getContentType());
        media.setProperty(property);
        mediaRepository.save(media);
    }

    @Override
    public void uploadDocument(MultipartFile file, Property property)  {
        if (documentRepository.countByPropertyId(property.getId()) == 2){
            throw new RejectException("Quá tài liệu được đăng");
        }
        String url = uploadImage(file);
        Document document = new Document();
        document.setDocumentName(file.getName());
        document.setUrl(url);
        document.setProperty(property);
        documentRepository.save(document);
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
    public void sendMail(Mail mail, Map<String, String> model, String pathMail){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate(pathMail);
            model.put("logo", env.getProperty("app.logo"));
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(mail.getToMail());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
            javaMailSender.send(message);

        } catch (MessagingException | IOException | TemplateException ex) {
            System.out.println("Error sending Mail: " + ex.getMessage());
            throw new MessageSendingException("Failed to send the message");
        }
    }


}
