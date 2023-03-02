package in.clear.ap.india.http.services.impl;

import in.clear.ap.india.http.clients.InvoiceAPIServiceClient;
import in.clear.ap.india.http.dtos.request.ActivityStatusUpdateRequest;
import in.clear.ap.india.http.dtos.request.BulkUnstructuredInputRequest;
import in.clear.ap.india.http.dtos.request.FileStatusUpdateDTO;
import in.clear.ap.india.http.models.Activity;
import in.clear.ap.india.http.models.ActivityStatus;
import in.clear.ap.india.http.models.File;
import in.clear.ap.india.http.models.FileStatus;
import in.clear.ap.india.http.redis.RedisService;
import in.clear.ap.india.http.repository.ActivityRepository;
import in.clear.ap.india.http.services.ActivityService;
import in.clear.ap.india.http.util.sqs.SqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final InvoiceAPIServiceClient invoiceAPIServiceClient;
    private final SqsService sqsService;
    @Value("${aws.sqs.queue-url}")
    private String sqsUrl;
    private final RedisService redisService;

    private String fileStatusKey = "ap-india-activity-file-";
    private String counterKey = "ap-india-activity-counter-";
    private String activitySize = "ap-india-activity-totalCount-";

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
        fileStatusKey = fileStatusKey+activity.getId()+"-";
        for(File file : activity.getFiles()){
            String key = fileStatusKey+file.getId();
            redisService.save(key,FileStatus.PROCESSING.toString(),60);
        }
        redisService.save(activitySize+activity.getId(),String.valueOf(activity.getFiles().size()),60);
        redisService.save(counterKey+activity.getId(),String.valueOf(0),60);
        activity = activityRepository.save(activity);
        sqsService.publish(activity,sqsUrl);
    }

    @Override
    public void expireUncompletedActivities() {
        List<ActivityStatus> activityStatuses = new ArrayList<>();
        activityStatuses.add(ActivityStatus.PARTIALLY_SUCCESSFUL);
        activityStatuses.add(ActivityStatus.SUCCESSFUL);
        activityStatuses.add(ActivityStatus.FAILED);
        LocalDateTime expiryDate =  LocalDateTime.now().minus(40, ChronoUnit.MINUTES);
        List<Activity> expiredActivities = activityRepository.findByCreatedAtBeforeAndActivityStatusNotIn(activityStatuses,expiryDate);
        expiredActivities.forEach(activity->{
            activity.setActivityStatus(ActivityStatus.FAILED);
            activity.setUpdatedAt(LocalDateTime.now());
            activity.getFiles().forEach(file->file.setFileStatus(FileStatus.FAILED));
        });
        activityRepository.saveAll(expiredActivities);
    }

    @Override
    public void updateActivityStatus(ActivityStatusUpdateRequest activityStatusUpdateRequest) {
        Activity activity = activityRepository.findById(activityStatusUpdateRequest.getActivityId()).orElse(null);
        if(activity==null){
            // TODO: throw bad request exception
        }
        activity.setActivityStatus(activityStatusUpdateRequest.getActivityStatus());
        activity.setUpdatedAt(LocalDateTime.now());
        Map<String, FileStatusUpdateDTO> fileStatusUpdateDTOMap = activityStatusUpdateRequest.getFileStatuses().stream().collect(Collectors.toMap(FileStatusUpdateDTO::getFileId, el->el));
        for(File file: activity.getFiles()){
            FileStatusUpdateDTO fileStatusUpdateDTO = fileStatusUpdateDTOMap.get(file.getId());
            file.setFileStatus(fileStatusUpdateDTO.getFileStatus());
            file.setOcrId(fileStatusUpdateDTO.getOcrId());
            file.setErrorMessage(fileStatusUpdateDTO.getErrorMessage());
        }
        activityRepository.save(activity);
    }
}
