package com.example.urlshortener;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UrlRepository extends JpaRepository<UrlDetails, Long> {
     UrlDetails findByCode(String code);
    List<UrlDetails> findAll();
    UrlDetails save(UrlDetails obj);
}
