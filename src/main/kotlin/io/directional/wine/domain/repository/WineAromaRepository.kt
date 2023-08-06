package io.directional.wine.domain.repository
import io.directional.wine.domain.WineAroma
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WineAromaRepository : JpaRepository<WineAroma, Long>, QuerydslPredicateExecutor<WineAroma>