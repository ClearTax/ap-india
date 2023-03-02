package in.clear.ap.india.http.clients;

import in.clear.ap.india.http.config.FeignClientConfig;
import in.clear.ap.india.http.dtos.request.InvoiceCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "invoice-api-service-client", url = "${module.invoice-service.base-url}", configuration = FeignClientConfig.class)
public interface InvoiceAPIServiceClient {

    @PostMapping(value = "/integration/v1/maxItc/triggerWorkflow", produces = MediaType.APPLICATION_JSON_VALUE)
    void createInvoiceRecords(@RequestBody InvoiceCreateRequest fileDto);
}
