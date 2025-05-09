package id.my.hendisantika.rbacjwt.controller;

import id.my.hendisantika.rbacjwt.config.TokenProvider;
import id.my.hendisantika.rbacjwt.model.AuthToken;
import id.my.hendisantika.rbacjwt.model.LoginUser;
import id.my.hendisantika.rbacjwt.model.User;
import id.my.hendisantika.rbacjwt.model.UserDto;
import id.my.hendisantika.rbacjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-rbac-jwt
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 19/04/25
 * Time: 14.27
 * To change this template use File | Settings | File Templates.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider jwtTokenUtil;

    private final UserService userService;

    /**
     * Generates a token for the given user credentials.
     *
     * @param loginUser The user's login credentials.
     * @return A response entity containing the generated token.
     * @throws AuthenticationException if authentication fails.
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

    /**
     * Saves a new user.
     *
     * @param user The user to be saved.
     * @return The saved user.
     */
    @PostMapping(value = "/register")
    public User saveUser(@RequestBody UserDto user) {
        return userService.save(user);
    }

    /**
     * Returns a message that can only be accessed by users with the 'ADMIN' role.
     *
     * @return A message that can only be accessed by admins.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/adminping")
    public String adminPing() {
        return "Only Admins Can Read This";
    }

    /**
     * Returns a message that can be accessed by any user.
     *
     * @return A message that can be accessed by any user.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/userping")
    public String userPing() {
        return "Any User Can Read This";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create/employee")
    public User createEmployee(@RequestBody UserDto user) {
        return userService.createEmployee(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/find/all")
    public List<User> getAllList() {
        return userService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/find/by/username")
    public User getAllList(@RequestParam String username) {
        return userService.findOne(username);
    }
}
