package io.directional.wine.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import io.directional.wine.domain.QWine
import io.directional.wine.domain.Wine
import io.directional.wine.domain.repository.WineRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * 와인 검색 커맨드 객체
 */
data class WineSearchCriteria(
    var type: String? = null,
    var nameKorean: String? = null,
    var nameEnglish: String? = null,
    var minAlcohol: Double? = null,
    var maxAlcohol: Double? = null,
    var minPrice: Int? = null,
    var maxPrice: Int? = null,
    var style: String? = null,
    var rating: String? = null,
    var region: String? = null,
    var sortBy: String? = null,
    var sortAscending: Boolean = true,
    var limit: Int = 0 // 조회 갯수, 0이면 제한 없음
)

/**
 * 와인 다수조회  (와인의 종류, 와인 이름, 최상위 지역 이름)만 담은 커맨드 객체*/
data class WineInfo(
    val type: String,
    val nameKorean: String,
    val nameEnglish: String,
    val region: List<String>
)

/**
 * wine CRUD service
 */

@Service
class WineService(private val wineRepository: WineRepository) {

    /**와인 등록(Create) or 수정(Update)(기존등록된 와인 있는경우 수정, 없는경우 등록)*/
    fun saveOrUpdateWine(wine: Wine): Wine {
        if (wine.id != null && wineRepository.existsById(wine.id!!)) {
            return wineRepository.save(wine)
        } else {
            return wineRepository.save(wine)
        }
    }

    /**삭제(Delete)*/
    fun deleteWineById(id: Long) {
        wineRepository.deleteById(id)
    }

    /** 단일아이디조회(Single Read) */
    fun getWineById(id: Long): Wine? {
        return wineRepository.findById(id).orElse(null)
    }

    /** 검색조회(Multiple Read)*/
    fun getWinesByCriteria(criteria: WineSearchCriteria): List<Any> {
        val predicate = buildPredicate(criteria)
        val sort = buildSort(criteria)
        val limit = criteria.limit;
        val pageable = PageRequest.of(0, limit, sort)//페이징처리
        val wines = wineRepository.findAll(predicate!!, pageable).content
        return wines.map { wine ->
            WineInfo(
                type = wine.type,
                nameKorean = wine.nameKorean!!,
                nameEnglish = wine.nameEnglish!!,
                region = listOfNotNull(
                    wine.region?.parentNameKorean,
                    wine.region?.parentNameEnglish
                ).mapNotNull { it ?: "알 수 없는 지역" }
            )
        }
    }
}

/**필터링*/
private fun buildPredicate(criteria: WineSearchCriteria): Predicate? {
    val wineQuery = QWine.wine

    return BooleanBuilder().apply {
        criteria.type?.let { and(wineQuery.type.eq(it)) }
        criteria.minAlcohol?.let { and(wineQuery.alcohol.goe(it)) }
        criteria.maxAlcohol?.let { and(wineQuery.alcohol.loe(it)) }
        criteria.minPrice?.let { and(wineQuery.price.goe(it)) }
        criteria.maxPrice?.let { and(wineQuery.price.loe(it)) }
        criteria.style?.let { and(wineQuery.style.eq(it)) }
        criteria.rating?.let { and(wineQuery.grade.eq(it)) }
        criteria.region?.let { region ->
            if (region.isNotBlank()) {
                and(wineQuery.region.nameKorean.eq(region).or(wineQuery.region.nameEnglish.eq(region)))
            }
        }
        /**이름으로 검색 S*/
        criteria.nameKorean?.let {
            and(wineQuery.nameKorean.containsIgnoreCase(it))
        }
        criteria.nameEnglish?.let {
            and(wineQuery.nameEnglish.containsIgnoreCase(it))
        }
        /**이름으로 검색 E*/
    }
}

/**정렬*/
private fun buildSort(criteria: WineSearchCriteria): Sort {
    val sortProperty = when (criteria.sortBy) {
        "nameKorean" -> "nameKorean"
        "nameEnglish" -> "nameEnglish"
        "alcohol", "acidity", "body", "sweetness", "tannin", "score", "price" -> criteria.sortBy
        else -> "nameKorean" // 기본정렬기준
    }
    val sortDirection = if (criteria.sortAscending) Sort.Direction.ASC else Sort.Direction.DESC
    return Sort.by(sortDirection, sortProperty)
}















