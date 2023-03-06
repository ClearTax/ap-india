package in.clear.ap.india.commonmodels.dtos.request;

import in.clear.ap.india.commonmodels.annotations.RemovePreSignedInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileMetadata {
    @NotNull
    @NotBlank
    private String fileName;

    @NotNull
    @NotBlank
    @RemovePreSignedInformation
    private String s3Url;
}
