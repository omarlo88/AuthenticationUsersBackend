package omar.lo.sec;

import omar.lo.entities.AppUser;
import omar.lo.metier.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountServiceImpl accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser == null){throw new UsernameNotFoundException("Invalid User!!");}
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        appUser.getRoles().forEach(appRole -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(appRole.getRoleName()));
        });

        return new User(appUser.getUsername(), appUser.getPassword(), appUser.isActived(), appUser.isAccountNonExpired(), appUser.isCredentialsNonExpired(), appUser.isAccountNonLocked(), grantedAuthorities);
    }

}// UserDetailsServiceImpl
