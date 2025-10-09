package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.Date;

import carevn.luv2code.cms.tevc_cms_api.dto.response.ChartData;

public interface DashboardService {
    ChartData getEmployeesByDepartment(Boolean status, Date dateFrom, Date dateTo);
}
