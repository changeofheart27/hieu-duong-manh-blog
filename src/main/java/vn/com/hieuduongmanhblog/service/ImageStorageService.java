package vn.com.hieuduongmanhblog.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface ImageStorageService {
    String uploadImage(MultipartFile imageFile);

    Resource loadImage(String imageName);

    Stream<Path> loadAllImages();

    void deleteAllImages();
}
