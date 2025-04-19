package id.my.hendisantika.rbacjwt.controller;

import id.my.hendisantika.rbacjwt.config.TokenProvider;
import id.my.hendisantika.rbacjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
