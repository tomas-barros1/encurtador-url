package com.url.shorter.services;

import com.url.shorter.exceptions.UrlExpiredException;
import com.url.shorter.exceptions.UrlNotFoundException;
import com.url.shorter.models.ShorterUrl;
import com.url.shorter.repositories.ShorterUrlRepository;
import com.url.shorter.requests.ShortUrlRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShorterUrlServiceTest {

    @InjectMocks
    private ShorterUrlService shorterUrlService;

    @Mock
    private ShorterUrlRepository shorterUrlRepository;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldCreateShortUrl() {
        String originalUrl = "https://www.google.com";
        ShortUrlRequest request = new ShortUrlRequest(originalUrl);
        
        ShorterUrl savedUrl = new ShorterUrl();
        savedUrl.setOriginalUrl(originalUrl);
        savedUrl.setShorterUrl("ABC123");
        savedUrl.setExpirationTime(LocalDateTime.now().plusDays(2));

        when(shorterUrlRepository.findByShorterUrl(any())).thenReturn(Optional.empty());
        when(shorterUrlRepository.save(any())).thenReturn(savedUrl);

        ShorterUrl result = shorterUrlService.save(request);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertNotNull(result.getShorterUrl());
        verify(shorterUrlRepository).save(any());
    }

    @Test
    void shouldRetrieveOriginalUrl() {
        String shortCode = "ABC123";
        String originalUrl = "https://www.google.com";
        
        ShorterUrl shorterUrl = new ShorterUrl();
        shorterUrl.setOriginalUrl(originalUrl);
        shorterUrl.setShorterUrl(shortCode);
        shorterUrl.setExpirationTime(LocalDateTime.now().plusDays(1));

        when(shorterUrlRepository.findByShorterUrl(shortCode)).thenReturn(Optional.of(shorterUrl));

        String result = shorterUrlService.retrieveUrl(shortCode);

        assertEquals(originalUrl, result);
    }

    @Test
    void shouldThrowExceptionWhenUrlNotFound() {
        String shortCode = "ABC123";
        when(shorterUrlRepository.findByShorterUrl(shortCode)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () ->
            shorterUrlService.retrieveUrl(shortCode)
        );
    }

    @Test
    void shouldThrowExceptionWhenUrlExpired() {
        String shortCode = "ABC123";
        ShorterUrl shorterUrl = new ShorterUrl();
        shorterUrl.setExpirationTime(LocalDateTime.now().minusDays(1));
        
        when(shorterUrlRepository.findByShorterUrl(shortCode)).thenReturn(Optional.of(shorterUrl));

        assertThrows(UrlExpiredException.class, () ->
            shorterUrlService.retrieveUrl(shortCode)
        );
    }

    @Test
    void shouldAddHttpsWhenSchemeNotPresent() {
        String shortCode = "ABC123";
        String originalUrl = "www.google.com";
        
        ShorterUrl shorterUrl = new ShorterUrl();
        shorterUrl.setOriginalUrl(originalUrl);
        shorterUrl.setExpirationTime(LocalDateTime.now().plusDays(1));
        
        when(shorterUrlRepository.findByShorterUrl(shortCode)).thenReturn(Optional.of(shorterUrl));
        when(request.getHeader("X-Forwarded-Proto")).thenReturn(null);
        when(request.getScheme()).thenReturn("https");

        String result = shorterUrlService.getRedirectUrl(shortCode, request);

        assertEquals("https://" + originalUrl, result);
    }

    @Test
    void shouldKeepExistingHttpScheme() {
        String shortCode = "ABC123";
        String originalUrl = "http://www.google.com";
        
        ShorterUrl shorterUrl = new ShorterUrl();
        shorterUrl.setOriginalUrl(originalUrl);
        shorterUrl.setExpirationTime(LocalDateTime.now().plusDays(1));
        
        when(shorterUrlRepository.findByShorterUrl(shortCode)).thenReturn(Optional.of(shorterUrl));

        String result = shorterUrlService.getRedirectUrl(shortCode, request);

        assertEquals(originalUrl, result);
    }

    @Test
    void shouldUseForwardedProto() {
        String shortCode = "ABC123";
        String originalUrl = "www.google.com";
        
        ShorterUrl shorterUrl = new ShorterUrl();
        shorterUrl.setOriginalUrl(originalUrl);
        shorterUrl.setExpirationTime(LocalDateTime.now().plusDays(1));
        
        when(shorterUrlRepository.findByShorterUrl(shortCode)).thenReturn(Optional.of(shorterUrl));
        when(request.getHeader("X-Forwarded-Proto")).thenReturn("http");

        String result = shorterUrlService.getRedirectUrl(shortCode, request);

        assertEquals("http://" + originalUrl, result);
    }
}