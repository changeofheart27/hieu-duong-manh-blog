package vn.com.hieuduongmanhblog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;
import vn.com.hieuduongmanhblog.dto.UserDTO;
import vn.com.hieuduongmanhblog.dto.mapper.UserMapper;
import vn.com.hieuduongmanhblog.entity.Post;
import vn.com.hieuduongmanhblog.entity.User;
import vn.com.hieuduongmanhblog.exception.ResourceNotFoundException;
import vn.com.hieuduongmanhblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hieuduongmanhblog.service.ImageStorageService;
import vn.com.hieuduongmanhblog.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final ImageStorageService imageService;

    @Value("${project.image.url}")
    private String avatarUrlBase;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ImageStorageService imageStorageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.imageService = imageStorageService;
    }

    @Override
    public Page<UserDTO> getAllUsers(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<User> usersPage =  this.userRepository.findAll(pageRequest);
        List<UserDTO> userDTOList = usersPage
                .getContent()
                .stream()
                .map(this.userMapper::toUserDTO)
                .toList();

        return new PageImpl<>(userDTOList, usersPage.getPageable(), usersPage.getTotalElements());
    }

    @Override
    public UserDTO findUserById(int userId) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            return this.userMapper.toUserDTO(foundUser);
        } else throw new ResourceNotFoundException("Could not find User with id - " + userId);
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            return this.userMapper.toUserDTO(foundUser);
        } else throw new ResourceNotFoundException("Could not find User with username - " + username);
    }

    @Override
    @Transactional
    public UserDTO updateUserById(int userId, UserDTO newUserData) {
        User userToUpdate = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find User with id - " + userId));
        if (!checkCurrentLoggedInUser(userToUpdate.getUsername())) {
            throw new RuntimeException("Unable to update User with username - " + userToUpdate.getUsername());
        }

        userToUpdate.setUpdatedAt(LocalDateTime.now());

        User updatedUser = this.userRepository.save(userToUpdate);
        return this.userMapper.toUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(int userId) {
        User userToDelete = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find User with id - " + userId));
        if (!checkCurrentLoggedInUser(userToDelete.getUsername())) {
            throw new RuntimeException("Unable to delete User with username - " + userToDelete.getUsername());
        }

        // remove associated references to current user
        List<Post> postsByUser = userToDelete.getPosts();
        if (!postsByUser.isEmpty()) {
            postsByUser.forEach(post -> post.setUser(null));
        }

        this.userRepository.delete(userToDelete);
    }

    @Override
    @Transactional
    public UserDTO changeAvatar(int userId, MultipartFile multipartFile) throws IOException {
        User userToUpdate = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find User with id - " + userId));
        if (!checkCurrentLoggedInUser(userToUpdate.getUsername())) {
            throw new RuntimeException("Unable to change avatar with username - " + userToUpdate.getUsername());
        }
        // upload image
        String avatarName = this.imageService.uploadImage(multipartFile);
        String avatarUrl = this.avatarUrlBase + avatarName;

        // update user with new avatarName
        userToUpdate.setAvatar(avatarName);
        userToUpdate.setUpdatedAt(LocalDateTime.now());
        User updatedUser = this.userRepository.save(userToUpdate);

        UserDTO userDTO = this.userMapper.toUserDTO(updatedUser);
        userDTO.setAvatarUrl(avatarUrl);

        return userDTO;
    }

    /**
     * check if current logged-in user is the same as user to perform update/delete operations
     * if user role is ROLE_ADMIN then bypass the check
     *
     * @param username
     * @return Boolean
     */
    private Boolean checkCurrentLoggedInUser(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUserInfo = (UserDetails) auth.getPrincipal();

        String currentUserRole = currentUserInfo.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if (!currentUserRole.contains("ADMIN")) {
            return currentUserInfo.getUsername().equals(username);
        }

        return true;
    }
}
