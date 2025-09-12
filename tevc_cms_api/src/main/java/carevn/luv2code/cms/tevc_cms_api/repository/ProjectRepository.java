package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByProjectManagerId(Integer managerId);

    List<Project> findByMembersId(Integer employeeId);

    List<Project> findByStatus(String status);
}
