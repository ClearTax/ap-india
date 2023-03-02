package in.clear.ap.india.http.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvoiceCreateRequest {
    private List<InvoiceMetadataDto> invoiceMetadataDtoList;
}
