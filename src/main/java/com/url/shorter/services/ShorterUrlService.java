package com.url.shorter.services;

import com.url.shorter.models.ShorterUrl;
import com.url.shorter.repositories.ShorterUrlRepository;
import com.url.shorter.requests.ShortUrlRequest;
import com.url.shorter.exceptions.UrlNotFoundException;
import com.url.shorter.exceptions.UrlExpiredException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Random;

@Service
public class ShorterUrlService {

  private final ShorterUrlRepository shorterUrlRepository;

  public ShorterUrlService(ShorterUrlRepository shorterUrlRepository) {
    this.shorterUrlRepository = shorterUrlRepository;
  }
  private static final String ALLOWED_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
  private static final int CODE_LENGTH = 6;
  private static final Random RANDOM = new Random();

  private String generateShortCode() {
    StringBuilder shortCode = new StringBuilder(CODE_LENGTH);
    for (int i = 0; i < CODE_LENGTH; i++) {
      shortCode.append(ALLOWED_CHARS.charAt(RANDOM.nextInt(ALLOWED_CHARS.length())));
    }
    return shortCode.toString();
  }

  public ShorterUrl save(ShortUrlRequest request) {
    String shortUrl;

    do {
      shortUrl = generateShortCode();
    } while (shorterUrlRepository.findByShorterUrl(shortUrl).isPresent());

    ShorterUrl shorterUrl = new ShorterUrl();
    shorterUrl.setShorterUrl(shortUrl);
    shorterUrl.setOriginalUrl(request.url());

    shorterUrlRepository.save(shorterUrl);

    return shorterUrl;
  }
  public String retrieveUrl(String shorterUrl) {
    ShorterUrl shorterUrlModel = shorterUrlRepository.findByShorterUrl(shorterUrl)
        .orElseThrow(() -> new UrlNotFoundException("URL encurtada não encontrada"));
    
    if (shorterUrlModel.getExpirationTime().isBefore(LocalDateTime.now())) {
      throw new UrlExpiredException("URL expirada");
    }

    return shorterUrlModel.getOriginalUrl();
  }
  public String getRedirectUrl(String shortCode, HttpServletRequest request) {
    String originalUrl = retrieveUrl(shortCode);

    if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
      String scheme = request.getHeader("X-Forwarded-Proto");
      if (scheme == null) {
        scheme = request.getScheme();
      }
      originalUrl = scheme + "://" + originalUrl;
    }

    return originalUrl;
  }

}
