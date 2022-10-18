package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.models.User;
import web.models.role.Role;
import web.repository.UserRepository;

import java.security.Principal;
import java.util.Collections;

@Service
@Transactional()
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean createUser(User user){
        String email = user.getUsername();
        var user1 = userRepository.findByUsername(email);
        if(user1!=null) return  false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.USER);
        userRepository.save(user);
        return  true;
    }

    public web.models.User getUserByPrincipal(Principal principal) {
            if (principal == null) {
                return new User();
            }
            return userRepository.findByUsername(principal.getName());

    }
}
