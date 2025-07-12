package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.catalogAndContent.ContentCreateDTO;
import org.example.ecommerce.common.dto.catalogAndContent.ContentListDTO;
import org.example.ecommerce.entity.Content;
import org.example.ecommerce.entity.admin.Admin;
import org.example.ecommerce.repository.admin.AdminRepo;
import org.example.ecommerce.repository.admin.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private AdminRepo adminRepository;

    public List<ContentListDTO> getFilteredContents(String status, String keyword) {
        List<ContentListDTO> result = contentRepository.findFilteredContentList(status, keyword);
        return result;
    }


    public void createContent(ContentCreateDTO dto) {
        Admin admin = adminRepository.findById(dto.adminId())
                .orElseThrow(() -> new RuntimeException("Admin không tồn tại"));

        Content content = new Content();
        content.setTitle(dto.title());
        content.setSlug(dto.slug());
        content.setType(dto.type());
        content.setContent(dto.body());
        content.setStatus(dto.status());
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        content.setUpdatedBy(admin);

        contentRepository.save(content);
    }


}
