package gao.shortener.controllers;

import gao.shortener.dtos.ShortenUrlRequest;
import gao.shortener.dtos.ShortenUrlResponse;
import gao.shortener.entities.Resource;
import gao.shortener.repositories.ResourceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ResourcesController {

    private ResourceRepository resourceRepository;

    ResourcesController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @PostMapping("/shortener")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request, HttpServletRequest httpServletRequest) {
        var id = RandomStringUtils.randomAlphabetic(5, 10);

        this.resourceRepository.insert(new Resource(
                id,
                request.url()
        ));

        var redirectUrl = httpServletRequest.getRequestURL().toString().replace("shortener", id);

        return ResponseEntity.ok(
                ShortenUrlResponse
                        .builder()
                        .url(redirectUrl)
                        .build()
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id) {

        var url = resourceRepository.findById(id);

        if (url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getResource()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
