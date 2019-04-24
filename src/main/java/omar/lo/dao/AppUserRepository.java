package omar.lo.dao;

import omar.lo.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

//@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
    //Page<AppUser> findByNomStartsWithOrderByNom(@Param("motCle") String motCle, Pageable pageable);
    Page<AppUser> findByNomStartsWithOrderByNom(String motCle, Pageable pageable);

    /*@Query(value = "select u from AppUser u where u.username like :x ORDER BY u.username DESC")
    Page<AppUser> chercherUsersParMotCle(@Param("x") String motCle, Pageable pageable);*/

}// AppUserRepository
