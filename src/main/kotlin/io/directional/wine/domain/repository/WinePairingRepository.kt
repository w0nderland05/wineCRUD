package io.directional.wine.domain.repository

import io.directional.wine.domain.WinePairing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WinePairingRepository : JpaRepository<WinePairing, Long>, QuerydslPredicateExecutor<WinePairing>