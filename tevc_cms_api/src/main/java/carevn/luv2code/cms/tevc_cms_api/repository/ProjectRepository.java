package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByProjectManagerId(UUID managerId);

    List<Project> findByMembersId(UUID employeeId);

    List<Project> findByStatus(String status);
}
