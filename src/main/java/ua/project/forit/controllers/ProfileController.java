package ua.project.forit.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.project.forit.data.entities.models.AuthenticationToken;
import ua.project.forit.data.entities.models.Post;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.services.UserService;
import ua.project.forit.data.services.WritingService;
import ua.project.forit.exceptions.UserServiceException;
import ua.project.forit.services.MediaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile")
public class ProfileController
{
    protected final UserService userService;
    protected final WritingService writingService;
    protected final MediaService mediaService;

    protected final Logger logger = LogManager.getLogger(ProfileController.class.getName());

    @Autowired
    public ProfileController(UserService userService,
                             WritingService writingService,
                             MediaService mediaService)
    {
        this.userService = userService;
        this.writingService = writingService;
        this.mediaService = mediaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());
            Map<String, Object> data = user.getJSON();
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/getUserByEntityToken/{entityToken}")
    public ResponseEntity<Map<String, Object>> getUserByEntityToken(@PathVariable("entityToken") String entityToken)
    {
        try
        {
            User user = userService.getUserByEntityToken(entityToken);
            Map<String, Object> data = user.getJSON();
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/follow")
    public ResponseEntity<Void> follow(@RequestBody Map<String, String> data,
                                       Authentication authentication)
    {
        try
        {
            User userForFollowing = userService.getUserByEntityToken(data.get("entityToken"));
            User user = userService.getUserByEmail(authentication.getName());
            userService.follow(userForFollowing, user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/addPost")
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Map<String, String> data,
                                                       Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());

            Post post = writingService.createPost(data, user);
            return new ResponseEntity<>(post.getJSON(), HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/getAuthenticationTokens")
    public ResponseEntity<List<Map<String, Object>>> getAuthenticationTokens(Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());

            List<Map<String, Object>> authenticationTokens = user.getAuthenticationTokens().stream()
                    .map(AuthenticationToken::getJSON)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(authenticationTokens, HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/setUserImg", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> setUserImg(@RequestParam("file") MultipartFile file,
                                           Authentication authentication)
    {
        Map<String, String> data = new HashMap<>();
        try
        {
            User user = userService.getUserByEmail(authentication.getName());
            data.put("imgId", mediaService.setUserImg(file, user));
            userService.saveAndFlush(user);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        catch (Throwable ex)
        {
            logger.error(ex.getMessage());
            data.put("error", ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.CONFLICT);
        }
    }
}
