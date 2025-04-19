package id.my.hendisantika.rbacjwt.service;

import id.my.hendisantika.rbacjwt.model.Role;
import id.my.hendisantika.rbacjwt.model.User;
import id.my.hendisantika.rbacjwt.model.UserDto;
import id.my.hendisantika.rbacjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    public User findOne(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(UserDto user) {
        User nUser = user.getUserFromDto();
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        // Set default role as USER
        Role role = roleService.findByName("USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        // If email domain is admin.edu, add ADMIN role
        if (nUser.getEmail().split("@")[1].equals("admin.edu")) {
            role = roleService.findByName("ADMIN");
            roleSet.add(role);
        }

        nUser.setRoles(roleSet);
        return userRepository.save(nUser);
    }

    public User createEmployee(UserDto user) {
        User nUser = user.getUserFromDto();
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        Role employeeRole = roleService.findByName("EMPLOYEE");
        Role customerRole = roleService.findByName("USER");

        Set<Role> roleSet = new HashSet<>();
        if (employeeRole != null) {
            roleSet.add(employeeRole);
        }
        if (customerRole != null) {
            roleSet.add(customerRole);
        }

        nUser.setRoles(roleSet);
        return userRepository.save(nUser);
    }
}
