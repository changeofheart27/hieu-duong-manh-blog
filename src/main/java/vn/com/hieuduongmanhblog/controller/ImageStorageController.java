package vn.com.hieuduongmanhblog.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.service.ImageStorageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/images")
public class ImageStorageController {
    private final ImageStorageService imageService;

    @Autowired
    public ImageStorageController(ImageStorageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseDTO uploadImage(@RequestParam MultipartFile imageFile) {
        String uploadedImageName = this.imageService.uploadImage(imageFile);
        return new ResponseDTO(HttpStatus.OK, "Image uploaded successful: " + uploadedImageName, LocalDateTime.now());
    }

    @GetMapping(value = "/{imageName:.+}")
    public ResponseEntity<Resource> loadImage(@PathVariable String imageName) {
        Resource image = this.imageService.loadImage(imageName);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> loadAllImages() {
        List<String> imageNames = this.imageService.loadAllImages()
                .map(path -> MvcUriComponentsBuilder.fromMethodName(
                        ImageStorageController.class,
                        "loadImage",
                        path.getFileName().toString())
                        .build().toString())
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(imageNames);
    }
}
