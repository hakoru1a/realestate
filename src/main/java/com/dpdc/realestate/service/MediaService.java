package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Media;

import java.util.Set;

public interface MediaService {

    void saveAll(Set<Media> medias);
}
