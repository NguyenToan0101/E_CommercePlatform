package org.example.ecommerce.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.time.Instant;
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "products")
@Data
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String shopId;

    @Field(type = FieldType.Keyword)
    private String categoryId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Integer)
    private Integer weight;

    @Field(type = FieldType.Integer)
    private Integer length;

    @Field(type = FieldType.Integer)
    private Integer width;

    @Field(type = FieldType.Integer)
    private Integer height;

    @Field(type = FieldType.Boolean)
    private Boolean useVariantShipping;

    @Field(type = FieldType.Date)
    private Instant lockedUntil;

    @CompletionField
    private Completion suggest;

    @Field(type = FieldType.Text)
    private String categoryName;

    @CompletionField
    private Completion categoryNameSuggest;



}
