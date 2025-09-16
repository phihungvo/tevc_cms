package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.TeamDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.TeamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ApiResponse<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        return ApiResponse.<TeamDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Create team successful")
                .result(teamService.createTeam(teamDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<TeamDTO> updateTeam(@PathVariable Integer id, @RequestBody TeamDTO teamDTO) {
        return ApiResponse.<TeamDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Update team successful")
                .result(teamService.updateTeam(id, teamDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TeamDTO> getTeamById(@PathVariable Integer id) {
        return ApiResponse.<TeamDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get team successful")
                .result(teamService.getTeamById(id))
                .build();
    }

    @GetMapping("/no-paging")
    public ApiResponse<List<TeamDTO>> getAllTeamsNoPaging() {
        return ApiResponse.<List<TeamDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all teams successful")
                .result(teamService.getAllTeamsNoPaging())
                .build();
    }

    @GetMapping
    public ResponseEntity<Page<TeamDTO>> getAllTeams(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getAllTeams(page, size));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTeam(@PathVariable Integer id) {
        teamService.deleteTeam(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete team successful")
                .build();
    }

    @PutMapping("/{teamId}/add-member/{employeeId}")
    public ApiResponse<TeamDTO> addMember(@PathVariable Integer teamId, @PathVariable Integer employeeId) {
        return ApiResponse.<TeamDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Add member to team successful")
                .result(teamService.addMember(teamId, employeeId))
                .build();
    }

    @PutMapping("/{teamId}/remove-member/{employeeId}")
    public ApiResponse<TeamDTO> removeMember(@PathVariable Integer teamId, @PathVariable Integer employeeId) {
        return ApiResponse.<TeamDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Remove member from team successful")
                .result(teamService.removeMember(teamId, employeeId))
                .build();
    }
}
