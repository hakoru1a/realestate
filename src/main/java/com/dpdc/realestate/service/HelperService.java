package com.dpdc.realestate.service;

import com.dpdc.realestate.dto.request.Mail;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface HelperService {

//    Cloudinary Service
    String uploadImage(MultipartFile file);

    boolean deleteImage(String publicId);

    Map getCloudinaryUsage();

    boolean checkPublicIdExists(String publicId);

//    Message service

    boolean sendMessage(String toPhone, String message);

//    Mail service


    boolean sendMail(Mail mail, Map<String, String> model);
}
