package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.SkillDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Skill;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.SkillMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.SkillRepository;
import carevn.luv2code.cms.tevc_cms_api.service.SkillService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillMapper skillMapper;

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        Skill skill = skillMapper.toEntity(skillDTO);
        List<Employee> employee = employeeRepository.findAllById(skillDTO.getEmployeesIds());

        skill.setEmployees(employee);
        return skillMapper.toDTO(skillRepository.save(skill));
    }

    @Override
    public SkillDTO updateSkill(Integer id, SkillDTO skillDTO) {
        Skill existingSkill =
                skillRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));

        skillMapper.updateEntityFromDTO(skillDTO, existingSkill);
        return skillMapper.toDTO(skillRepository.save(existingSkill));
    }

    @Override
    public SkillDTO getSkillById(Integer id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_FOUND));
        return skillMapper.toDTO(skill);
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream().map(skillMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteSkill(Integer id) {
        if (!skillRepository.existsById(id)) {
            throw new AppException(ErrorCode.SKILL_NOT_FOUND);
        }
        skillRepository.deleteById(id);
    }
}
