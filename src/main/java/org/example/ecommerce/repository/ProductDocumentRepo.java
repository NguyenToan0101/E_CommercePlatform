package org.example.ecommerce.repository;

import org.example.ecommerce.entity.ProductDocument;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableElasticsearchRepositories
public interface ProductDocumentRepo extends ElasticsearchRepository<ProductDocument, Long> {
    List<ProductDocument> findByNameContainingIgnoreCase(String keyword);


}
