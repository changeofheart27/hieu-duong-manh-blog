package vn.com.hieuduongmanhblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.dto.UserRegistrationRequestDTO;
import vn.com.hieuduongmanhblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Tag(name = "User Controller", description = "Endpoints for User Operations")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get All Users Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Get All Users", description = "Get All Users With Pagination Support")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = "5", required = false) int pageSize
    ) {
        Page<UserDTO> users = this.userService.getAllUsers(pageNumber, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get All Users Successful", LocalDateTime.now(), users));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get User By ID Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Get User By ID", description = "Get User With Specific ID")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO> findUserById(@PathVariable int userId) {
        UserDTO givenUser = this.userService.findUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get User By ID Successful", LocalDateTime.now(), givenUser));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Get User By username Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Get User By Username", description = "Get User With Specific Username")
    @GetMapping("/username")
    public ResponseEntity<ResponseDTO> findUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO givenUser = this.userService.findUserByUsername(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Get User By username Successful", LocalDateTime.now(), givenUser));
    }

    @ApiResponse(
            responseCode = "201",
            description = "Create New User Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Create New User", description = "Create New User With Request Payload")
    @PostMapping
    public ResponseEntity<ResponseDTO> createNewUser(@Valid @RequestBody UserRegistrationRequestDTO userData) {
        UserDTO updatedUser = this.userService.createNewUser(userData);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), "Create New User Successful", LocalDateTime.now(), updatedUser));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Update User Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Update User", description = "Update User With Specific ID And Request Payload")
    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDTO> updateExistingUser(@PathVariable int userId, @Valid @RequestBody UserDTO userData) {
        UserDTO updatedUser = this.userService.updateUserById(userId, userData);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Update User Successful", LocalDateTime.now(), updatedUser));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Delete User Successful",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class)
            )
    )
    @Operation(summary = "Delete User", description = "Delete User With Specific ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> deleteUserById(@PathVariable int userId) {
        this.userService.deleteUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK.value(), "Delete User Successful", LocalDateTime.now()));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Update User Avatar Successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Update User Avatar Failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @Operation(summary = "Update User Avatar", description = "Update User Avatar With Specific ID and Image File")
    @PatchMapping("/{userId}/change-avatar")
    public ResponseEntity<ResponseDTO> changeAvatar(@PathVariable int userId, @RequestPart MultipartFile imageFile) {
        try {
            UserDTO userWithUpdatedAvatar = this.userService.changeAvatar(userId, imageFile);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK.value(), "Update User Avatar Successful", LocalDateTime.now(), userWithUpdatedAvatar));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Update User Avatar Failed: " + e.getMessage(), LocalDateTime.now()));
        }
    }
}
