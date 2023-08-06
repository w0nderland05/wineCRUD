package io.directional.wine.domain.repository

import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Importer
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor


interface ImporterRepository : JpaRepository<Importer, Long>, QuerydslPredicateExecutor<Importer> {

    fun findAll(predicate: Predicate?, sort: Sort): List<Importer>
}
