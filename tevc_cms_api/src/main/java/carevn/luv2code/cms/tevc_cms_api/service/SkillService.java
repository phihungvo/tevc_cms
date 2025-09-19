package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carevn.luv2code.cms.tevc_cms_api.dto.SkillDTO;

public interface SkillService {
    SkillDTO createSkill(SkillDTO skillDTO);

    SkillDTO updateSkill(Integer id, SkillDTO skillDTO);

    SkillDTO getSkillById(Integer id);

    List<SkillDTO> getAllSkills();

    Page<SkillDTO> getAllSkillsPagined(int page, int size);

    Page<SkillDTO> getSkillsByEmployeeIdPaged(Integer employeeId, Pageable pageable);

    void deleteSkill(Integer id);
}
