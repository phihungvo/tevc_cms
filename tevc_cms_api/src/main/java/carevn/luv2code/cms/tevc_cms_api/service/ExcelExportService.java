package carevn.luv2code.cms.tevc_cms_api.service;

import java.io.ByteArrayInputStream;

public interface ExcelExportService {
    ByteArrayInputStream exportToExcel(String entityType);
}
