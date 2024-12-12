package gao.shortener.dtos;

import lombok.Builder;

@Builder
public record ShortenUrlResponse(String url) {
}
