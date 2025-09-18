package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.dto.SkillDTO;

public interface SkillService {
    SkillDTO createSkill(SkillDTO skillDTO);

    SkillDTO updateSkill(Integer id, SkillDTO skillDTO);

    SkillDTO getSkillById(Integer id);

    List<SkillDTO> getAllSkills();

    void deleteSkill(Integer id);
}
