package live.sahiwl.pocketlybe.service;

import live.sahiwl.pocketlybe.dto.LinkRequestDTO;
import live.sahiwl.pocketlybe.dto.LinkResponseDTO;
import live.sahiwl.pocketlybe.model.Link;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.LinkRepository;
import live.sahiwl.pocketlybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LinkService {

    private  final LinkRepository linkRepository;
    private  final UserRepository userRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }


    public LinkResponseDTO createLink(Long userId, String originalURL){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not found"));

        String hash = generateHash();

        Link link = Link.builder().hash(hash).user(user).build();
        linkRepository.save(link);
        return new LinkResponseDTO(link.getId(), link.getHash(), user.getId());
    }

    private String generateHash() {
        return Integer.toHexString(new Random().nextInt(999999));
    }

    public List<LinkResponseDTO> getLinksByUser(Long userId){
    return linkRepository.findByUserId(userId).stream().map(link -> new LinkResponseDTO(link.getId(), link.getHash(), link.getUser().getId())).collect(Collectors.toList());
    }

    public String deleteLink(Long linkId, Long userId){
        Link link = linkRepository.findById(linkId).orElseThrow(()-> new RuntimeException("Invalid linkId"));

        if(!link.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized to delete this link");

        linkRepository.delete(link);
       return "Link deleted!";
    }

}
