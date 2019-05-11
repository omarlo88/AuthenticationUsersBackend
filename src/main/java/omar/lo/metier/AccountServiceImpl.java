package omar.lo.metier;

import omar.lo.dao.AppRoleRepository;
import omar.lo.dao.AppUserRepository;
import omar.lo.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public AppUser updateUser(Long id, AppUser user) {
        return appUserRepository.findById(id)
                .map(userDb ->{
                    userDb.setNom(user.getNom());
                    userDb.setPrenom(user.getPrenom());
                    userDb.setUsername(user.getUsername());
                    userDb.setEmail(user.getEmail());
                    return appUserRepository.save(userDb);
                })
                .orElseGet(()->{
                    user.setId(id);
                    return appUserRepository.save(user);
                });
    }

    @Override
    public AppUser updateUserPhoto(MultipartFile file, Long id) {
        return appUserRepository.findById(id)
                .map(user ->{
                    try {
                        user.setPhoto(file.getBytes());
                        user.setPhotoName(file.getOriginalFilename());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return appUserRepository.save(user);

                })
                .orElseThrow(()-> new AppUserNotFoundException(id));
    }

    @Override
    public AppRole updateRole(Long id, AppRole role) {
        return appRoleRepository.findById(id)
                .map(r ->{
                    r.setRoleName(role.getRoleName());
                    return appRoleRepository.save(r);
                })
                .orElseGet(()->{
                    role.setId(id);
                    return appRoleRepository.save(role);
                });
    }

    @Override
    public AppUser getUser(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserNotFoundException(id));
    }

    @Override
    public AppRole getRole(Long id) {
        return appRoleRepository.findById(id)
                .orElseThrow(() -> new AppRoleNotFoundException(id));
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null){ throw new AppUserNotFoundException(username);}
        AppRole role = appRoleRepository.findByRoleName(roleName);
        if (role == null){ throw new AppRoleNotFoundException(roleName);}
        if (appUser.getRoles().contains(role)) {
            throw new RoleUserExisteException(roleName, username);
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
            throw new RoleUserNotFoundException(roleName, username);
        }
    }

    @Override
    public void deleteAllRoles() {
        appUserRepository.deleteAll();
    }

    @Override
    public void deleteAllUsers() {
        appRoleRepository.deleteAll();
    }

}// AccountServiceImpl
