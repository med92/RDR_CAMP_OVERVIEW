package com.gaming.project.rdrcampaccountant.mapper;


import com.gaming.project.rdrcampaccountant.model.CampAction;
import com.gaming.project.rdrcampaccountant.model.MessagePayload;
import com.gaming.project.rdrcampaccountant.model.dto.CampActionDto;
import com.gaming.project.rdrcampaccountant.model.dto.MessagePayloadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessagePayLoadMapper {
    CampActionMapper mapper = Mappers.getMapper(CampActionMapper.class);

    @Mapping(target = "embeds", expression = "java(mapEmbeds(messagePayload.getEmbeds()))")
    MessagePayloadDto fromPojoToDto(MessagePayload messagePayload);

    default CampActionDto mapEmbeds(List<CampAction> campActions) {
        if (null == campActions || campActions.isEmpty()) {
            return null;
        }
        return mapper.fromPojoToDto(campActions.get(0));
    }
}
