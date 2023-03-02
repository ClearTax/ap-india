package in.clear.ap.india.http.dtos.request;

import in.clear.ap.india.http.models.ActivityStatus;
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
