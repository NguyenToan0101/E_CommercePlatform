package org.example.ecommerce.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.ecommerce.service.ProductimageService;
import org.example.ecommerce.service.SimilarImageProjection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ProductimageController {

    private final ProductimageService service;
    private final Cloudinary cloudinary;

    public ProductimageController(ProductimageService service, Cloudinary cloudinary) {
        this.service = service;
        this.cloudinary = cloudinary;
    }

    /**
     * Lưu embedding cho imageId đã có sẵn
     * Ví dụ: POST /api/images/index?imageId=27&url=...
     */
    @PostMapping("/index")
    public void indexImage(
            @RequestParam Integer imageId,
            @RequestParam String url
    ) {
        service.indexImage(imageId, url);
    }

    /**
     * Tìm top-k ảnh tương tự
     * GET  /api/images/search?url=...&k=5
     */
    @GetMapping("/search")
    public List<SimilarImageProjection> search(
            @RequestParam String url,
            @RequestParam(defaultValue = "5") int k) {
        return service.searchSimilar(url, k);
    }


    @PostMapping("/index-all")
    public void indexAll() {
        service.indexAllPending();
    }

    @GetMapping("/count-embedding")
    public Long countImagesWithEmbedding() {
        return service.countImagesWithEmbedding();
    }

    @PostMapping(
            value = "/search-upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public List<SimilarImageProjection> searchByUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "k", defaultValue = "5") int k
    ) throws Exception {
        // 1) Upload file lên Cloudinary
        Map<?,?> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());
        String url = uploadResult.get("secure_url").toString();

        // 2) Gọi service tìm ảnh tương tự
        return service.searchSimilar(url, k);
    }

}