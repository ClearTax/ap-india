package in.clear.ap.india.http.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PreSignedUrlGenerationRequest {
    @Size(min = 1, max = 99)
    private List<PreSignFileMetadata> fileMetadataList;
}
