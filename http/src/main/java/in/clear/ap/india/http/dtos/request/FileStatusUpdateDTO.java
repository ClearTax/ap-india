package in.clear.ap.india.http.dtos.request;

import in.clear.ap.india.http.models.FileStatus;
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
