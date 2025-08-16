package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.ProjectDTO;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(UUID id, ProjectDTO projectDTO);

    void deleteProject(UUID id);

    ProjectDTO getProject(UUID id);

    Page<ProjectDTO> getAllProjects(int page, int size);

    ProjectDTO addMembers(UUID id, List<UUID> memberIds);

    ProjectDTO removeMember(UUID projectId, UUID memberId);

    ProjectDTO assignManager(UUID id, UUID managerId);
}
