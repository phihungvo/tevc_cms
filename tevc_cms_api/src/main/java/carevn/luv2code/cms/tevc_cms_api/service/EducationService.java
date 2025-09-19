package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carevn.luv2code.cms.tevc_cms_api.dto.EducationDTO;

public interface EducationService {
    EducationDTO createEducation(EducationDTO educationDTO);

    EducationDTO updateEducation(Integer id, EducationDTO educationDTO);

    EducationDTO getEducationById(Integer id);

    List<EducationDTO> getEducationsByEmployeeId(Integer employeeId);

    Page<EducationDTO> getEducationsByEmployeeIdPaged(Integer employeeId, Pageable pageable);

    void deleteEducation(Integer id);
}
