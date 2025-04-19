package id.my.hendisantika.rbacjwt.service;

import id.my.hendisantika.rbacjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-rbac-jwt
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 19/04/25
 * Time: 14.04
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "userService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final RoleService roleService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bcryptEncoder;

}
