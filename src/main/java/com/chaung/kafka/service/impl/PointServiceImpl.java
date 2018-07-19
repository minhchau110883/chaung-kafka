package com.chaung.kafka.service.impl;

import com.chaung.kafka.service.PointService;
import com.chaung.kafka.domain.Point;
import com.chaung.kafka.repository.PointRepository;
import com.chaung.kafka.service.dto.PointDTO;
import com.chaung.kafka.service.mapper.PointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Point.
 */
@Service
@Transactional
public class PointServiceImpl implements PointService {

    private final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    private final PointRepository pointRepository;

    private final PointMapper pointMapper;

    public PointServiceImpl(PointRepository pointRepository, PointMapper pointMapper) {
        this.pointRepository = pointRepository;
        this.pointMapper = pointMapper;
    }

    /**
     * Save a point.
     *
     * @param pointDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PointDTO save(PointDTO pointDTO) {
        log.debug("Request to save Point : {}", pointDTO);
        Point point = pointMapper.toEntity(pointDTO);
        point = pointRepository.save(point);
        return pointMapper.toDto(point);
    }

    /**
     * Get all the points.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Points");
        return pointRepository.findAll(pageable)
            .map(pointMapper::toDto);
    }


    /**
     * Get one point by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PointDTO> findOne(Long id) {
        log.debug("Request to get Point : {}", id);
        return pointRepository.findById(id)
            .map(pointMapper::toDto);
    }

    /**
     * Delete the point by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Point : {}", id);
        pointRepository.deleteById(id);
    }
}
