package com.url.shorter.controller;

import com.url.shorter.models.ShorterUrl;
import com.url.shorter.requests.ShortUrlRequest;
import com.url.shorter.requests.ShortUrlResponse;
import com.url.shorter.services.ShorterUrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShorterUrlControllerTest {

    @InjectMocks
    private ShorterUrlController shorterUrlController;

    @Mock
    private ShorterUrlService shorterUrlService;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldCreateShortUrl() {
        ShortUrlRequest shortUrlRequest = new ShortUrlRequest("https://www.google.com");
        ShorterUrl shorterUrl = new ShorterUrl();
        shorterUrl.setShorterUrl("ABC123");
        shorterUrl.setExpirationTime(LocalDateTime.now().plusDays(2));
        
        when(shorterUrlService.save(any())).thenReturn(shorterUrl);

        ResponseEntity<ShortUrlResponse> response = shorterUrlController.shortUrl(shortUrlRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ABC123", response.getBody().shortUrl());
        assertNotNull(response.getBody().expirationDateTime());
    }

    @Test
    void shouldRedirectToOriginalUrl() {
        String shortCode = "ABC123";
        String redirectUrl = "https://www.google.com";
        
        when(shorterUrlService.getRedirectUrl(shortCode, request)).thenReturn(redirectUrl);

        ResponseEntity<Void> response = shorterUrlController.redirectToOriginalUrl(shortCode, request);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(URI.create(redirectUrl), response.getHeaders().getLocation());
    }
}
