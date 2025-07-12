package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.catalogAndContent.ContentCreateDTO;
import org.example.ecommerce.common.dto.catalogAndContent.ContentListDTO;
import org.example.ecommerce.service.admin.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/contents")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("")
    public List<ContentListDTO> getAllContents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        return contentService.getFilteredContents(status, keyword);
    }


    @PostMapping("")
    public ResponseEntity<?> createContent(@RequestBody ContentCreateDTO dto) {
        contentService.createContent(dto);
        return ResponseEntity.ok().build();
    }



}
