package com.trendyol.stock.services;

import com.trendyol.stock.domain.Post;
import com.trendyol.stock.domain.Stock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        // set connection and read timeouts
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(500))
                .setReadTimeout(Duration.ofSeconds(500))
                .build();
    }

    // GET
    public String getPostsPlainJSON() {
        String url = "";
        return this.restTemplate.getForObject(url, String.class);
    }

    public Stock getStockAsObject() {
        String url = "";
        return this.restTemplate.getForObject(url, Stock.class);
    }

    public Stock getStockByIdAsObject(String id) {
        String url = "http://localhost:8081/v1/products/" + id;
        return this.restTemplate.getForObject(url, Stock.class);
    }

    public Post getPostWithUrlParameters() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";
        return this.restTemplate.getForObject(url, Post.class, 1);
    }

    public Post getPostWithResponseHandling() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";
        ResponseEntity<Post> response = this.restTemplate.getForEntity(url, Post.class, 1);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    //POST
    public Post createPost() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("title", "Introduction to Spring Boot");
        map.put("body", "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Post> response = this.restTemplate.postForEntity(url, entity, Post.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Post createPostWithObject() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object
        Post post = new Post(1, "Introduction to Spring Boot",
                "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");

        // build the request
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);

        // send POST request
        return restTemplate.postForObject(url, entity, Post.class);
    }

    //UPDATE
    public void updatePost() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object
        Post post = new Post(4, "New Title", "New Body");

        // build the request
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);

        // send PUT request to update post with `id` 10
        this.restTemplate.put(url, entity, 10);
    }

    public Post updatePostWithResponse() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object
        Post post = new Post(4, "New Title", "New Body");

        // build the request
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);

        // send PUT request to update post with `id` 10
        ResponseEntity<Post> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity, Post.class, 10);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    //DELETE
    public void deletePost() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // send DELETE request to delete post with `id` 10
        this.restTemplate.delete(url, 10);
    }

    //HEAD
    public HttpHeaders retrieveHeaders() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // send HEAD request
        return this.restTemplate.headForHeaders(url);
    }

    //OPTIONAL
    public Set<HttpMethod> allowedOperations() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // send HEAD request
        return this.restTemplate.optionsForAllow(url);
    }

    //ERROR HANDLING
    public String unknownRequest() {
        try {
            String url = "https://jsonplaceholder.typicode.com/404";
            return this.restTemplate.getForObject(url, String.class);
        } catch (HttpStatusCodeException ex) {
            // raw http status code e.g `404`
            System.out.println(ex.getRawStatusCode());
            // http status code e.g. `404 NOT_FOUND`
            System.out.println(ex.getStatusCode().toString());
            // get response body
            System.out.println(ex.getResponseBodyAsString());
            // get http headers
            HttpHeaders headers= ex.getResponseHeaders();
            System.out.println(headers.get("Content-Type"));
            System.out.println(headers.get("Server"));
        }

        return null;
    }
}
