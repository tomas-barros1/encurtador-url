package com.url.shorter.controller;

import com.url.shorter.models.ShorterUrl;
import com.url.shorter.requests.ShortUrlRequest;
import com.url.shorter.requests.ShortUrlResponse;
import com.url.shorter.services.ShorterUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

@RestController
@RequestMapping("/shorter")
public class ShorterUrlController implements ShorterUrlControllerDocs {

  private final ShorterUrlService shorterUrlService;

  public ShorterUrlController(ShorterUrlService shorterUrlService) {
    this.shorterUrlService = shorterUrlService;
  }
  @PostMapping
  public ResponseEntity<ShortUrlResponse> shortUrl(@Valid @RequestBody ShortUrlRequest shortUrlRequest) {
    ShorterUrl shorterUrl = shorterUrlService.save(shortUrlRequest);
    return ResponseEntity.ok(new ShortUrlResponse(shorterUrl.getShorterUrl(), shorterUrl.getExpirationTime()));
  }
  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirectToOriginalUrl(
        @PathVariable String shortCode,
        HttpServletRequest request
  ) {
    String redirectUrl = shorterUrlService.getRedirectUrl(shortCode, request);
    return ResponseEntity.status(302).location(URI.create(redirectUrl)).build();
  }
}