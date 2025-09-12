package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.ProjectDTO;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Integer id, ProjectDTO projectDTO);

    void deleteProject(Integer id);

    ProjectDTO getProject(Integer id);

    Page<ProjectDTO> getAllProjects(int page, int size);

    ProjectDTO addMembers(Integer id, List<Integer> memberIds);

    ProjectDTO removeMember(Integer projectId, Integer memberId);

    ProjectDTO assignManager(Integer id, Integer managerId);
}
