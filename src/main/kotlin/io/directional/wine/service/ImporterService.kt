package io.directional.wine.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import io.directional.wine.domain.Importer
import io.directional.wine.domain.QImporter
import io.directional.wine.domain.repository.ImporterRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**수입사 검색 커맨드객체*/
data class ImporterSearchCriteria(
    var nameKorean: String? = null,
    var nameEnglish: String? = null,
    var sortBy: String? = null,
    var sortAscending: Boolean = true,
    var limit: Int = 0 // 조회 갯수, 0이면 제한 없음
)

@Service
class ImporterService(private val importerRepository: ImporterRepository){

    /**수입사 등록(Create) or 수정(Update)(기존등록된 수입사 있는경우 수정, 없는경우 생성등록)*/
    fun saveOrUpdateImporter(importer: Importer): Importer {
        if (importer.id != null && importerRepository.existsById(importer.id!!)) {
            return importerRepository.save(importer)
        } else {
            return importerRepository.save(importer)
        }
    }

    /**삭제(Delete)*/
    fun deleteImporterById(id: Long) {
        importerRepository.deleteById(id)
    }

    /** 단일조회(Single Read) */
    fun getImporterById(id: Long): Importer? {
        return importerRepository.findById(id).orElse(null)
    }

    /** 다수조회(Multiple Read)*/
    fun getImportersByCriteria(criteria: ImporterSearchCriteria): List<Any> {
        val predicate = buildPredicate(criteria)
        val sort = buildSort(criteria)
        val limit = criteria.limit;
        val pageable = PageRequest.of(0, limit, sort) //페이징 처리
        val importers = importerRepository.findAll(predicate!!, pageable).content
        return importers.map { importer ->
               var nameKorean = importer.nameKorean!!
               var nameEnglish = importer.nameEnglish!!
        }
    }
}

/**검색*/
private fun buildPredicate(criteria: ImporterSearchCriteria): Predicate? {
    val importerQuery = QImporter.importer

    return BooleanBuilder().apply {
        criteria.nameKorean?.let {
            and(importerQuery.nameKorean.contains(it))
        }
        criteria.nameEnglish?.let {
            and(importerQuery.nameEnglish.containsIgnoreCase(it))
        }
    }
}

/**정렬*/
private fun buildSort(criteria: ImporterSearchCriteria): Sort {
    val sortProperty = when (criteria.sortBy) {
        "nameKorean" -> "nameKorean"
        "nameEnglish" -> "nameEnglish"
        else -> "nameKorean" // 기본정렬기준
    }
    val sortDirection = if (criteria.sortAscending) Sort.Direction.ASC else Sort.Direction.DESC
    return Sort.by(sortDirection, sortProperty)
}