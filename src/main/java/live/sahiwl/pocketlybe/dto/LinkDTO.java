package live.sahiwl.pocketlybe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
    private Long id;
    private String hash;
    private Long userId;
}
