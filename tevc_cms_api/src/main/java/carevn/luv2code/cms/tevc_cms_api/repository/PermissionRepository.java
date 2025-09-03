package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    //    Optional<Permission> findByResourceAndAction(String resource, String action);

    Optional<Permission> findByName(String name);
}
