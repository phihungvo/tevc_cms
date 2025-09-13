package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.ProjectDTO;
import carevn.luv2code.cms.tevc_cms_api.service.ProjectService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Integer id, @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ProjectDTO> addMembers(@PathVariable Integer id, @RequestBody List<Integer> memberIds) {
        return ResponseEntity.ok(projectService.addMembers(id, memberIds));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ProjectDTO> removeMember(@PathVariable Integer projectId, @PathVariable Integer memberId) {
        return ResponseEntity.ok(projectService.removeMember(projectId, memberId));
    }

    @PatchMapping("/{id}/manager/{managerId}")
    public ResponseEntity<ProjectDTO> assignManager(@PathVariable Integer id, @PathVariable Integer managerId) {
        return ResponseEntity.ok(projectService.assignManager(id, managerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
