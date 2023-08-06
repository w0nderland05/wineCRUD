package io.directional.wine.domain.repository

import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Grape
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface GrapeRepository : JpaRepository<Grape, Long>, QuerydslPredicateExecutor<Grape>{
    fun findAll(predicate: Predicate?, sort: Sort): List<Grape>
}