package io.directional.wine.domain.repository

import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Region
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface RegionRepository : JpaRepository<Region, Long>, QuerydslPredicateExecutor<Region>{
    fun findAll(predicate: Predicate?, sort: Sort): List<Region>

}