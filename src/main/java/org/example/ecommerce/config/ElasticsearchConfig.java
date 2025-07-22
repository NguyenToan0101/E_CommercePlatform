package org.example.ecommerce.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Create custom ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Create mapper for ES client
        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(mapper);

        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        // Create the transport with the custom mapper
        RestClientTransport transport = new RestClientTransport(restClient, jsonpMapper);

        // Create the high-level client
        return new ElasticsearchClient(transport);
    }
}

