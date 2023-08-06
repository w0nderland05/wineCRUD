package io.directional.wine.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import io.directional.wine.domain.QWinery
import io.directional.wine.domain.Winery
import io.directional.wine.domain.repository.WineryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**와이너리 검색 커맨드객체*/
data class WinerySearchCriteria(
    var region: String? = null,
    var nameKorean: String? = null,
    var nameEnglish: String? = null,
    var sortByName: String? = null,
    var sortAscending: Boolean = true,
    var limit: Int = 0 // 조회 갯수, 0이면 제한 없음
)

/**와이너리 다수조회  (와이너리 이름, 지역 이름)만 담은 커맨드 객체*/
data class WineryInfo(
    var nameKorean: String, var nameEnglish: String, var region: List<String>
)

@Service
class WineryService(private val wineryRepository: WineryRepository) {

    /**와이너리 등록(Create) or 수정(Update)(기존등록된 와이너리 있는경우 수정, 없는경우 생성등록)*/
    fun saveOrUpdateWinery(winery: Winery): Winery {
        if (winery.id != null && wineryRepository.existsById(winery.id!!)) {
            return wineryRepository.save(winery)
        } else {
            return wineryRepository.save(winery)
        }
    }

    /**삭제(Delete)*/
    fun deleteWineryById(id: Long) {
        wineryRepository.deleteById(id)
    }

    /** 단일조회(Single Read) */
    fun getWineryById(id: Long): Winery? {
        return wineryRepository.findById(id).orElse(null)
    }

    /** 다수조회(Multiple Read)*/
    fun getWineriesByCriteria(criteria: WinerySearchCriteria): List<Any> {
        val predicate = buildPredicate(criteria)
        val sort = buildSort(criteria)
        val limit = criteria.limit;
        val pageable = PageRequest.of(0, limit, sort) //페이징 처리
        val wines = wineryRepository.findAll(predicate!!, pageable).content
        return wines.map { wine ->
            WineryInfo(nameKorean = wine.nameKorean!!, nameEnglish = wine.nameEnglish!!, region = listOfNotNull(
                wine.region?.nameKorean,
                wine.region?.parentNameKorean,
                wine.region?.nameEnglish,
                wine.region?.parentNameEnglish
            ).mapNotNull { it ?: "알 수 없는 지역" })
        }
    }
}

/**필터링*/
private fun buildPredicate(criteria: WinerySearchCriteria): Predicate? {
    val wineryQuery = QWinery.winery

    return BooleanBuilder().apply {
        criteria.region?.let { region ->
            if (region.isNotBlank()) {
                and(wineryQuery.region.nameKorean.eq(region).or(wineryQuery.region.nameEnglish.eq(region)))
            }
        }
        criteria.nameKorean?.let {
            and(wineryQuery.nameKorean.containsIgnoreCase(it))
        }
        criteria.nameEnglish?.let {
            and(wineryQuery.nameEnglish.containsIgnoreCase(it))
        }
    }
}

/**정렬*/
private fun buildSort(criteria: WinerySearchCriteria): Sort {
    val sortProperty = when (criteria.sortByName) {
        "nameKorean" -> "nameKorean"
        "nameEnglish" -> "nameEnglish"
        else -> "nameKorean" // 기본정렬기준
    }
    val sortDirection = if (criteria.sortAscending) Sort.Direction.ASC else Sort.Direction.DESC
    return Sort.by(sortDirection, sortProperty)
}

