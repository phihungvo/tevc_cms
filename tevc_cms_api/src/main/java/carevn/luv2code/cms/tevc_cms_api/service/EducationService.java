package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.dto.EducationDTO;

public interface EducationService {
    EducationDTO createEducation(EducationDTO educationDTO);

    EducationDTO updateEducation(Integer id, EducationDTO educationDTO);

    EducationDTO getEducationById(Integer id);

    List<EducationDTO> getEducationsByEmployeeId(Integer employeeId);

    void deleteEducation(Integer id);
}
