package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.catalogAndContent.ContentCreateDTO;
import org.example.ecommerce.common.dto.catalogAndContent.ContentDetailDTO;
import org.example.ecommerce.common.dto.catalogAndContent.ContentListDTO;
import org.example.ecommerce.entity.Content;
import org.example.ecommerce.entity.admin.Admin;
import org.example.ecommerce.repository.admin.AdminRepo;
import org.example.ecommerce.repository.admin.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    //Add
    public ContentDetailDTO createContent(ContentCreateDTO dto) {
        Admin admin = adminRepository.findById(dto.adminId())
                .orElseThrow(() -> new RuntimeException("Admin không tồn tại"));

        Content content = new Content();
        content.setTitle(dto.title());
        content.setSlug(dto.slug());
        content.setType(dto.type());
        content.setContent(dto.content());
        content.setStatus(dto.status());
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        content.setUpdatedBy(admin);

        Content saved = contentRepository.save(content);

        return new ContentDetailDTO(
                saved.getContentid(),
                saved.getTitle(),
                saved.getSlug(),
                saved.getType(),
                saved.getStatus(),
                saved.getContent(),
                saved.getUpdatedBy() != null ? saved.getUpdatedBy().getFullname() : "Unknown",
                saved.getUpdatedAt()
        );
    }

    //find by id
    public Optional<ContentDetailDTO> findById(int id) {
        return contentRepository.findById(id)
                .map(c -> new ContentDetailDTO(
                        c.getContentid(),
                        c.getTitle(),
                        c.getSlug(),
                        c.getType(),
                        c.getStatus(),
                        c.getContent(),
                        c.getUpdatedBy() != null ? c.getUpdatedBy().getFullname() : "Unknown",
                        c.getUpdatedAt()
                ));
    }


    //update
    public Optional<ContentDetailDTO> update(Integer id, ContentCreateDTO dto) {
        Admin admin = adminRepository.findById(dto.adminId())
                .orElseThrow(() -> new RuntimeException("Admin không tồn tại"));
        return contentRepository.findById(id)
                .map(entity -> {
                    // gán lại các trường
                    entity.setTitle(dto.title());
                    entity.setSlug(dto.slug());
                    entity.setType(dto.type());
                    entity.setStatus(dto.status());
                    entity.setContent(dto.content());
                    entity.setUpdatedAt(LocalDateTime.now());
                    entity.setUpdatedBy(admin);

                    Content saved = contentRepository.save(entity);
                    return new ContentDetailDTO(
                            saved.getContentid(),
                            saved.getTitle(),
                            saved.getSlug(),
                            saved.getType(),
                            saved.getStatus(),
                            saved.getContent(),
                            saved.getUpdatedBy() != null ? saved.getUpdatedBy().getFullname() : "Unknown",
                            saved.getUpdatedAt()
                    );
                });
    }

    //delete
    public boolean delete(Integer id) {
        if (!contentRepository.existsById(id)) return false;
        contentRepository.deleteByContentid(id);
        return true;
    }

    //published
    public Optional<ContentDetailDTO> publish(Integer id, Integer adminId) {
        return contentRepository.findById(id).map(c -> {
            // 1) cập nhật status và thời gian
            contentRepository.updateStatus(id, "published", LocalDateTime.now());
            // 3) lấy lại entity và trả về DTO
            Content updated = contentRepository.findById(id).get();
            return new ContentDetailDTO(
                    updated.getContentid(),
                    updated.getTitle(),
                    updated.getSlug(),
                    updated.getType(),
                    updated.getStatus(),
                    updated.getContent(),
                    updated.getUpdatedBy() != null ? updated.getUpdatedBy().getFullname() : "Unknown",
                    updated.getUpdatedAt()
            );
        });
    }

    //
    public List<ContentDetailDTO> getPoliciesAndGuides() {
        List<Content> list = contentRepository.findByTypeIn(List.of("policy", "guide","news"));
        return list.stream()
                .map(c -> new ContentDetailDTO(
                        c.getContentid(),
                        c.getTitle(),
                        c.getSlug(),
                        c.getType(),
                        c.getStatus(),
                        c.getContent(),
                        c.getUpdatedBy() != null ? c.getUpdatedBy().getFullname() : "Unknown",
                        c.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

}
