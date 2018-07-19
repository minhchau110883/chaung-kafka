package com.chaung.kafka.service.mapper;

import com.chaung.kafka.domain.*;
import com.chaung.kafka.service.dto.CampaignDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Campaign and its DTO CampaignDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CampaignMapper extends EntityMapper<CampaignDTO, Campaign> {



    default Campaign fromId(Long id) {
        if (id == null) {
            return null;
        }
        Campaign campaign = new Campaign();
        campaign.setId(id);
        return campaign;
    }
}
