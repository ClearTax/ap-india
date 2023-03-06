package in.clear.ap.india.commonmodels.dtos.request;

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
public class FileStatusUpdateRequest {
    private String activityId;

    @Size(min = 1, max = 99)
    private List<FileStatusUpdateDTO> fileStatusUpdateDTOList;
}
