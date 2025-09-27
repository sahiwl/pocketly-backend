package live.sahiwl.pocketlybe.service;

import live.sahiwl.pocketlybe.dto.ContentResponseDTO;
import live.sahiwl.pocketlybe.dto.LinkResponseDTO;
import live.sahiwl.pocketlybe.dto.PocketDTO;
import live.sahiwl.pocketlybe.model.Link;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.LinkRepository;
import live.sahiwl.pocketlybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private  final LinkRepository linkRepository;
    private  final UserRepository userRepository;
    private final ContentService contentService;

//    @Autowired
//    public LinkService(LinkRepository linkRepository, UserRepository userRepository) {
//        this.linkRepository = linkRepository;
//        this.userRepository = userRepository;
//    }

    @Transactional
    public LinkResponseDTO createShareLink(Long userId, boolean share){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not found"));

        if(!share){
            linkRepository.findByUserId(userId).ifPresent(linkRepository::delete);
            return null; // share disabled
        }

        // Idempotent: return existing link if present
        Link link = linkRepository.findByUserId(userId).orElse(null);

        if (link == null) {
            link = Link.builder()
                    .user(user)
                    .hash(UUID.randomUUID().toString().substring(0, 8))
                    .build();
            linkRepository.save(link);
        }

        return LinkResponseDTO.builder().id(link.getId()).hash(link.getHash()).userId(user.getId()).build();

    }

@Transactional(readOnly = true)
public PocketDTO getUserPocketBySharedHash(String hash){
        Link link = linkRepository.findByHash(hash).orElseThrow(()-> new RuntimeException("Invalid share link"));
        Long userId = link.getUser().getId();
        List<ContentResponseDTO> contents = contentService.getContentByUser(userId);
        return new PocketDTO(link.getUser().getUsername(), contents);
    }

}
