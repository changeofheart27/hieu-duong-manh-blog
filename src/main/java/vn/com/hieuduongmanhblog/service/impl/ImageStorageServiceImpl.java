package vn.com.hieuduongmanhblog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.service.ImageStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private final Path uploadDir = Paths.get("uploads");

    @Value("${project.image.url}")
    private String imageUrlBase;

    public ImageStorageServiceImpl() throws IOException {
        // create 'uploads' directory if it doesn't exist
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    @Override
    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new ResourceNotFoundException("Could not upload empty image");
        }
        String originalImageName = imageFile.getOriginalFilename();
        String imageExtension = getImageExtension(originalImageName);
        if (!isValidImageFile(imageExtension)) {
            throw new IOException("Invalid image file type");
        }
        // normalize the image name to avoid issues with path traversal
        String sanitizedImageName = originalImageName.replaceAll("[^a-zA-Z0-9.-]", "_");
        Path targetPath = this.uploadDir.resolve(sanitizedImageName).normalize();
        // Check if file already exists
        if (Files.exists(targetPath)) {
            Files.delete(targetPath);
        }

        // save the file to target path
        Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return sanitizedImageName;
    }

    @Override
    public List<String> getAllImageUrls() throws IOException {
        try (Stream<Path> paths = Files.walk(this.uploadDir)) {
            return paths
                    .filter(Files::isRegularFile) // Ensure it's a file, not a directory
                    .map(Path::getFileName) // Get the file name
                    .map(Path::toString)
                    .map(imageName -> this.imageUrlBase + imageName) // Prepend the base URL to the file name to form the full URL
                    .collect(Collectors.toList());
        }
    }

    @Override
    public byte[] loadImage(String imageName) throws IOException {
        Path imagePath = this.uploadDir.resolve(imageName);
        return Files.readAllBytes(imagePath);
    }

    @Override
    public void deleteAllImages() {
        FileSystemUtils.deleteRecursively(this.uploadDir.toFile());
    }

    private String getImageExtension(String imageName) {
        return imageName.substring(imageName.lastIndexOf(".") + 1);
    }

    private boolean isValidImageFile(String extension) {
        return "jpg".equalsIgnoreCase(extension)
                || "jpeg".equalsIgnoreCase(extension)
                || "png".equalsIgnoreCase(extension);
    }
}
