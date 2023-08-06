package io.directional.wine.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import io.directional.wine.domain.QRegion
import io.directional.wine.domain.Region
import io.directional.wine.domain.repository.RegionRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * 지역 검색 커맨드 객체
 */
data class RegionSearchCriteria(
    var nameKorean: String? = null,
    var nameEnglish: String? = null,
    var parentNameKorean: String? = null,
    var parentNameEnglish: String? = null,
    var sortBy: String? = null,
    var sortAscending: Boolean = true,
    var limit: Int = 0 // 조회 갯수, 0이면 제한 없음
)

/**
 * 지역다수조회  (지역이름)만 담은 커맨드 객체*/
data class RegionInfo(
    val nameKorean: String,
    val nameEnglish: String,
)

@Service
class RegionService(private val regionRepository: RegionRepository) {
    /**지역 등록(Create) or 수정(Update)(기존등록된 포도품종 있는경우 수정, 없는경우 등록)*/
    fun saveOrUpdateRegion(region: Region): Region {
        if (region.id != null && regionRepository.existsById(region.id!!)) {
            return regionRepository.save(region)
        } else {
            return regionRepository.save(region)
        }
    }

    /**삭제(Delete)*/
    fun deleteRegionById(id: Long) {
        regionRepository.deleteById(id)
    }

    /** 단일 아이디 조회(Single Read) */
    fun getRegionById(id: Long): Region? {
        return regionRepository.findById(id).orElse(null)
    }

    /** 검색조회(Multiple Read)*/
    fun getRegionByCriteria(criteria: RegionSearchCriteria): List<Any> {
        val predicate = buildPredicate(criteria)
        val sort = buildSort(criteria)
        val limit = criteria.limit;
            val pageable = PageRequest.of(0, limit, sort)
            val regions = regionRepository.findAll(predicate!!, pageable).content
            return regions.map { wine ->
                RegionInfo(
                    nameKorean = wine.nameKorean!!,
                    nameEnglish = wine.nameEnglish!!
                )
            }
    }
}

/**필터링*/
private fun buildPredicate(criteria: RegionSearchCriteria): Predicate? {
    val regionQuery = QRegion.region

    return BooleanBuilder().apply {
        criteria.parentNameKorean?.let {
            and(regionQuery.parentRegion.nameKorean.eq(it))
        }

        criteria.parentNameEnglish?.let {
            and(regionQuery.parentRegion.nameEnglish.eq(it))
        }

        /**이름으로 검색 S*/
        criteria.nameKorean?.let {
            and(regionQuery.nameKorean.containsIgnoreCase(it))
        }
        criteria.nameEnglish?.let {
            and(regionQuery.nameEnglish.containsIgnoreCase(it))
        }
        /**이름으로 검색 E*/
    }
}

/**정렬*/
private fun buildSort(criteria: RegionSearchCriteria): Sort {
    val sortProperty = when (criteria.sortBy) {
        "nameKorean" -> "nameKorean"
        "nameEnglish" -> "nameEnglish"
        "parentRegion"-> "parentRegion"
        else -> "nameKorean" // 기본정렬기준
    }
    val sortDirection = if (criteria.sortAscending) Sort.Direction.ASC else Sort.Direction.DESC
    return Sort.by(sortDirection, sortProperty)
}
