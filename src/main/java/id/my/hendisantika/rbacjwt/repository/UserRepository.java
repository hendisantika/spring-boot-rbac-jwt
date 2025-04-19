package id.my.hendisantika.rbacjwt.repository;

import id.my.hendisantika.rbacjwt.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-rbac-jwt
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 19/04/25
 * Time: 14.01
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
