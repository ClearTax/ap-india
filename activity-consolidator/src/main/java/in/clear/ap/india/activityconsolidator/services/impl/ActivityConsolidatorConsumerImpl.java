package in.clear.ap.india.activityconsolidator.services.impl;

import in.clear.ap.india.activityconsolidator.clients.APIndiaHttpClient;
import in.clear.ap.india.activityconsolidator.services.ActivityConsolidatorConsumer;
import in.clear.ap.india.activityconsolidator.util.redis.RedisService;
import in.clear.ap.india.commonmodels.dtos.request.ActivityStatus;
import in.clear.ap.india.commonmodels.dtos.request.ActivityStatusUpdateRequest;
import in.clear.ap.india.commonmodels.dtos.request.FileStatus;
import in.clear.ap.india.commonmodels.dtos.request.FileStatusUpdateDTO;
import in.clear.ap.india.commonmodels.dtos.request.RedisFileStatusValue;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityConsolidatorConsumerImpl implements ActivityConsolidatorConsumer {

    private String fileStatusKey = "ap-india-activity-file-";
    private String counterKey = "ap-india-activity-counter-";
    private String activitySize = "ap-india-activity-totalCount-";

    private final RedisService redisService;
    private final APIndiaHttpClient apIndiaHttpClient;

    @Override
    @JmsListener(destination = "${module.ocr-service.sqs.queue-url}")
    public void poll(String activityId) {
        int totalCount = Integer.parseInt(redisService.get(activitySize+activityId));
        int completedCount = Integer.parseInt(redisService.get(counterKey+activityId));
        if(completedCount>=totalCount){
            Map<String, RedisFileStatusValue> map = redisService.getAllValuesFromHash(fileStatusKey+activityId);
            List<FileStatusUpdateDTO> list = new ArrayList<>();
            int errorcount =0;
            for(Map.Entry<String,RedisFileStatusValue> entry: map.entrySet()){
                if(entry.getValue().getFileStatus().equals(FileStatus.FAILED)){
                    errorcount++;
                }
                FileStatusUpdateDTO fileStatusUpdateDTO = FileStatusUpdateDTO.builder()
                        .fileStatus(entry.getValue().getFileStatus())
                        .errorMessage(entry.getValue().getErrorMessage())
                        .fileId(entry.getKey())
                        .build();
                list.add(fileStatusUpdateDTO);
            }
            ActivityStatusUpdateRequest activityStatusUpdateRequest = ActivityStatusUpdateRequest.builder().activityId(activityId).fileStatuses(list).build();
            if(errorcount==0){
                activityStatusUpdateRequest.setActivityStatus(ActivityStatus.SUCCESSFUL);
            }else if(errorcount==totalCount){
                activityStatusUpdateRequest.setActivityStatus(ActivityStatus.FAILED);
            }else{
                activityStatusUpdateRequest.setActivityStatus(ActivityStatus.PARTIALLY_SUCCESSFUL);
            }
            apIndiaHttpClient.updateActivityStatus(activityStatusUpdateRequest);
        }

    }
}
