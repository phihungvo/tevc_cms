package carevn.luv2code.cms.tevc_cms_api.mapper;

import carevn.luv2code.cms.tevc_cms_api.dto.TrainingDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Training;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    
    @Mapping(target = "participantIds", expression = "java(training.getParticipants().stream().map(Employee::getId).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "participantCount", expression = "java(training.getParticipants().size())")
    TrainingDTO toDTO(Training training);

    @Mapping(target = "participants", ignore = true)
    Training toEntity(TrainingDTO trainingDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participants", ignore = true)
    void updateTrainingFromDto(TrainingDTO dto, @MappingTarget Training entity);
}

