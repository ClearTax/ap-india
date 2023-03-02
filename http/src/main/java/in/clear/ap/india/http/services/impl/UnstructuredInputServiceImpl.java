package in.clear.ap.india.http.services.impl;

import in.clear.ap.india.http.clients.InvoiceAPIServiceClient;
import in.clear.ap.india.http.dtos.request.BulkUnstructuredInputRequest;
import in.clear.ap.india.http.dtos.request.InvoiceCreateRequest;
import in.clear.ap.india.http.dtos.request.InvoiceMetadataDto;
import in.clear.ap.india.http.models.Activity;
import in.clear.ap.india.http.models.ActivityStatus;
import in.clear.ap.india.http.models.File;
import in.clear.ap.india.http.repository.ActivityRepository;
import in.clear.ap.india.http.services.UnStructuredInputService;
import in.clear.ap.india.http.util.sqs.SqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnstructuredInputServiceImpl implements UnStructuredInputService {

    private final ActivityRepository activityRepository;
    private final InvoiceAPIServiceClient invoiceAPIServiceClient;
    private final SqsService sqsService;
    @Value("${aws.sqs.queue-url}")
    private final String sqsUrl;

    @Override
    public void processUnstructuredInput(BulkUnstructuredInputRequest bulkUnstructuredInputRequest) {
        Activity activity = Activity.builder()
                .activityStatus(ActivityStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .files(bulkUnstructuredInputRequest
                        .getFileMetadataList()
                        .stream()
                        .map(el-> File.builder()
                                .fileName(el.getFileName())
                                .s3Url(el.getS3Url()).build())
                        .collect(Collectors.toList())
                ).build();
        activity = activityRepository.save(activity);
        InvoiceCreateRequest invoiceCreateRequest = InvoiceCreateRequest.builder().invoiceMetadataDtoList(activity.getFiles().stream().map(InvoiceMetadataDto::new).collect(Collectors.toList())).build();
        invoiceAPIServiceClient.createInvoiceRecords(invoiceCreateRequest);
        sqsService.publish(activity,sqsUrl);
    }
}
