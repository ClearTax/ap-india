package in.clear.ap.india.commonmodels.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileStatusUpdateDTO {
    private String fileId;
    private FileStatus fileStatus;
    private String ocrId;
    private String errorMessage;
}
