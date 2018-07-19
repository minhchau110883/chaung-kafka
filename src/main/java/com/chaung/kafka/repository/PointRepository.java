package com.chaung.kafka.repository;

import com.chaung.kafka.domain.Point;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Point entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointRepository extends JpaRepository<Point, Long>, JpaSpecificationExecutor<Point> {

}
