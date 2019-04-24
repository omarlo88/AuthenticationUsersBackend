package omar.lo.metier;

import omar.lo.entities.AppRole;
import omar.lo.entities.AppUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {

    AppUser saveUser(AppUser user);
    AppRole saveRole(AppRole appRole);
    AppUser loadUserByUsername(String username);
    AppUser loadUserByEmail(String email);
    AppUser getUser(Long id);
    AppRole getRole(Long id);
    void addRoleToUser(String username, String roleName);
    List<AppUser> getAppUsers();
    List<AppRole> getAppRoles();
    Page<AppUser> chercherUsersParMotCle(String motCle, int page, int size);
    Page<AppRole> chercherRolesParMotCle(String motCle, int page, int size);
    void deleteUser(Long id);
    void deleteRole(Long id);
    void deleteRoleUser(String username, String roleName);
    void deleteAllRoles();
    void deleteAllUsers();
    void activedDisabledUser(String username);
    void accountUserNonExpired(String username);
    void credentialsUserNonExpired(String username);
    void accountUserNonLocked(String username);


}// AccountService
