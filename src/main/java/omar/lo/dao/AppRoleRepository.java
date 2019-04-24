package omar.lo.dao;

import omar.lo.entities.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

//@RepositoryRestResource
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

    AppRole findByRoleName(String roleName);
    Page<AppRole> findByRoleNameLikeOrderByRoleName(String motCle, Pageable page);

    //@Query("select r from AppRole r where r.roleName like :x")
    /*@Query("select r from AppRole r where r.roleName like :x Order By r.roleName DESC")
    Page<AppRole> chercherRolesParMotCle(@Param("x") String motCle, Pageable page);*/





}// AppRoleRepository
