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

    @Autowired
    private BucketService bucketService;

    private Long countGuest= Long.valueOf(0);


    public boolean createUser(User user){
        String email = user.getUsername();
        var user1 = userRepository.findByUsername(email);
        if(user1!=null) return  false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.USER);
        userRepository.save(user);
        user.setBucket(bucketService.createBucket(user));
        userRepository.save(user);
        return  true;
    }

    public User createGuest(){
        User user =new User();
        String username ="GUEST#"+countGuest;
        var user1 =userRepository.findByUsername(username);
        if(user1!=null){
            return user1;
        }else {
        user.setUsername(username);
        user.getRoles().add(Role.GUEST);
        user.setArchive(false);
        userRepository.save(user);}
        return user;
    }

    public void deleteGuest(String username){
        var user =userRepository.findByUsername(username);
        userRepository.delete(user);
        countGuest++;
    }

    public web.models.User getUserByPrincipal(Principal principal) {
            if (principal == null) {
                return new User();
            }
            return userRepository.findByUsername(principal.getName());

    }
}
