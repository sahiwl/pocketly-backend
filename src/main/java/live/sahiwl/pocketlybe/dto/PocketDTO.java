package live.sahiwl.pocketlybe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PocketDTO {
    private String username;
    private List<ContentResponseDTO> content;
}
