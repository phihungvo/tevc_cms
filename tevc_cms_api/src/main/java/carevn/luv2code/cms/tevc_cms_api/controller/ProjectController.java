package carevn.luv2code.cms.tevc_cms_api.controller;

import carevn.luv2code.cms.tevc_cms_api.dto.ProjectDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AuthenticationRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.LogoutRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.response.AuthenticationResponse;
import carevn.luv2code.cms.tevc_cms_api.dto.response.LogoutResponse;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.security.AuthService;
import carevn.luv2code.cms.tevc_cms_api.security.JwtService;
import carevn.luv2code.cms.tevc_cms_api.security.TokenBlacklist;
import carevn.luv2code.cms.tevc_cms_api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAuthority('PROJECT:CREATE')")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable UUID id,
            @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public ResponseEntity<ProjectDTO> addMembers(
            @PathVariable UUID id,
            @RequestBody List<UUID> memberIds) {
        return ResponseEntity.ok(projectService.addMembers(id, memberIds));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public ResponseEntity<ProjectDTO> removeMember(
            @PathVariable UUID projectId,
            @PathVariable UUID memberId) {
        return ResponseEntity.ok(projectService.removeMember(projectId, memberId));
    }

    @PatchMapping("/{id}/manager/{managerId}")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public ResponseEntity<ProjectDTO> assignManager(
            @PathVariable UUID id,
            @PathVariable UUID managerId) {
        return ResponseEntity.ok(projectService.assignManager(id, managerId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PROJECT:DELETE')")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
