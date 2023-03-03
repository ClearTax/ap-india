package in.clear.ap.india.unstructuredinputconsumer.services.impl;

import in.clear.ap.india.unstructuredinputconsumer.clients.InvoiceApiClient;
import in.clear.ap.india.unstructuredinputconsumer.dtos.Activity;
import in.clear.ap.india.unstructuredinputconsumer.dtos.InvoiceCreateRequest;
import in.clear.ap.india.unstructuredinputconsumer.dtos.InvoiceMetadataDto;
import in.clear.ap.india.unstructuredinputconsumer.services.UnstructuredInputListener;
import in.clear.ap.india.unstructuredinputconsumer.util.sqs.SqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnstructuredInputListenerImpl implements UnstructuredInputListener {

    private final InvoiceApiClient invoiceApiClient;
    private final SqsService sqsService;

    @Value("${module.ocr-service.sqs.queue-url}")
    private String OcrQueueUrl;

    @Override
    @JmsListener(destination = "${module.ap-india-http.sqs-config.queue}")
    public void poll(Activity activity) {
        InvoiceCreateRequest invoiceCreateRequest = InvoiceCreateRequest.builder()
                .invoiceMetadataDtoList(activity.getFiles().stream().map(el-> InvoiceMetadataDto.builder()
                .fileName(el.getName())
                .externalReferenceId(activity.getId())
                                .build())
                .collect(Collectors.toList()))
                .build();
        invoiceApiClient.createInvoices(invoiceCreateRequest);
        sqsService.publish(activity,OcrQueueUrl);

    }
}
