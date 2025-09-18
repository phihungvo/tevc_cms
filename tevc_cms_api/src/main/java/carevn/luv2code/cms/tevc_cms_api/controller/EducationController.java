package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.EducationDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.EducationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    public ApiResponse<EducationDTO> createEducation(@RequestBody EducationDTO educationDTO) {
        return ApiResponse.<EducationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Create education successful")
                .result(educationService.createEducation(educationDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EducationDTO> updateEducation(@PathVariable Integer id, @RequestBody EducationDTO educationDTO) {
        return ApiResponse.<EducationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Update education successful")
                .result(educationService.updateEducation(id, educationDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<EducationDTO> getEducationById(@PathVariable Integer id) {
        return ApiResponse.<EducationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get education successful")
                .result(educationService.getEducationById(id))
                .build();
    }

    @GetMapping("/employee/{employeeId}")
    public ApiResponse<List<EducationDTO>> getEducationsByEmployeeId(@PathVariable Integer employeeId) {
        return ApiResponse.<List<EducationDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get educations by employee successful")
                .result(educationService.getEducationsByEmployeeId(employeeId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEducation(@PathVariable Integer id) {
        educationService.deleteEducation(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete education successful")
                .build();
    }
}
