package omar.lo.metier;

import omar.lo.dao.AppRoleRepository;
import omar.lo.dao.AppUserRepository;
import omar.lo.entities.AppRole;
import omar.lo.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AppUser saveUser(AppUser user) {
        String hashPW = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPW);
        user.setActived(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        return appUserRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser loadUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUser getUser(Long id) {
        //appUserRepository.findById(id).get();
        return appUserRepository.getOne(id);
    }

    @Override
    public AppRole getRole(Long id) {
        return appRoleRepository.getOne(id);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null){ throw new RuntimeException("Cet utilisateur n'existe pas !!");}
        AppRole role = appRoleRepository.findByRoleName(roleName);
        if (role == null){ throw new RuntimeException("Ce rôle n'existe pas !!");}
        if (appUser.getRoles().contains(role)) {
            throw new RuntimeException("Cet utilisateur a déjà ce rôle!!");
        }
        appUser.getRoles().add(role);
    }

    @Override
    public List<AppUser> getAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public List<AppRole> getAppRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public Page<AppUser> chercherUsersParMotCle(String motCle, int page, int size) {
        return appUserRepository.findByNomStartsWithOrderByNom(motCle, PageRequest.of(page, size));
    }

    @Override
    public Page<AppRole> chercherRolesParMotCle(String motCle, int page, int size) {
        //appRoleRepository.chercherRoles(motCle, PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "roleName")));
        return appRoleRepository.findByRoleNameLikeOrderByRoleName(motCle, PageRequest.of(page, size));
    }

    /*@Override
    public Page<AppRole> chercherRolesParMotCle(String motCle) {
        return appRoleRepository.findByRoleNameLikeOrderByRoleName(motCle);
    }*/

    @Override
    public void activedDisabledUser(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        appUser.setActived(!appUser.isActived());
    }

    @Override
    public void accountUserNonExpired(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        appUser.setAccountNonExpired(!appUser.isAccountNonExpired());
    }

    @Override
    public void credentialsUserNonExpired(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        appUser.setCredentialsNonExpired(!appUser.isCredentialsNonExpired());
    }

    @Override
    public void accountUserNonLocked(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        appUser.setAccountNonLocked(!appUser.isAccountNonLocked());
    }

    @Override
    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public void deleteRole(Long id) {
        appRoleRepository.deleteById(id);
    }

    @Override
    public void deleteRoleUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole role = appRoleRepository.findByRoleName(roleName);
        if (appUser.getRoles().contains(role)) {
            appUser.getRoles().remove(role);
        } else {
            throw new RuntimeException("Cet utilisateur n'avait pas ce rôle!!");
        }
    }

    @Override
    public void deleteAllRoles() {
        appUserRepository.deleteAll();
    }

    @Override
    public void deleteAllusers() {
        appRoleRepository.deleteAll();
    }

}// AccountServiceImpl
