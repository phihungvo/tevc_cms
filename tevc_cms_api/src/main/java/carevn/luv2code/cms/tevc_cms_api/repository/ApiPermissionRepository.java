package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.ApiPermission;

@Repository
public interface ApiPermissionRepository extends JpaRepository<ApiPermission, Integer> {
    @Cacheable(value = "apiPermissions", key = "#endpoint + '-' + #httpMethod")
    Optional<ApiPermission> findByEndpointAndHttpMethod(String endpoint, String httpMethod);

    @Cacheable(value = "apiPermissionsList")
    List<ApiPermission> findAll();
}
