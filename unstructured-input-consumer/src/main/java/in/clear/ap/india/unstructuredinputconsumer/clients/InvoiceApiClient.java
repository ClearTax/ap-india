package in.clear.ap.india.unstructuredinputconsumer.clients;

import in.clear.ap.india.unstructuredinputconsumer.dtos.InvoiceCreateRequest;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        url = "/invoice",
        accept = MediaType.APPLICATION_JSON_VALUE)
public interface InvoiceApiClient {

    @PostExchange(value = "/", contentType = MediaType.APPLICATION_JSON_VALUE, accept = MediaType.APPLICATION_JSON_VALUE)
    void createInvoices(InvoiceCreateRequest invoiceCreateRequest);
}
