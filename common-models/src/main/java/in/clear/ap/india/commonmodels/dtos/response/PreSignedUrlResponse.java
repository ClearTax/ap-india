package in.clear.ap.india.commonmodels.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PreSignedUrlResponse {
    private Map<String,String> preSignedUrls;
}
