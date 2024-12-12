package gao.shortener.dtos;

import lombok.*;

@Builder
public record ShortenUrlRequest(String url) {
}
