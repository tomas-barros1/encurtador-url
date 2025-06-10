package com.url.shorter.repositories;

import com.url.shorter.models.ShorterUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShorterUrlRepository extends MongoRepository<ShorterUrl, UUID> {
  Optional<ShorterUrl> findByShorterUrl(String shorterUrl);
}
