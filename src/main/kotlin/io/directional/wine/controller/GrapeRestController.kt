package io.directional.wine.controller

import io.directional.wine.domain.Grape
import io.directional.wine.service.GrapeSearchCriteria
import io.directional.wine.service.GrapeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/grape")
class GrapeRestController(@Autowired private val grapeService: GrapeService) {


    /**포도품종 저장 또는 업데이트*/
    @PostMapping
    fun saveGrape(@RequestBody grape: Grape): ResponseEntity<Grape> {
        val savedGrape = grapeService.saveOrUpdateGrape(grape);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrape)
    }

    /** 포도품종 단일 조회*/
    @GetMapping("/{id}")
    fun getGrapeById(@PathVariable id: Long): ResponseEntity<Grape> {
        val grape = grapeService.getGrapeById(id)
        return if (grape != null) {
            ResponseEntity.ok(grape)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**포도품종 검색*/
    @GetMapping
    fun searchGrape(search: GrapeSearchCriteria): ResponseEntity<JSONData<Any>> {
        val jsonData = JSONData<Any>()
        try {
            val items = grapeService.getGrapesByCriteria(search)
            jsonData.success = true
            jsonData.data = items // 목록 조회 시 전체 리스트
            return ResponseEntity.ok(jsonData)
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    /** 포도품종 삭제*/
    @DeleteMapping("/{id}")
    fun deleteGrapeById(@PathVariable id: Long): ResponseEntity<Void> {
        grapeService.deleteGrapeById(id)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(Exception::class)
    fun errorHandler(e: Exception): ResponseEntity<JSONData<Any>> {
        val jsonData = JSONData<Any>();
        jsonData.success = false;
        jsonData.data = e.message;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonData);
    }
}