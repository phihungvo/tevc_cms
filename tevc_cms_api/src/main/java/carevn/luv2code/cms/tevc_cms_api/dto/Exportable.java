package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.util.ExcelColumn;

public interface Exportable {
    List<ExcelColumn> getExcelColumns();

    List<?> getData();
}
