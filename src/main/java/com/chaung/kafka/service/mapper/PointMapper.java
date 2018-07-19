package com.chaung.kafka.service.mapper;

import com.chaung.kafka.domain.*;
import com.chaung.kafka.service.dto.PointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Point and its DTO PointDTO.
 */
@Mapper(componentModel = "spring", uses = {CampaignMapper.class})
public interface PointMapper extends EntityMapper<PointDTO, Point> {

    @Mapping(source = "campaign.id", target = "campaignId")
    PointDTO toDto(Point point);

    @Mapping(source = "campaignId", target = "campaign")
    Point toEntity(PointDTO pointDTO);

    default Point fromId(Long id) {
        if (id == null) {
            return null;
        }
        Point point = new Point();
        point.setId(id);
        return point;
    }
}
