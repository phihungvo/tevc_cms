package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ChartData;
import carevn.luv2code.cms.tevc_cms_api.service.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/employees-by-department")
    public ApiResponse<ChartData> getEmployeesByDepartment(
            @RequestParam(required = false, defaultValue = "true") Boolean status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {

        ChartData data = dashboardService.getEmployeesByDepartment(status, dateFrom, dateTo);

        return ApiResponse.<ChartData>builder()
                .message("Lấy dữ liệu thành công")
                .result(data)
                .build();
    }
}
