package io.directional.wine.controller

import io.directional.wine.domain.Region
import io.directional.wine.service.RegionSearchCriteria
import io.directional.wine.service.RegionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/region")
class RegionRestController(@Autowired private val regionService: RegionService) {

    /**지역 저장 또는 업데이트*/
    @PostMapping
    fun saveRegion(@RequestBody region: Region): ResponseEntity<Region> {
        val savedRegion = regionService.saveOrUpdateRegion(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRegion)
    }

    /** 지역 단일 조회*/
    @GetMapping("/{id}")
    fun getRegionById(@PathVariable id: Long): ResponseEntity<Region> {
        val region = regionService.getRegionById(id)
        return if (region != null) {
            ResponseEntity.ok(region)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**지역 검색*/
    @GetMapping
    fun searchWinery(search: RegionSearchCriteria): ResponseEntity<JSONData<Any>> {
        val jsonData = JSONData<Any>()
        try {
            val items = regionService.getRegionByCriteria(search)
            jsonData.success = true
            jsonData.data = items
            return ResponseEntity.ok(jsonData)
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    /** 지역삭제*/
    @DeleteMapping("/{id}")
    fun deleteRegionById(@PathVariable id: Long): ResponseEntity<Void> {
        regionService.deleteRegionById(id)
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