package live.sahiwl.pocketlybe.repository;

import live.sahiwl.pocketlybe.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUserId(Long userId);
    Optional<Link> findByHash(String hash);

}