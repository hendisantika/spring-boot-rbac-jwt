package id.my.hendisantika.rbacjwt.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenProviderTest {

    private static final String SECRET_KEY = "testSecretKey123456789012345678901234567890";
    private static final long TOKEN_VALIDITY = 3600; // 1 hour
    private static final String AUTHORITIES_KEY = "roles";
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setup() {
        tokenProvider = new TokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "SIGNING_KEY", SECRET_KEY);
        ReflectionTestUtils.setField(tokenProvider, "TOKEN_VALIDITY", TOKEN_VALIDITY);
        ReflectionTestUtils.setField(tokenProvider, "AUTHORITIES_KEY", AUTHORITIES_KEY);
    }

    @Test
    public void testGenerateToken() {
        // Arrange
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        // Act
        String token = tokenProvider.generateToken(authentication);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    public void testGetUsernameFromToken() {
        // Arrange
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        String token = tokenProvider.generateToken(authentication);

        // Act
        String username = tokenProvider.getUsernameFromToken(token);

        // Assert
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    public void testGetExpirationDateFromToken() {
        // Arrange
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        String token = tokenProvider.generateToken(authentication);

        // Act
        Date expirationDate = tokenProvider.getExpirationDateFromToken(token);

        // Assert
        assertThat(expirationDate).isAfter(new Date());
    }

    @Test
    public void testValidateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        String token = tokenProvider.generateToken(authentication);

        // Act
        boolean isValid = tokenProvider.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_WithInvalidUsername_ShouldReturnFalse() {
        // Arrange
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        String token = tokenProvider.generateToken(authentication);

        UserDetails differentUser = new User("differentuser", "password", authorities);

        // Act
        boolean isValid = tokenProvider.validateToken(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testGetAuthenticationToken() {
        // Arrange
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        UserDetails userDetails = new User("testuser", "password", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        String token = tokenProvider.generateToken(authentication);

        // Act
        UsernamePasswordAuthenticationToken authToken = tokenProvider.getAuthenticationToken(token, null, userDetails);

        // Assert
        assertThat(authToken).isNotNull();
        assertThat(authToken.getPrincipal()).isEqualTo(userDetails);

        Collection<? extends GrantedAuthority> resultAuthorities = authToken.getAuthorities();
        assertThat(resultAuthorities).isNotNull();
        assertThat(resultAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))).isTrue();
        assertThat(resultAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))).isTrue();
    }
}