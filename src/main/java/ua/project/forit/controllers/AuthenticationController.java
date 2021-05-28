package ua.project.forit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.project.forit.data.entities.models.Confirm;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.services.AuthenticationTokenService;
import ua.project.forit.data.services.ConfirmService;
import ua.project.forit.data.services.UserService;
import ua.project.forit.exceptions.ConfirmServiceException;
import ua.project.forit.exceptions.UserServiceException;
import ua.project.forit.security.services.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController
{
    private final ConfirmService confirmService;
    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public AuthenticationController(ConfirmService confirmService,
                                    AuthorizationService authorizationService,
                                    UserService userService,
                                    AuthenticationTokenService authenticationTokenService)
    {
        this.confirmService = confirmService;
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @GetMapping("/registration")
    public ResponseEntity<Void> registration()
    {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registered(@Validated @RequestBody Confirm confirm,
                                          HttpServletRequest request)
    {
        Map<String, String> map = new HashMap<>();
        try
        {
            map.put("hash", confirmService.saveUserRegistrationRequest(confirm, request.getRemoteAddr()));
        }
        catch (ConfirmServiceException ex)
        {
            map.put("error", ex.getMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/registration/confirm")
    public ResponseEntity<Map<String, String>> confirm(@RequestBody Map<String, String> map)
    {
        try
        {
            map.put(AuthenticationTokenService.TOKEN_NAME, confirmService.confirmUserRegistrationEmail(map.get("hash"), map.get("code")));
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch(ConfirmServiceException ex)
        {
            map.put("error", ex.getMessage());
            return new ResponseEntity<>(map, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login()
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> map,
                                                     HttpServletRequest request)
    {
        Map<String, Object> answer = new HashMap<>();
        try
        {
            User user = userService.getUserByEmailAndPassword(map.get("username"), map.get("password"));
            answer.put(AuthenticationTokenService.TOKEN_NAME, authenticationTokenService.generateToken(user, request.getRemoteAddr(), map.get("remember-me")).toString());
            return new ResponseEntity<>(answer, HttpStatus.OK);
        }
        catch (UserServiceException ex)
        {
            answer.put("error", ex.getMessage());
            return new ResponseEntity<>(answer, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request)
    {
        authorizationService.logout(request.getHeader(AuthenticationTokenService.TOKEN_NAME));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
