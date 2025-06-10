package com.url.shorter.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document
public class ShorterUrl {

  @Id
  public String id;

  @Indexed(expireAfter = "2d")
  private LocalDateTime createdAt = LocalDateTime.now();

  @Indexed(expireAfter = "0s")
  private LocalDateTime expirationTime = LocalDateTime.now().plusDays(2);

  public String originalUrl;

  public String shorterUrl;

  public ShorterUrl() {
  }

  public ShorterUrl(String id, LocalDateTime createdAt, LocalDateTime expirationTime, String originalUrl, String shorterUrl) {
    this.id = id;
    this.createdAt = createdAt;
    this.expirationTime = expirationTime;
    this.originalUrl = originalUrl;
    this.shorterUrl = shorterUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(LocalDateTime expirationTime) {
    this.expirationTime = expirationTime;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getShorterUrl() {
    return shorterUrl;
  }

  public void setShorterUrl(String shorterUrl) {
    this.shorterUrl = shorterUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ShorterUrl that = (ShorterUrl) o;
    return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) && Objects.equals(expirationTime, that.expirationTime) && Objects.equals(originalUrl, that.originalUrl) && Objects.equals(shorterUrl, that.shorterUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, expirationTime, originalUrl, shorterUrl);
  }
}
