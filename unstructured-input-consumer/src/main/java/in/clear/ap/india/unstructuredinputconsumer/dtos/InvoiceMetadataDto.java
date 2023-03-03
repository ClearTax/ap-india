package in.clear.ap.india.unstructuredinputconsumer.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvoiceMetadataDto {
    private String externalReferenceId;
    private String fileName;
    private String s3Url;

    public InvoiceMetadataDto(File file){
        this.externalReferenceId= file.getId();
        this.fileName = file.getFileName();
        this.s3Url = file.getS3Url();
    }
}
