package in.clear.ap.india.http.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PreSignFileMetadata {
    @NotNull
    private String name;

    @NotNull
    private FileType fileType;

    @NotNull
    @Max(value = (int)1e+7)
    private long sizeInBytes;
}
