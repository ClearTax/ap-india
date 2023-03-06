package in.clear.ap.india.commonmodels.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ActivityStatusUpdateRequest {
    private String activityId;
    private ActivityStatus activityStatus;
    List<FileStatusUpdateDTO> fileStatuses;
}
