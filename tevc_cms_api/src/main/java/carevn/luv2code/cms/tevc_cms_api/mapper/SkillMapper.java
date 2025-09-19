package carevn.luv2code.cms.tevc_cms_api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import carevn.luv2code.cms.tevc_cms_api.dto.SkillDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Skill;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;

@Mapper(
        componentModel = "spring",
        uses = {EmployeeRepository.class})
public abstract class SkillMapper {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Mapping(target = "employeesIds", source = "id", qualifiedByName = "mapSkillIdToEmployeesIds")
    public abstract SkillDTO toDTO(Skill skill);

    @Mapping(target = "employees", ignore = true)
    public abstract Skill toEntity(SkillDTO skillDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "employees", ignore = true)
    public abstract void updateEntityFromDTO(SkillDTO skillDTO, @MappingTarget Skill skill);

    @Named("mapSkillIdToEmployeesIds")
    protected List<Integer> mapSkillIdToEmployeesIds(Integer skillId) {
        if (skillId == null) {
            return new ArrayList<>();
        }
        return employeeRepository.findEmployeeIdsBySkillId(skillId);
    }
}
