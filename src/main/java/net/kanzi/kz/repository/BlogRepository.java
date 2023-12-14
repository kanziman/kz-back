package net.kanzi.kz.repository;

import net.kanzi.kz.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}

