package live.sahiwl.pocketlybe.repository;

import live.sahiwl.pocketlybe.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByUserId(Long userId);
}
