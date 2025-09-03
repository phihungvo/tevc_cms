package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.ApiPermission;

@Repository
public interface ApiPermissionRepository extends JpaRepository<ApiPermission, UUID> {}
