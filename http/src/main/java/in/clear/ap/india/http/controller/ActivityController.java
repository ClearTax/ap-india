package in.clear.ap.india.http.controller;


import in.clear.ap.india.commonmodels.dtos.request.ActivityStatusUpdateRequest;
import in.clear.ap.india.commonmodels.dtos.request.BulkUnstructuredInputRequest;
import in.clear.ap.india.http.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/public/unstructured/v1/")
@EnableFeignClients
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateActivity(@RequestBody BulkUnstructuredInputRequest bulkUnstructuredInputRequest){
        activityService.processUnstructuredInput(bulkUnstructuredInputRequest);
    }

    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateActivity(@RequestBody ActivityStatusUpdateRequest activityStatusUpdateRequest){
        activityService.updateActivityStatus(activityStatusUpdateRequest);
    }
}
