package com.dpdc.realestate.service;

import java.io.File;
import java.io.IOException;

public interface YoutubeService {
    String uploadFile(File file, String title, String description) throws IOException;
}
