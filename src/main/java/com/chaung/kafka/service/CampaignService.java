package com.chaung.kafka.service;

import com.chaung.kafka.service.dto.CampaignDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Campaign.
 */
public interface CampaignService {

    /**
     * Save a campaign.
     *
     * @param campaignDTO the entity to save
     * @return the persisted entity
     */
    CampaignDTO save(CampaignDTO campaignDTO);

    /**
     * Get all the campaigns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CampaignDTO> findAll(Pageable pageable);

    /**
     * Get the "id" campaign.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CampaignDTO findOne(Long id);

    /**
     * Delete the "id" campaign.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
