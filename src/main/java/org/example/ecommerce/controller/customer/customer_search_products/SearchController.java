package org.example.ecommerce.controller.customer.customer_search_products;

import org.example.ecommerce.common.dto.customer.SearchResultDto;
import org.example.ecommerce.entity.ProductDocument;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.SearchHistoryRepository;
import org.example.ecommerce.service.customer.search.ProductDocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")

public class SearchController {

    private final ProductRepository productRepository;
    private final ProductDocumentService productDocumentService;
    private final SearchHistoryRepository searchHistoryRepository;
    public SearchController(ProductRepository productRepository, ProductDocumentService productDocumentService, SearchHistoryRepository searchHistoryRepository) {

        this.productRepository = productRepository;
        this.productDocumentService = productDocumentService;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @GetMapping("/name")
    public ResponseEntity<List<ProductDocument>> search(@RequestParam String keyword) {
        List<ProductDocument> results = productDocumentService.search(keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/convert")
    public void convert(){
        productRepository.findAll().stream()
                .map(productDocumentService::toDocument)
                .forEach(productDocumentService::save);

    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDocument>> findAll() {
        List<ProductDocument> results = productDocumentService.findAll();
        return ResponseEntity.ok(results);
    }
    @GetMapping("/deleteAll")
    public void deleteAll() {
        productDocumentService.deleteAll();
        System.out.println("Data has been deleted"  );
    }

    @GetMapping("/combined")
    public ResponseEntity<SearchResultDto> getCombinedResults(
            @RequestParam(value = "input", defaultValue = "") String input,
            @RequestParam(value = "userId", defaultValue = "anonymous") String userId,
            @RequestParam(defaultValue = "false") boolean save,
            @RequestParam(value = "shopId", required = false) String shopId) {

        long startTime = System.currentTimeMillis();
        SearchResultDto results =productDocumentService.getCombinedSearchResults(input, userId,save);
        results.setSearchTime(System.currentTimeMillis() - startTime);

        return ResponseEntity.ok(results);
    }
    @GetMapping("/fuzzy")
    public ResponseEntity<List<ProductDocument>> searchFuzzy(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "userId", defaultValue = "anonymous") String userId,
            @RequestParam(value = "shopId", required = false) String shopId,
            @RequestParam(value = "categoryId", required = false) String categoryId) {

        List<ProductDocument> results = productDocumentService.searchFuzzy(keyword, userId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(
            @RequestParam(value = "prefix", defaultValue = "") String prefix,
            @RequestParam(value = "userId", defaultValue = "anonymous") String userId,
            @RequestParam(defaultValue = "false") boolean save,
            @RequestParam(value = "shopId", required = false) String shopId) {

        List<String> suggestions = productDocumentService.getSuggestions(prefix, userId,save,shopId);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/advanced")
    public ResponseEntity<List<ProductDocument>> searchAdvanced(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "userId", defaultValue = "anonymous") String userId) {

        List<ProductDocument> results = productDocumentService.searchAdvanced(keyword, userId);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<String>> getSearchHistory(@PathVariable String userId) {
        List<String> history = productDocumentService.getSearchHistory(userId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/history/{userId}")
    public ResponseEntity<Void> clearHistory(@PathVariable String userId) {
        searchHistoryRepository.deleteAllByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{userId}/item")
    public ResponseEntity<Void> removeHistoryItem(@PathVariable String userId,
                                                  @RequestParam String keyword) {
        searchHistoryRepository.deleteByUserIdAndKeyword(userId, keyword);
        return ResponseEntity.noContent().build();
    }

}
