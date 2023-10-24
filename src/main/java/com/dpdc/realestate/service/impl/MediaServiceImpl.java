package com.dpdc.realestate.service.impl;


import com.dpdc.realestate.models.entity.Media;
import com.dpdc.realestate.repository.MediaRepository;
import com.dpdc.realestate.service.MediaService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public void saveAll(Set<Media> medias) {
        mediaRepository.saveAll(medias);
    }
}
