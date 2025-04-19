package id.my.hendisantika.rbacjwt.service;

import id.my.hendisantika.rbacjwt.model.Role;
import id.my.hendisantika.rbacjwt.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-rbac-jwt
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 19/04/25
 * Time: 14.02
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "roleService")
@RequiredArgsConstructor
public class RoleService {
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        Role role = roleRepository.findRoleByName(name);
        return role;
    }
}
