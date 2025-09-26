package live.sahiwl.pocketlybe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkRequestDTO {
    private Long userId;
    private String originalURL;
}
