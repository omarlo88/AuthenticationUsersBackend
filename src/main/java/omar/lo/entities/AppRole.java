package omar.lo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class AppRole implements Comparable<AppRole> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@NotNull
    @Column(unique = true, nullable = false, length = 50)
    private String roleName;

    @Override
    public int compareTo(AppRole role) {
        return roleName.compareTo(role.roleName);
    }

    @Override
    public boolean equals(Object role) {
        return id.equals(((AppRole)role).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }

}// AppRole
