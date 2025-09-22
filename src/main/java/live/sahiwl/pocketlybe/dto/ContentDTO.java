package live.sahiwl.pocketlybe.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {
    private Long id;
    private String title;
    private String type;
    private String link;
    private Long userId;
    private List<String> tags;
}
