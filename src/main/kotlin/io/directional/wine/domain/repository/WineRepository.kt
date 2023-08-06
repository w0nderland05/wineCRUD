package io.directional.wine.domain.repository

import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Wine
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WineRepository : JpaRepository<Wine, Long>, QuerydslPredicateExecutor<Wine>{

    fun findAll(predicate: Predicate?, sort: Sort): List<Wine>
}
