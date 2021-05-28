package ua.project.forit.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.project.forit.data.entities.enums.WritingReaction;
import ua.project.forit.data.entities.interfaces.Writing;
import ua.project.forit.data.entities.models.Post;
import ua.project.forit.data.entities.models.PostComment;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.services.UserService;
import ua.project.forit.data.services.WritingReactionService;
import ua.project.forit.data.services.WritingService;
import ua.project.forit.exceptions.UserServiceException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/writings")
public class WritingController
{
    protected final UserService userService;
    protected final WritingService writingService;
    protected final WritingReactionService writingReactionService;

    protected final Logger logger = LogManager.getLogger(WritingController.class.getName());


    @Autowired
    public WritingController(UserService userService,
                             WritingService writingService,
                             WritingReactionService writingReactionService)
    {
        this.userService = userService;
        this.writingService = writingService;
        this.writingReactionService = writingReactionService;
    }

    @GetMapping(value = "/getCurrentUserWritings")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getCurrentUserWritings(Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());
            Map<String, List<Map<String, Object>>> writings = writingService.getUserWritings(user, user);
            return new ResponseEntity<>(writings, HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getUserWritings/{entityToken}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getUserWritingsByEntityToken(@PathVariable("entityToken") String entityToken,
                                                                                               Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());
            User usersWritings = userService.getUserByEntityToken(entityToken);
            Map<String, List<Map<String, Object>>> writings = writingService.getUserWritings(usersWritings, user);
            return new ResponseEntity<>(writings, HttpStatus.OK);
        }
        catch (Throwable ex)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/addCurrentUserPost")
    public ResponseEntity<Writing> addCurrentUserPost(@RequestBody Map<String, String> data,
                                                 Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());

            Writing post = writingService.createPost(data, user);

            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/deleteCurrentUserPost")
    public ResponseEntity<Void> deleteCurrentUserPost(@RequestBody String entityToken,
                                                      Authentication authentication)
    {
        writingService.deletePost(authentication.getName(), entityToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/setPostReaction")
    public ResponseEntity<Void> setPostReaction(@RequestBody Map<String, String> data,
                                                Authentication authentication)
    {
        try
        {
            Post post = writingService.getPostByEntityToken(data.get("entityToken"));
            User user = userService.getUserByEmail(authentication.getName());
            writingReactionService.setReactionOnWriting(post, user, WritingReaction.valueOf(data.get("emotion")));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Throwable ignore)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/addPostComment")
    public ResponseEntity<Map<String, Object>> addPostComment(@RequestBody Map<String, String> data,
                                                      Authentication authentication)
    {
        try
        {
            User user = userService.getUserByEmail(authentication.getName());
            Post post = writingService.getPostByEntityToken(data.get("entityToken"));

            PostComment comment = writingService.addPostComment(user, post, data.get("comment"));
            return new ResponseEntity<>(comment.getJSON(), HttpStatus.OK);
        }
        catch (Throwable ignore)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
