package vn.com.hieuduongmanhblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.service.ImageStorageService;

import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageStorageController {
    private final ImageStorageService imageService;

    @Autowired
    public ImageStorageController(ImageStorageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseDTO uploadImage(@RequestPart MultipartFile imageFile) {
        try {
            String uploadedImageName = this.imageService.uploadImage(imageFile);
            return new ResponseDTO(HttpStatus.OK, "Upload Image Successful: " + uploadedImageName, LocalDateTime.now());
        } catch (IOException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "Upload Image Failed: " + e.getMessage(), LocalDateTime.now());
        }
    }

    @GetMapping("/")
    public ResponseDTO getAllImageUrls() {
        try {
        List<String> imageUrls = this.imageService.getAllImageUrls();
        return new ResponseDTO(HttpStatus.OK, "Get All Image URLs Successful", LocalDateTime.now(), imageUrls);
        } catch (IOException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "Get All Image URLs Failed: " + e.getMessage(), LocalDateTime.now());
        }
    }

    @GetMapping(value = "/{imageName:.+}")
    public ResponseEntity<byte[]> loadImage(@PathVariable String imageName) {
        try {
            byte[] image = this.imageService.loadImage(imageName);

            String mimeType = URLConnection.guessContentTypeFromName(imageName);
            if (mimeType == null) {
                // default binary stream
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(image);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
