package org.example.ecommerce.service;

import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.repository.ProductimageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductimageService {

    private final EmbedService embedService;
    private final ProductimageRepository repository;

    public ProductimageService(EmbedService embedService,
                               ProductimageRepository repository) {
        this.embedService = embedService;
        this.repository = repository;
    }

    /**
     * Lấy embedding từ Python service rồi lưu vào DB cho imageId đã có sẵn.
     */
    @Transactional
    public void indexImage(Integer imageId, String imageUrl) {
        // 1. Gọi Python microservice lấy embedding
        List<Double> vec = embedService.fetchEmbedding(imageUrl);
        // 2. Chuyển List<Double> → Float[]
        Float[] floats = vec.stream()
                .map(Double::floatValue)
                .toArray(Float[]::new);
        // 3. Cập nhật entity và lưu
        Productimage img = repository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found: " + imageId));
        img.setEmbedding(floats);
        repository.save(img);
    }

    @Transactional
    public void indexAllPending() {
        List<Productimage> pendings = repository.findByEmbeddingIsNull();
        for (Productimage img : pendings) {
            indexImage(img.getId(), img.getImageurl());
        }
    }

    /**
     * Tìm top-k ảnh tương tự cho một URL image mới.
     */
    public List<SimilarImageProjection> searchSimilar(String imageUrl, int k) {
        // 1. Lấy embedding từ Python
        List<Double> vec = embedService.fetchEmbedding(imageUrl);
        Float[] floats = vec.stream()
                .map(Double::floatValue)
                .toArray(Float[]::new);
        // 2. Sinh literal "[x1,x2,...]"
        String vectorLiteral = Arrays.stream(floats)
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
        // 3. Query chỉ trả imageId, productId, imageUrl
        return repository.findTopKSimilarByLiteral(vectorLiteral, k);
    }

    /**
     * Đếm số lượng ảnh có embedding
     */
    public Long countImagesWithEmbedding() {
        return repository.countImagesWithEmbedding();
    }
}