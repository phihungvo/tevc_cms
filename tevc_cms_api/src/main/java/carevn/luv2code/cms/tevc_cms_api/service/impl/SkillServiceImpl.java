package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (skillDTO.getEmployeesIds() != null && !skillDTO.getEmployeesIds().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(skillDTO.getEmployeesIds());
            if (employees.size() != skillDTO.getEmployeesIds().size()) {
                throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
            }

            Skill skill = skillMapper.toEntity(skillDTO);
            skill.setEmployees(employees);

            for (Employee employee : employees) {
                if (employee.getSkills() == null) {
                    employee.setSkills(new ArrayList<>());
                }
                if (!employee.getSkills().contains(skill)) {
                    employee.getSkills().add(skill);
                }
            }

            Skill savedSkill = skillRepository.save(skill);
            employeeRepository.saveAll(employees);

            return skillMapper.toDTO(savedSkill);
        } else {
            Skill skill = skillMapper.toEntity(skillDTO);
            Skill savedSkill = skillRepository.save(skill);
            return skillMapper.toDTO(savedSkill);
        }
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
    public Page<SkillDTO> getAllSkillsPagined(int page, int size) {
        Page<Skill> skillPage = skillRepository.findAll(PageRequest.of(page, size));
        return skillPage.map(skillMapper::toDTO);
    }

    @Override
    public Page<SkillDTO> getSkillsByEmployeeIdPaged(Integer employeeId, Pageable pageable) {
        return skillRepository.findByEmployeesId(employeeId, pageable).map(skillMapper::toDTO);
    }

    @Override
    public void deleteSkill(Integer id) {
        if (!skillRepository.existsById(id)) {
            throw new AppException(ErrorCode.SKILL_NOT_FOUND);
        }
        skillRepository.deleteById(id);
    }
}
