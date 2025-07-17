package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.catalogAndContent.ContentCreateDTO;
import org.example.ecommerce.common.dto.catalogAndContent.ContentDetailDTO;
import org.example.ecommerce.common.dto.catalogAndContent.ContentListDTO;
import org.example.ecommerce.repository.admin.ContentRepository;
import org.example.ecommerce.service.admin.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/contents")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentRepository contentRepository;

    @GetMapping("")
    public List<ContentListDTO> getAllContents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        return contentService.getFilteredContents(status, keyword);
    }


    //thêm mơí content
    @PostMapping
    public ResponseEntity<ContentDetailDTO> create(@RequestBody ContentCreateDTO dto) {
        ContentDetailDTO created = contentService.createContent(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    //xem truươớc
    @GetMapping("/{id}")
    public ResponseEntity<ContentDetailDTO> getOne(
            @PathVariable Integer id
    ) {
        return contentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    //update
    @PutMapping("/{id}")
    public ResponseEntity<ContentDetailDTO> update(
            @PathVariable Integer id,
            @RequestBody ContentCreateDTO dto
    ) {
        return contentService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = contentService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //published
    @PostMapping("/{id}/publish")
    public ResponseEntity<ContentDetailDTO> publish(
            @PathVariable Integer id,
            @RequestParam Integer adminId
    ) {
        return contentService.publish(id, adminId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
