package live.sahiwl.pocketlybe.dto;

import lombok.*;

import java.util.List;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequestDTO {
    private String title;
    private String type;
    private String link; // raw URL string
    private Long userId; // TODO: replace with auth token’s userId in Phase 8
    private List<Long> tagIds; // optional
}
