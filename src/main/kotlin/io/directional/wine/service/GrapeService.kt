package io.directional.wine.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Grape
import io.directional.wine.domain.QGrape
import io.directional.wine.domain.repository.GrapeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * 포도품종 검색 커맨드 객체
 */
data class GrapeSearchCriteria(
    var region: String? = null,
    var nameKorean: String? = null,
    var nameEnglish: String? = null,
    val acidity: Int,
    val body: Int,
    val sweetness: Int,
    val tannin: Int,
    var sortBy: String? = null,
    var sortAscending: Boolean = true,
    var limit: Int = 0 // 조회 갯수, 0이면 제한 없음
)

/**
 * 포도품종 다수조회  (포도품종,지역이름)만 담은 커맨드 객체*/
data class GrapeInfo(
    val nameKorean: String,
    val nameEnglish: String,
    val region: List<String>

)

/**
 * winery CRUD service
 */
@Service
class GrapeService(private val grapeRepository: GrapeRepository) {

    /**포도품종 등록(Create) or 수정(Update)(기존등록된 포도품종 있는경우 수정, 없는경우 등록)*/
    fun saveOrUpdateGrape(grape: Grape): Grape {
        if (grape.id != null && grapeRepository.existsById(grape.id!!)) {
            return grapeRepository.save(grape)
        } else {
            return grapeRepository.save(grape)
        }
    }

    /**삭제(Delete)*/
    fun deleteGrapeById(id: Long) {
        grapeRepository.deleteById(id)
    }

    /** 단일 아이디 조회(Single Read) */
    fun getGrapeById(id: Long): Grape? {
        return grapeRepository.findById(id).orElse(null)
    }

    /** 검색조회(Multiple Read)*/
    fun getGrapesByCriteria(criteria: GrapeSearchCriteria): List<Any> {
        val predicate = buildPredicate(criteria)
        val sort = buildSort(criteria)
        val limit = criteria.limit;
        val pageable = PageRequest.of(0, limit, sort)//페이징 처리
        val grapes = grapeRepository.findAll(predicate!!, pageable).content
        return grapes.map { wine ->
            GrapeInfo(
                nameKorean = wine.nameKorean!!,
                nameEnglish = wine.nameEnglish!!,
                region = listOfNotNull(
                    wine.region?.nameKorean,
                    wine.region?.parentNameKorean,
                    wine.region?.nameEnglish,
                    wine.region?.parentNameEnglish
                ).mapNotNull { it ?: "알 수 없는 지역" }
            )
        }
    }
}

/**필터링*/
private fun buildPredicate(criteria: GrapeSearchCriteria): Predicate? {
    val grapeQuery = QGrape.grape

    return BooleanBuilder().apply {
        criteria.region?.let { region ->
            if (region.isNotBlank()) {
                and(grapeQuery.region.nameKorean.eq(region).or(grapeQuery.region.nameEnglish.eq(region)))
            }
        }
        /**이름으로 검색 S*/
        criteria.nameKorean?.let {
            and(grapeQuery.nameKorean.containsIgnoreCase(it))
        }
        criteria.nameEnglish?.let {
            and(grapeQuery.nameEnglish.containsIgnoreCase(it))
        }
        /**이름으로 검색 E*/
    }
}

/**정렬*/
private fun buildSort(criteria: GrapeSearchCriteria): Sort {
    val sortProperty = when (criteria.sortBy) {
        "nameKorean" -> "nameKorean"
        "nameEnglish" -> "nameEnglish"
        "acidity", "body", "sweetness" -> criteria.sortBy
        else -> "nameKorean" // 기본정렬기준
    }
    val sortDirection = if (criteria.sortAscending) Sort.Direction.ASC else Sort.Direction.DESC
    return Sort.by(sortDirection, sortProperty)
}
