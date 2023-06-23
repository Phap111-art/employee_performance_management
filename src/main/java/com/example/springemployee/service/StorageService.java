package com.example.springemployee.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    String storeAdd(MultipartFile file);

    MultipartFile convertStringToFile(String nameFile);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    boolean isImageOrPdfFile(MultipartFile file);
}
