package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.WorkHistoryDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.WorkHistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/work-histories")
@RequiredArgsConstructor
public class WorkHistoryController {

    private final WorkHistoryService workHistoryService;

    @PostMapping
    public ApiResponse<WorkHistoryDTO> createWorkHistory(@RequestBody WorkHistoryDTO workHistoryDTO) {
        return ApiResponse.<WorkHistoryDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Create work history successful")
                .result(workHistoryService.createWorkHistory(workHistoryDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkHistoryDTO> updateWorkHistory(
            @PathVariable Integer id, @RequestBody WorkHistoryDTO workHistoryDTO) {
        return ApiResponse.<WorkHistoryDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Update work history successful")
                .result(workHistoryService.updateWorkHistory(id, workHistoryDTO))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkHistoryDTO> getWorkHistoryById(@PathVariable Integer id) {
        return ApiResponse.<WorkHistoryDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Get work history successful")
                .result(workHistoryService.getWorkHistoryById(id))
                .build();
    }

    @GetMapping("/employee/{employeeId}")
    public ApiResponse<List<WorkHistoryDTO>> getWorkHistoriesByEmployeeId(@PathVariable Integer employeeId) {
        return ApiResponse.<List<WorkHistoryDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get work histories by employee successful")
                .result(workHistoryService.getWorkHistoriesByEmployeeId(employeeId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteWorkHistory(@PathVariable Integer id) {
        workHistoryService.deleteWorkHistory(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete work history successful")
                .build();
    }
}
