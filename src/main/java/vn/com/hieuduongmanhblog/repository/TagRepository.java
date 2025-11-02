package vn.com.hieuduongmanhblog.repository;

import vn.com.hieuduongmanhblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByTagName(String tagName);

    List<Tag> findAllByTagNameIn(Set<String> tagNames);
}
