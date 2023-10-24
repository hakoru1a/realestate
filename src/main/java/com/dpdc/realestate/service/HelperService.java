package com.dpdc.realestate.service;

import com.dpdc.realestate.dto.request.Mail;
import com.dpdc.realestate.models.entity.Property;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HelperService {

//    Cloudinary Service
    String uploadImage(MultipartFile file);

    boolean deleteImage(String publicId);

    void deleteMedia(Integer id);

    void deleteDocument(Integer id);


    void uploadMedia(List<MultipartFile> files, Property property) throws IOException;

    void uploadImages(MultipartFile file, Property property) throws IOException;

    void uploadDocument(MultipartFile file, Property property) throws IOException;

    Map getCloudinaryUsage();

    boolean checkPublicIdExists(String publicId);

//    Message service

    void sendMessage(String toPhone, String message);

//    Mail service


    void sendMail(Mail mail, Map<String, String> model, String pathMail);
}
