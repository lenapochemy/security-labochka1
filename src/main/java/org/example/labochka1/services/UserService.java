package org.example.labochka1.services;


import org.example.labochka1.dto.UserDTO;
import org.example.labochka1.model.User;
import org.example.labochka1.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;

//    @Autowired
    public UserService(UserRepository userRepository, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }

    public User login(UserDTO dto){
//        User user = mapper.userFromDTO(dto);
        User userFromDB = userRepository.findByLogin(dto.getLogin());
        if(userFromDB != null && passwordEncoder.matches(dto.getPassword(), userFromDB.getPassword())){
            return userFromDB;
        } else return null;
    }


    public User registration(UserDTO dto){
        User user = new User(dto.getLogin(), passwordEncoder.encode(dto.getPassword()));
        User userFromDB = userRepository.findByLogin(user.getLogin());
        if(userFromDB != null){
            return null;
        } else {
            return userRepository.save(user);
        }
    }


    public String generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(user.getUsername())
                .claim("uid", user.getId())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
