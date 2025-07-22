package org.example.ecommerce.service.customer.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.SuggestMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionContext;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.example.ecommerce.common.dto.customer.SearchResultDto;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.ProductDocument;
import org.example.ecommerce.entity.SearchHistory;
import org.example.ecommerce.repository.ProductDocumentRepo;
import org.example.ecommerce.repository.SearchHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductDocumentService {
    private final ProductDocumentRepo productDocumentRepo;
    private final ElasticsearchClient elasticsearchClient;
    private final SearchHistoryRepository searchHistoryRepository;

    private final Map<String, List<String>> userSearchHistory = new HashMap<>();
    private static final int MAX_HISTORY_SIZE = 10;
    public ProductDocumentService(ProductDocumentRepo productDocumentRepo, ElasticsearchClient elasticsearchClient, SearchHistoryRepository searchHistoryRepository) {
        this.productDocumentRepo = productDocumentRepo;
        this.elasticsearchClient = elasticsearchClient;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public ProductDocument toDocument(Product product) {
        ProductDocument doc = new ProductDocument();
        doc.setId(product.getId().toString());
        doc.setShopId(product.getShopid() != null ? product.getShopid().getId().toString() : null);
        doc.setCategoryId(product.getCategoryid() != null ? product.getCategoryid().getId().toString() : null);
        doc.setName(product.getName());
        doc.setDescription(product.getDescription());
        doc.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : 0.0);
        doc.setStatus(product.getStatus());
        doc.setCreatedAt(product.getCreatedat());
        doc.setWeight(product.getWeight());
        doc.setLength(product.getLength());
        doc.setWidth(product.getWidth());
        doc.setHeight(product.getHeight());
        doc.setUseVariantShipping(product.getUseVariantShipping());
        doc.setLockedUntil(product.getLockedUntil());
        doc.setSuggest(new Completion(List.of(product.getName())));
        doc.setCategoryName(product.getCategoryid().getCategoryname());
        doc.setCategoryNameSuggest(new Completion(List.of(product.getCategoryid().getCategoryname())));
        return doc;
    }

    public List<ProductDocument> search(String keyword) {
        return productDocumentRepo.findByNameContainingIgnoreCase(keyword);
    }

    public void save(ProductDocument productDocument) {
        productDocumentRepo.save(productDocument);
    }

    public List<ProductDocument> findAll() {
        Pageable pageable = PageRequest.of(0, 1000);
        return productDocumentRepo.findAll(pageable).getContent();
    }

    public void deleteAll(){
        productDocumentRepo.deleteAll();
    }

    public SearchResultDto getCombinedSearchResults(String input, String userId,boolean save) {
        try {
            if(input == null || input.trim().isEmpty()){
                return SearchResultDto.builder()
                        .suggestions((getSearchHistory(userId)))
                        .products(new ArrayList<>())
                        .isHistory(true)
                        .build();
            }

            String keyword = input.trim();
            Query   baseQuery = QueryBuilders.bool( builder -> {
                    builder.should(should -> should.match(m->
                                    m.field("categoryName")
                                            .query(keyword)
                                            .boost(5.0F)))

                            .should(should -> should.match((m ->
                            m.field("name")
                                    .query(keyword)
                                    .boost(3.0F)
                            )))
                            .should(should -> should.match(m->
                                    m.field("name")
                                            .query(keyword)
                                            .fuzziness("AUTO")
                                            .boost(2.0F)
                                    ))
                            .should(should -> should.wildcard(w -> w.field("name.keyword")
                                    .value("*"+ keyword + "*")
                                    .boost(1.5F)
                            ))
                            .should(should -> should.matchPhrase(mp -> mp.field("description")
                                    .query(keyword)
                                    .boost(1.0F)
                            ));
                builder.filter(filter -> filter.term(t->t.field("status").value("available")));
//                if(shopId != null && !shopId.isEmpty()){
//                    builder.filter(filter -> filter.term(t->t.field("shopId").value(shopId)));
//                }

                return builder;
            });
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("products")
                    .query(baseQuery)
                    .size(20)
                    .sort(sort -> sort.score(score -> score.order(SortOrder.Desc)))
                    .sort(sort -> sort.field(field -> field.field("price").order(SortOrder.Asc)))
                    .suggest(suggest -> suggest.suggesters(
                            "product-suggest",suggester -> suggester.prefix(keyword)
                                    .completion(completion -> completion.field("suggest")
                                            .skipDuplicates(true)
                                            .size(8))
//                                                    .contexts(shopId != null && !shopId.isEmpty() ?
//                                                            Map.of("shopId", List.of(CategoryQueryContext.of(c -> c.category(shopId).boost(1).prefix(true)))) : Map.of())

//                                    .contexts(shopId != null && !shopId.isEmpty() ?
//                                            Map.of("shopId",List.of(shopId)) : Map.of()))


                    )

                            .suggesters("category-suggest",suggester -> suggester
                                    .prefix(keyword)
                                    .completion(completion -> completion
                                            .field("categoryNameSuggest")
                                            .skipDuplicates(true)
                                            .size(5)))
                            .suggesters("term-suggest",
                            suggester -> suggester.text(keyword)
                                    .term(term -> term.field("name")
                                            .size(3)
                                            .suggestMode(SuggestMode.Popular)))));
            SearchResponse<ProductDocument> response = elasticsearchClient.search(searchRequest, ProductDocument.class);
            List<ProductDocument> products = (List<ProductDocument>) response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .filter(product -> "available".equalsIgnoreCase(product.getStatus())).toList();
            List<String> suggestions = new ArrayList<>();
            if(response.suggest() != null && response.suggest().get("product-suggest") != null){
                response.suggest().get("product-suggest").forEach(suggestion -> {
                    if(suggestion.isCompletion()){
                        suggestion.completion().options().forEach(option ->{
                            suggestions.add(option.text());
                        });
                    }
                });
            }
            if (response.suggest() != null && response.suggest().get("term-suggest") != null) {
                response.suggest().get("term-suggest").forEach(suggestion -> {
                    if (suggestion.isTerm()) {
                        suggestion.term().options().forEach(option -> {
                            if (!suggestions.contains(option.text())) {
                                suggestions.add(option.text());
                            }
                        });
                    }
                });
            }
            if(response.suggest() != null && response.suggest().get("category-suggest") != null){
                response.suggest().get("category-suggest").forEach(suggestion -> {
                    if(suggestion.isCompletion()){
                        suggestion.completion().options().forEach(option -> {
                            if (!suggestions.contains(option.text())) {
                                suggestions.add(option.text());
                            }
                        });
                    }
                });
            }
            products.stream()
                    .map(ProductDocument::getName)
                    .filter(name -> name != null && !suggestions.contains(name))
                    .limit(3)
                    .forEach(suggestions::add);
            if (!products.isEmpty() || !suggestions.isEmpty()) {
                addToSearchHistory(userId, keyword);
            }
            assert response.hits().total() != null;
            System.out.println("Suggest response: " + response.suggest());
            if (!products.isEmpty() || !suggestions.isEmpty()) {
                if (save) {
                    addToSearchHistory(userId, keyword);
                }
            }
            return SearchResultDto.builder()
                    .suggestions(suggestions.stream().distinct().limit(10).collect(Collectors.toList()))
                    .products(products)
                    .isHistory(false)
                    .totalHits(response.hits().total().value())
                    .build();
        } catch (Exception e) {
            System.out.println("Suggest response:  Rỗng " );
            e.printStackTrace();
            return SearchResultDto.builder()
                    .suggestions(new ArrayList<>())
                    .products(new ArrayList<>())
                    .isHistory(false)
                    .build();
        }
    }

    public List<String> getSuggestions(String prefix, String userId,boolean save,String shopId) {
        SearchResultDto result = getCombinedSearchResults(prefix, userId,save);
        return result.getSuggestions();
    }
    public List<String> getSearchHistory(String userId) {
        return searchHistoryRepository.findByUserId(userId, PageRequest.of(0, 10));
    }

    private void addToSearchHistory(String userId, String keyword) {
        // Xóa nếu đã tồn tại để tránh duplicate (giống logic cũ)
        searchHistoryRepository.deleteByUserIdAndKeyword(userId, keyword);

        // Lưu mới
        SearchHistory history = new SearchHistory();
        history.setUserId(userId);
        history.setKeyword(keyword);
        history.setSearchedAt(LocalDateTime.now());

        searchHistoryRepository.save(history);
    }

    public List<ProductDocument> searchFuzzy(String keyword, String userId) {
        try {
            // Lưu vào lịch sử tìm kiếm nếu keyword không rỗng
            if (keyword != null && !keyword.trim().isEmpty()) {
                addToSearchHistory(userId, keyword.trim());
            }

            // Tạo fuzzy search query
            Query fuzzyQuery = QueryBuilders.match(m -> m
                    .field("name")
                    .query(keyword)
                    .fuzziness("AUTO")
            );

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("products")
                    .query(fuzzyQuery)
                    .size(20) // Giới hạn số kết quả
            );

            SearchResponse<ProductDocument> response = elasticsearchClient
                    .search(searchRequest, ProductDocument.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }
    public List<ProductDocument> searchAdvanced(String keyword, String userId) {
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                addToSearchHistory(userId, keyword.trim());
            }

            // Kết hợp nhiều loại query với boost khác nhau
            Query combinedQuery = QueryBuilders.bool(b -> b
                    .should(s -> s.match(m -> m
                            .field("name")
                            .query(keyword)
                            .boost(2.0f) // Boost cao cho exact match
                    ))
                    .should(s -> s.match(m -> m
                            .field("name")
                            .query(keyword)
                            .fuzziness("AUTO")
                            .boost(1.0f) // Boost thấp hơn cho fuzzy match
                    ))
                    .should(s -> s.matchPhrase(mp -> mp
                            .field("description")
                            .query(keyword)
                            .boost(0.5f) // Boost thấp nhất cho description
                    ))
            );

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("products")
                    .query(combinedQuery)
                    .size(20)
            );

            SearchResponse<ProductDocument> response = elasticsearchClient
                    .search(searchRequest, ProductDocument.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }
    public void clearSearchHistory(String userId) {
        userSearchHistory.remove(userId);
    }

    /**
     * Xóa một item khỏi lịch sử tìm kiếm
     */
    public void removeFromHistory(String userId, String keyword) {
        List<String> history = userSearchHistory.get(userId);
        if (history != null) {
            history.remove(keyword);
        }
    }

}
