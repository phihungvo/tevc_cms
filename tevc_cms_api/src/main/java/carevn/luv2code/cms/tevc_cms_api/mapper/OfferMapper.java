package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import carevn.luv2code.cms.tevc_cms_api.dto.OfferDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Offer;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    OfferDTO toDTO(Offer candidate);

    Offer toEntity(OfferDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOfferFromDto(OfferDTO dto, @MappingTarget Offer entity);
}
