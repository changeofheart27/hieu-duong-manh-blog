package vn.com.hieuduongmanhblog.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.service.ImageStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private final Path uploadDir = Paths.get("uploads");

    @Override
    public String uploadImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new ResourceNotFoundException("Could not upload empty image");
        } else {
            try {
                // create 'uploads' directory if it doesn't exist
                if (!Files.exists(this.uploadDir)) {
                    Files.createDirectories(this.uploadDir);
                }

                // normalize the image name to avoid issues with path traversal
                String originalImageName = imageFile.getOriginalFilename();
                String sanitizedImageName = originalImageName.replaceAll("[^a-zA-Z0-9.-]", "_");
                Path targetPath = this.uploadDir.resolve(sanitizedImageName).normalize();

                // If the image already exists, generate a unique name using UUID
                if (Files.exists(targetPath)) {
                    String uniqueImageName = UUID.randomUUID() + "_" + sanitizedImageName;
                    targetPath = this.uploadDir.resolve(uniqueImageName).normalize();
                }

                // save the file to target path
                Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                return targetPath.getFileName().toString();
            } catch (IOException e) {
                throw new RuntimeException("Could not upload image: " + e.getMessage());
            }
        }
    }

    @Override
    public Resource loadImage(String imageName) {
        try {
            Path imagePath = this.uploadDir.resolve(imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not load the image");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAllImages() {
        try {
            return Files.walk(this.uploadDir, 1)
                    .filter(path -> !path.equals(this.uploadDir))  // exclude the root directory
                    .map(this.uploadDir::relativize);  // return relative paths
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public void deleteAllImages() {
        FileSystemUtils.deleteRecursively(this.uploadDir.toFile());
    }

    // Helper method to extract URL from Resource
    private String getImageUrl(Resource resource) throws IOException {
        URI uri = resource.getURI();
        URL url = uri.toURL();
        return url.toString();
    }

}
