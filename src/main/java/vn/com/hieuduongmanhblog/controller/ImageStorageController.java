package vn.com.hieuduongmanhblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Image Storage Controller", description = "Endpoints for Image Storage Operations")
@RestController
@RequestMapping("/api/v1/images")
public class ImageStorageController {
    private final ImageStorageService imageService;

    @Autowired
    public ImageStorageController(ImageStorageService imageService) {
        this.imageService = imageService;
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Upload Image Successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Upload Image Failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @Operation(summary = "Upload Image", description = "Upload Image To The Application")
    @PostMapping("/upload")
    public ResponseEntity<ResponseDTO> uploadImage(@RequestPart MultipartFile imageFile) {
        try {
            String uploadedImageName = this.imageService.uploadImage(imageFile);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK.value(), "Upload Image Successful: " + uploadedImageName, LocalDateTime.now()));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Upload Image Failed: " + e.getMessage(), LocalDateTime.now()));
        }
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get All Image URLs Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Get All Image URLs", description = "Get All Image URLs")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllImageUrls() {
        try {
        List<String> imageUrls = this.imageService.getAllImageUrls();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Image URLs Successful", LocalDateTime.now(), imageUrls));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Get All Image URLs Failed: " + e.getMessage(), LocalDateTime.now()));
        }
    }

    @ApiResponse(
            responseCode = "200",
            description = "Display Image Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Display Image", description = "Display Image With Specific Image Name")
    @GetMapping("/{imageName:.+}")
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
