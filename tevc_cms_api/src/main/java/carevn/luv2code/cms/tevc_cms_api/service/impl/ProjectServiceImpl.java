package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.ProjectDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Project;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.ProjectMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.ProjectRepository;
import carevn.luv2code.cms.tevc_cms_api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = projectMapper.toEntity(projectDTO);
        
        if (projectDTO.getProjectManagerId() != null) {
            Employee manager = employeeRepository.findById(projectDTO.getProjectManagerId())
                    .orElseThrow(() -> new AppException("Project manager not found"));
            project.setProjectManager(manager);
        }
        
        project.setStatus("ACTIVE");
        return projectMapper.toDTO(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectDTO addMembers(UUID id, List<UUID> memberIds) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        
        List<Employee> members = employeeRepository.findAllById(memberIds);
        if (members.size() != memberIds.size()) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }
        project.getMembers().addAll(members);
        
        return projectMapper.toDTO(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectDTO removeMember(UUID projectId, UUID memberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        
        Employee member = employeeRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
                
        project.getMembers().remove(member);
        return projectMapper.toDTO(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectDTO assignManager(UUID id, UUID managerId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_MANAGER_NOT_FOUND));
                
        project.setProjectManager(manager);
        return projectMapper.toDTO(projectRepository.save(project));
    }

    @Override
    public ProjectDTO getProject(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        return projectMapper.toDTO(project);
    }

    // ... other standard CRUD methods implementation ...
}
