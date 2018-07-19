package com.chaung.kafka.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.chaung.kafka.domain.Campaign;
import com.chaung.kafka.domain.*; // for static metamodels
import com.chaung.kafka.repository.CampaignRepository;
import com.chaung.kafka.service.dto.CampaignCriteria;

import com.chaung.kafka.service.dto.CampaignDTO;
import com.chaung.kafka.service.mapper.CampaignMapper;

/**
 * Service for executing complex queries for Campaign entities in the database.
 * The main input is a {@link CampaignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CampaignDTO} or a {@link Page} of {@link CampaignDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CampaignQueryService extends QueryService<Campaign> {

    private final Logger log = LoggerFactory.getLogger(CampaignQueryService.class);

    private final CampaignRepository campaignRepository;

    private final CampaignMapper campaignMapper;

    public CampaignQueryService(CampaignRepository campaignRepository, CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    /**
     * Return a {@link List} of {@link CampaignDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CampaignDTO> findByCriteria(CampaignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Campaign> specification = createSpecification(criteria);
        return campaignMapper.toDto(campaignRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CampaignDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CampaignDTO> findByCriteria(CampaignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Campaign> specification = createSpecification(criteria);
        return campaignRepository.findAll(specification, page)
            .map(campaignMapper::toDto);
    }

    /**
     * Function to convert CampaignCriteria to a {@link Specification}
     */
    private Specification<Campaign> createSpecification(CampaignCriteria criteria) {
        Specification<Campaign> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Campaign_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Campaign_.name));
            }
            if (criteria.getMinAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinAmount(), Campaign_.minAmount));
            }
            if (criteria.getMaxAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxAmount(), Campaign_.maxAmount));
            }
            if (criteria.getPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoint(), Campaign_.point));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Campaign_.type));
            }
        }
        return specification;
    }

}
