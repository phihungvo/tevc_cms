package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.SkillDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.SkillService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public ApiResponse<SkillDTO> createSkill(@RequestBody SkillDTO skillDTO) {
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Create skill successful")
                .result(skillService.createSkill(skillDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SkillDTO> updateSkill(@PathVariable Integer id, @RequestBody SkillDTO skillDTO) {
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Update skill successful")
                .result(skillService.updateSkill(id, skillDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SkillDTO> getSkillById(@PathVariable Integer id) {
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get skill successful")
                .result(skillService.getSkillById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SkillDTO>> getAllSkills() {
        return ApiResponse.<List<SkillDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all skills successful")
                .result(skillService.getAllSkills())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSkill(@PathVariable Integer id) {
        skillService.deleteSkill(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete skill successful")
                .build();
    }
}
