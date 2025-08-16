package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.*;

import carevn.luv2code.cms.tevc_cms_api.dto.ContractDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Contract;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(
            target = "employeeName",
            expression =
                    "java(contract.getEmployee() != null ? contract.getEmployee().getFirstName() + \" \" + contract.getEmployee().getLastName() : null)")
    ContractDTO toDTO(Contract contract);

    @Mapping(target = "employee", ignore = true)
    Contract toEntity(ContractDTO contractDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    void updateContractFromDto(ContractDTO dto, @MappingTarget Contract entity);
}
