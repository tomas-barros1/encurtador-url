package com.url.shorter.requests;

import java.time.LocalDateTime;

public record ShortUrlResponse(String shortUrl, LocalDateTime expirationDateTime) {
}
