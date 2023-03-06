package in.clear.ap.india.commonmodels.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisFileStatusValue {
    private FileStatus fileStatus;
    private String errorMessage;
}
