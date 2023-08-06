package io.directional.wine.domain.repository

import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Winery
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WineryRepository : JpaRepository<Winery, Long>, QuerydslPredicateExecutor<Winery>{

    fun findAll(predicate: Predicate?, sort: Sort): List<Winery>
}