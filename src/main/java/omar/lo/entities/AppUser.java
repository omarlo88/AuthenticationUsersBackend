package omar.lo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nom;
    @NotNull
    private String prenom;
    @Column(unique = true, nullable = false)
    private String username;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Email(message = "Il faut saisir un email valid!!")
    @Column(unique = true, nullable = false)
    private String email;
    @Type(type = "true_false")
    private boolean actived;
    @Type(type = "yes_no")
    private boolean accountNonExpired;
    @Type(type = "true_false")
    private boolean credentialsNonExpired;
    //@Type(type = "true_false")
    private boolean accountNonLocked;
    @Lob
    private byte[] photo;
    @Column(name = "photo_name")
    private String photoName;
    @Column(name = "date_creation")
    //private Instant dateCreation;
    //@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateCreation;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roles = new TreeSet<>();

}// AppUser
