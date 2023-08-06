package io.directional.wine.controller

import io.directional.wine.domain.Wine
import io.directional.wine.service.WineSearchCriteria
import io.directional.wine.service.WineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/wine")
class WineRestController(@Autowired private val wineService: WineService) {

    /**와인 저장 또는 업데이트*/
    @PostMapping
    fun saveWine(@RequestBody wine: Wine): ResponseEntity<Wine> {
        val savedWine = wineService.saveOrUpdateWine(wine);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWine)
    }

    /** 와인 단일 조회*/
    @GetMapping("/{id}")
    fun getWineById(@PathVariable id: Long): ResponseEntity<Wine> {
        val wine = wineService.getWineById(id)
        return if (wine != null) {
            ResponseEntity.ok(wine)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**와인 검색*/
    @GetMapping
    fun searchWine(search: WineSearchCriteria): ResponseEntity<JSONData<Any>> {
        val jsonData = JSONData<Any>()
        try {
            val items = wineService.getWinesByCriteria(search)
            jsonData.success = true
            jsonData.data = items // 목록 조회 시 전체 리스트
            return ResponseEntity.ok(jsonData)
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    /** 와인 삭제*/
    @DeleteMapping("/{id}")
    fun deleteWineById(@PathVariable id: Long): ResponseEntity<Void> {
        wineService.deleteWineById(id)
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