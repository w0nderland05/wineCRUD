package io.directional.wine.controller

import io.directional.wine.domain.Importer
import io.directional.wine.service.ImporterSearchCriteria
import io.directional.wine.service.ImporterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/importer")
class ImporterController(@Autowired private val importerService: ImporterService) {

    /**수입사 저장 또는 업데이트*/
    @PostMapping
    fun saveImporter(@RequestBody importer: Importer): ResponseEntity<Importer> {
        val savedRegion = importerService.saveOrUpdateImporter(importer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRegion)
    }

    /** 수입사 단일 조회*/
    @GetMapping("/{id}")
    fun getImporterById(@PathVariable id: Long): ResponseEntity<Importer> {
        val importer = importerService.getImporterById(id)
        return if (importer != null) {
            ResponseEntity.ok(importer)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**수입사 검색*/
    @GetMapping
    fun searchImporter(search: ImporterSearchCriteria): ResponseEntity<JSONData<Any>> {
        val jsonData = JSONData<Any>()
        try {
            val items = importerService.getImportersByCriteria(search)
            jsonData.success = true
            jsonData.data = items
            return ResponseEntity.ok(jsonData)
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    /** 수입사삭제*/
    @DeleteMapping("/{id}")
    fun deleteImporterById(@PathVariable id: Long): ResponseEntity<Void> {
        importerService.deleteImporterById(id)
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