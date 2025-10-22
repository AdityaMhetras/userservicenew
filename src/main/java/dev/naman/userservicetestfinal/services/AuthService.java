package dev.naman.userservicetestfinal.services;

import dev.naman.userservicetestfinal.config.JwtConfig;
import dev.naman.userservicetestfinal.dtos.UserDto;
import dev.naman.userservicetestfinal.mapper.UserMapper;
import dev.naman.userservicetestfinal.models.Session;
import dev.naman.userservicetestfinal.models.SessionStatus;
import dev.naman.userservicetestfinal.models.User;
import dev.naman.userservicetestfinal.repositories.SessionRepository;
import dev.naman.userservicetestfinal.repositories.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtConfig = jwtConfig;
    }

    public ResponseEntity<UserDto> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        claims.put("createdAt", new Date());
        claims.put("expiryAt", new Date(System.currentTimeMillis() + 86400000)); // 1 day expiration

        String token = Jwts.builder()
                .signWith(jwtConfig.jwtSecretKey())
                .subject(user.getEmail())
                .claims(claims)
                .compact();

//        String token = RandomStringUtils.randomAlphanumeric(30);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserMapper.INSTANCE.toDto(user);

//        Map<String, String> headers = new HashMap<>();
//        headers.put(HttpHeaders.SET_COOKIE, token);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);


        //        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        String bcryptPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(bcryptPassword);
        
        User savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDto(savedUser);
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();
        if (session.getSessionStatus() != SessionStatus.ACTIVE) {
            return null;
        }

        // verify jwt token
        try {

            Jwts.parser().verifyWith(jwtConfig.jwtSecretKey()).build().parseSignedClaims(token);

            //OK, we can trust this JWT

        } catch (JwtException e) {

            //don't trust the JWT!
            System.out.println("JWT not valid: " + e.getMessage());
            return null;
        }


        if( !Jwts.parser().verifyWith(jwtConfig.jwtSecretKey()).build().parseSignedClaims(token).getPayload().getSubject().equals("3@g.com") ){
            //don't trust the JWT!
            System.out.println("JWT not valid: Subject mismatch");
            return null;
        }

        return SessionStatus.ACTIVE;
    }

}
