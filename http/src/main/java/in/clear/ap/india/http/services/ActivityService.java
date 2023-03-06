package in.clear.ap.india.http.services;


import in.clear.ap.india.commonmodels.dtos.request.ActivityStatusUpdateRequest;
import in.clear.ap.india.commonmodels.dtos.request.BulkUnstructuredInputRequest;

public interface ActivityService {
    void processUnstructuredInput(BulkUnstructuredInputRequest bulkUnstructuredInputRequest);
    void expireUncompletedActivities();
    void updateActivityStatus(ActivityStatusUpdateRequest activityStatusUpdateRequest);
}
