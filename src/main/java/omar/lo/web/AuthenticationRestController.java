package omar.lo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import omar.lo.entities.AppRole;
import omar.lo.entities.AppUser;
import omar.lo.metier.AccountServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/AuthenticationRestController")
public class AuthenticationRestController {

    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServletContext servletContext;

    @PostMapping("/register")
    public AppUser register(@RequestParam("file") MultipartFile file, String user) throws IOException {
        UserForm userForm = objectMapper.readValue(user, UserForm.class);
        String username = userForm.getUsername();
        AppUser userFromDatabase = accountService.loadUserByUsername(username);
        if (userFromDatabase!= null){ throw new RuntimeException("Cet utilisateur existe déjà!!");}
        String password = userForm.getPassword();
        String confirmedPassword = userForm.getConfirmedPassword();
        if (!password.equals(confirmedPassword)){ throw new RuntimeException("Veuillez confirmer votre password!");}
        AppUser appUser = new AppUser();
        appUser.setNom(userForm.getNom());
        appUser.setPrenom(userForm.getPrenom());
        appUser.setUsername(username);
        appUser.setPassword(password);
        appUser.setEmail(userForm.getEmail());
        appUser.setPhoto(file.getBytes());
        appUser.setPhotoName(file.getOriginalFilename());
        appUser.setDateCreation(new Date());
        accountService.saveUser(appUser);
        accountService.addRoleToUser(username, "USER");

        System.out.println("*****************************");
        System.out.println(file.getContentType());
        System.out.println(file.getInputStream().toString());
        System.out.println(file.getName());
        System.out.println(file.getResource().getFilename());
        System.out.println(file.getSize());
        System.out.println("*****************************");
        return appUser;
    }

    @PostMapping("/registerServeurFile")
    public AppUser registerServeurFile(@RequestParam("file") MultipartFile file, String user) throws IOException {
        UserForm userForm = objectMapper.readValue(user, UserForm.class);
        boolean isExiste = new File(servletContext.getRealPath("/usersImage/")).exists();
        if (!isExiste){
            new File(servletContext.getRealPath("/usersImage/")).mkdir();
        }
        String fileName = file.getOriginalFilename();
        String newFileName = userForm.getUsername();
        String modifiedFileName = FilenameUtils.getBaseName(newFileName) + "_" + System.currentTimeMillis()
                + "." + FilenameUtils.getExtension(fileName);

        File serveurFile = new File(servletContext.getRealPath("/usersImage/" + File.separator + modifiedFileName));
        try {
            FileUtils.writeByteArrayToFile(serveurFile, file.getBytes());
        } catch (Exception e){
            System.out.println("AuthenticationRestController.registerServeur(): " + e.getMessage());
            e.printStackTrace();
        }

        String username = userForm.getUsername();
        AppUser userFromDatabase = accountService.loadUserByUsername(username);
        if (userFromDatabase!= null){ throw new RuntimeException("Cet utilisateur existe déjà!!");}
        String password = userForm.getPassword();
        String confirmedPassword = userForm.getConfirmedPassword();
        if (!password.equals(confirmedPassword)){ throw new RuntimeException("Veuillez confirmer votre password!");}
        AppUser appUser = new AppUser();
        appUser.setNom(userForm.getNom());
        appUser.setPrenom(userForm.getPrenom());
        appUser.setUsername(username);
        appUser.setPassword(password);
        appUser.setEmail(userForm.getEmail());
        //appUser.setPhoto(file.getBytes());
        //appUser.setPhotoName(file.getOriginalFilename());
        appUser.setPhotoName(modifiedFileName);
        appUser.setDateCreation(new Date());
        accountService.saveUser(appUser);
        accountService.addRoleToUser(username, "USER");
        return appUser;
    }

    @GetMapping("/Users/PhotoUser/{username}")
    public ResponseEntity<Map<String, String>> getPhotoUser(@PathVariable String username){
        Map<String, String> hm = new HashMap<>();
        String filesPath = servletContext.getRealPath("/usersImage");
        File fileFolder = new File(filesPath);
        if (fileFolder != null){
            for (File file : fileFolder.listFiles()) {
                if (!file.isDirectory() && file.getName().startsWith(username)){
                    String encodeBase64;
                    try {
                        String extensionFile = FilenameUtils.getExtension(file.getName());
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[(int) file.length()];
                        fileInputStream.read(bytes);
                        encodeBase64 = Base64.getEncoder().encodeToString(bytes);
                        //String photo = extensionFile + ";base64," + encodeBase64;
                        hm.put("extensionFile", extensionFile);
                        hm.put("photo", encodeBase64);
                        fileInputStream.close();
                    } catch (Exception e){
                        e.printStackTrace();
                        System.out.println("AuthenticationRestController.getPhotouser(): " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return new ResponseEntity<>(hm, HttpStatus.OK);
    }

    @GetMapping("/Users")
    public List<AppUser> getUsers(){
        return accountService.getAppUsers();
    }

    @GetMapping("/Users/{username}")
    public AppUser loadUserByUsername(@PathVariable(value = "username") String username){
        return accountService.loadUserByUsername(username);
    }

    @GetMapping("/Users/{eamil}")
    public AppUser loadUserByEmail(@PathVariable("email") String email){
        return accountService.loadUserByEmail(email);
    }

    @PutMapping("/Users/UpdateInfo/{id}")
    public AppUser updateUser(@PathVariable Long id, @RequestBody @Valid AppUser appUser){
        appUser.setId(id);
        return accountService.saveUser(appUser);
    }

    @PutMapping("/Users/UpdatePhoto/{id}")
    public AppUser updateUserPhoto(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws IOException {
        AppUser appUser = accountService.getUser(id);
        appUser.setPhoto(file.getBytes());
        appUser.setPhotoName(file.getOriginalFilename());
        return accountService.saveUser(appUser);
    }

    @DeleteMapping("/Users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        accountService.deleteUser(id);
        return new ResponseEntity<>("Suppression réussie!!", HttpStatus.OK);
    }

    @DeleteMapping("/Users")
    public ResponseEntity<String> deleteAllUsers(){
        accountService.deleteAllusers();
        return new ResponseEntity<>("Suppression réussie!!", HttpStatus.OK);
    }

    @PutMapping("/Users/ActivedDisabled/{username}")
    public ResponseEntity<String> activedDisabledUser(@PathVariable String username){
        accountService.activedDisabledUser(username);
        return new ResponseEntity<>("Opération réussie!!", HttpStatus.ACCEPTED.OK);
    }

    @PutMapping("/Users/AccountNonExpired/{username}")
    public ResponseEntity<String> accountNonExpired(@PathVariable String username){
        accountService.accountUserNonExpired(username);
        return new ResponseEntity<>("Opération réussie!!", HttpStatus.ACCEPTED.OK);
    }

    @PutMapping("/Users/CredentialsNonExpired/{username}")
    public ResponseEntity<String> credentialsNonExpired(@PathVariable String username){
        accountService.credentialsUserNonExpired(username);
        return new ResponseEntity<>("Opération réussie!!", HttpStatus.ACCEPTED.OK);
    }

    @PutMapping("/Users/AccountNonLocked/{username}")
    public ResponseEntity<String> accountNonLocked(@PathVariable String username){
        accountService.accountUserNonLocked(username);
        return new ResponseEntity<>("Opération réussie!!", HttpStatus.ACCEPTED.OK);
    }

    @PutMapping("/AddRoleToUser")
    public ResponseEntity<String> addRoleToUser(
            @RequestParam("username") String username,
            @RequestParam(name = "roleName", defaultValue = "USER") String roleName){
        accountService.addRoleToUser(username, roleName);
        return new ResponseEntity<>("Ajout réussi!!", HttpStatus.OK);
    }

    @PutMapping("/DeleteRoleUser")
    public ResponseEntity<String> deleteRoleUser(
            @RequestParam String username,
            @RequestParam(name = "roleName", defaultValue = "USER") String roleName){
        accountService.deleteRoleUser(username, roleName);
        return new ResponseEntity<>("Suppression réussie!!", HttpStatus.OK);
    }

    @GetMapping("/ChercherUsersParMotCle")
    public Page<AppUser> chercherUsersParMotCle(
            @RequestParam(name = "motCle", defaultValue = "") String motCle,
            //@RequestParam(name = "page", defaultValue = "0") int page
            @RequestParam(value = "page", defaultValue = "0") int page,
            //@RequestParam(name = "size", defaultValue = "5", required = true) int size
            @RequestParam(name = "size", defaultValue = "5", required = true) int size){
        return accountService.chercherUsersParMotCle(motCle, page, size);
    }

    @PostMapping("/Roles")
    public AppRole saveRole (@RequestBody @Valid AppRole appRole){
        return accountService.saveRole(appRole);
    }

    @GetMapping("/Roles")
    public List<AppRole> getRoles(){
        return accountService.getAppRoles();
    }

    @GetMapping("/Roles/{id}")
    public AppRole getRole(@PathVariable Long id){
        return accountService.getRole(id);
    }

    @GetMapping("/ChercherRoleParMotCle")
    public Page<AppRole> chercherRoleParMotCle(
            @RequestParam(name = "motCle", defaultValue = "") String motCle,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size){
        return accountService.chercherRolesParMotCle(motCle, page, size);
    }

    @DeleteMapping("/Roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id){
        accountService.deleteRole(id);
        return new ResponseEntity<>("Suppression réussie!!", HttpStatus.OK);
    }

    @DeleteMapping("/Roles")
    public ResponseEntity<String> deleteAllRoles(){
        accountService.deleteAllRoles();
        return new ResponseEntity<>("Suppression réussie!!", HttpStatus.OK);
    }

    @PutMapping("/Roles/{id}")
    public AppRole updateRole(@PathVariable Long id, @RequestBody AppRole appRole){
        appRole.setId(id);
        return accountService.saveRole(appRole);
    }

}// AuthenticationRestController
