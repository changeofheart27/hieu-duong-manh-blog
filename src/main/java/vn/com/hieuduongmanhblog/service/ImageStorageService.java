package vn.com.hieuduongmanhblog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface ImageStorageService {
    String uploadImage(MultipartFile imageFile) throws IOException;

    List<String> getAllImageUrls() throws IOException;

    byte[] loadImage(String imageName) throws IOException;

    void deleteAllImages();
}
