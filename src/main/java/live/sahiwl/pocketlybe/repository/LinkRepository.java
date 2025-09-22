package live.sahiwl.pocketlybe.repository;

import live.sahiwl.pocketlybe.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByUserId(Long userId);
}