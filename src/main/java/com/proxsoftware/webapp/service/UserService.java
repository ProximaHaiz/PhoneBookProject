package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
   /* @Value("${app.user.verification}")
    private Boolean requireActivation;*/

    //    @Value("${app.secret}")
    private String applicationSecret;


    @Autowired
    private AccountRepository repo;

    @Autowired
    private HttpSession httpSession;

    public final String CURRENT_USER_KEY = "CURRENT_USER";

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AccountEntity user = repo.findOneByUserName(username);
        System.out.println("from UserDeatails: " + user);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        /*if (requireActivation && !user.getToken().equals("1")) {
            Application.log.error("User [" + username + "] tried to login but is not activated");
            throw new UsernameNotFoundException(username + " has not been activated yet");
        }*/
        httpSession.setAttribute(CURRENT_USER_KEY, user);
        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(), user.getPassword(), auth);
    }

    public void autoLogin(AccountEntity user) {
        autoLogin(user.getUserName());
    }

    public void autoLogin(String username) {
        UserDetails userDetails = this.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    public AccountEntity register(AccountEntity user) {
        user.setPassword(encodeUserPassword(user.getPassword()));

        if (this.repo.findOneByUserName(user.getUserName()) == null) {
            String activation = createActivationToken(user, false);
            user.setToken(activation);
            this.repo.saveAndFlush(user);
            return user;
        }

        return null;
    }


    public String encodeUserPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Boolean delete(Long id) {
        this.repo.deleteAccountById(id);
        return true;
    }

    public AccountEntity activate(String activation) {
        if (activation.equals("1") || activation.length() < 5) {
            return null;
        }
        AccountEntity u = this.repo.findOneByToken(activation);
        if (u != null) {
            u.setToken("1");
            this.repo.saveAndFlush(u);
            return u;
        }
        return null;
    }

    public String createActivationToken(AccountEntity user, Boolean save) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String activationToken = encoder.encodePassword(user.getUserName(), applicationSecret);
        if (save) {
            user.setToken(activationToken);
            this.repo.saveAndFlush(user);
        }
        return activationToken;
    }

    /*public String createResetPasswordToken(AccountEntity user, Boolean save) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String resetToken = encoder.encodePassword(user.getEmail(), applicationSecret);
        if (save) {
            user.setToken(resetToken);
            this.repo.save(user);
        }
        return resetToken;
    }*/

    /*public AccountEntity resetActivation(String email) {
        AccountEntity u = this.repo.findOneByEmail(email);
        if (u != null) {
            createActivationToken(u, true);
            return u;
        }
        return null;
    }*/

    public Boolean resetPassword(AccountEntity user) {
        AccountEntity u = this.repo.findOneByUserName(user.getUserName());
        if (u != null) {
            u.setPassword(encodeUserPassword(user.getPassword()));
            u.setToken("1");
            this.repo.saveAndFlush(u);
            return true;
        }
        return false;
    }

    public void updateUser(AccountEntity user) {
        updateUser(user.getUserName(), user);
    }

    public void updateUser(String userName, AccountEntity newData) {
        this.repo.updateUser(
                userName,
                newData.getFirstName(),
                newData.getLastName(),
                newData.getLastName());

    }

    public AccountEntity getLoggedInUser() {
        return getLoggedInUser(false);
    }

    public AccountEntity getLoggedInUser(Boolean forceFresh) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountEntity user = (AccountEntity) httpSession.getAttribute(CURRENT_USER_KEY);
        if (forceFresh || httpSession.getAttribute(CURRENT_USER_KEY) == null) {
            user = this.repo.findOneByUserName(userName);
            httpSession.setAttribute(CURRENT_USER_KEY, user);
        }
        return user;
    }

  /*  public void updateLastLogin(String userName) {
        this.repo.updateLastLogin(userName);
    }*/

    /*public void updateProfilePicture(AccountEntity user, String profilePicture) {
        this.repo.updateProfilePicture(user.getUserName(), profilePicture);
    }*/
}