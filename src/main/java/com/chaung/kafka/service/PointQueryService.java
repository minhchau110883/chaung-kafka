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

import com.chaung.kafka.domain.Point;
import com.chaung.kafka.domain.*; // for static metamodels
import com.chaung.kafka.repository.PointRepository;
import com.chaung.kafka.service.dto.PointCriteria;

import com.chaung.kafka.service.dto.PointDTO;
import com.chaung.kafka.service.mapper.PointMapper;

/**
 * Service for executing complex queries for Point entities in the database.
 * The main input is a {@link PointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PointDTO} or a {@link Page} of {@link PointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PointQueryService extends QueryService<Point> {

    private final Logger log = LoggerFactory.getLogger(PointQueryService.class);

    private final PointRepository pointRepository;

    private final PointMapper pointMapper;

    public PointQueryService(PointRepository pointRepository, PointMapper pointMapper) {
        this.pointRepository = pointRepository;
        this.pointMapper = pointMapper;
    }

    /**
     * Return a {@link List} of {@link PointDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PointDTO> findByCriteria(PointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Point> specification = createSpecification(criteria);
        return pointMapper.toDto(pointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PointDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PointDTO> findByCriteria(PointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Point> specification = createSpecification(criteria);
        return pointRepository.findAll(specification, page)
            .map(pointMapper::toDto);
    }

    /**
     * Function to convert PointCriteria to a {@link Specification}
     */
    private Specification<Point> createSpecification(PointCriteria criteria) {
        Specification<Point> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Point_.id));
            }
            if (criteria.getOutletId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOutletId(), Point_.outletId));
            }
            if (criteria.getTerminalId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTerminalId(), Point_.terminalId));
            }
            if (criteria.getMerchantId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMerchantId(), Point_.merchantId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Point_.userId));
            }
            if (criteria.getCampaignId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCampaignId(), Point_.campaignId));
            }
            if (criteria.getPaymentTransaction() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentTransaction(), Point_.paymentTransaction));
            }
            if (criteria.getPaymentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentType(), Point_.paymentType));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Point_.amount));
            }
            if (criteria.getPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoint(), Point_.point));
            }
            if (criteria.getCampaignId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCampaignId(), Point_.campaign, Campaign_.id));
            }
        }
        return specification;
    }

}
