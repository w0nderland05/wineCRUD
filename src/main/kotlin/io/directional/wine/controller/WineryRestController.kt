package io.directional.wine.controller

import io.directional.wine.domain.Winery
import io.directional.wine.service.WinerySearchCriteria
import io.directional.wine.service.WineryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/winery")
class WineryRestController(@Autowired private val wineryService: WineryService) {

    /**와이너리 저장 또는 업데이트*/
    @PostMapping
    fun saveWinery(@RequestBody winery: Winery): ResponseEntity<Winery> {
        val savedWinery = wineryService.saveOrUpdateWinery(winery);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWinery)
    }

    /** 와이너리 단일 조회*/
    @GetMapping("/{id}")
    fun getWineryById(@PathVariable id: Long): ResponseEntity<Winery> {
        val winery = wineryService.getWineryById(id)
        return if (winery != null) {
            ResponseEntity.ok(winery)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**와이너리 검색*/
    @GetMapping
    fun searchWinery(search: WinerySearchCriteria): ResponseEntity<JSONData<Any>> {
        val jsonData = JSONData<Any>()
        try {
            val items = wineryService.getWineriesByCriteria(search)
            jsonData.success = true
            jsonData.data = items
            return ResponseEntity.ok(jsonData)
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    /** 와이너리 삭제*/
    @DeleteMapping("/{id}")
    fun deleteWineryById(@PathVariable id: Long): ResponseEntity<Void> {
        wineryService.deleteWineryById(id)
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