package in.clear.ap.india.activityconsolidator.clients;

import in.clear.ap.india.commonmodels.dtos.request.ActivityStatusUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(
        url = "/invoice",
        accept = MediaType.APPLICATION_JSON_VALUE)
public interface APIndiaHttpClient {

    @PutExchange(value = "/", contentType = MediaType.APPLICATION_JSON_VALUE, accept = MediaType.APPLICATION_JSON_VALUE)
    void updateActivityStatus(@RequestBody ActivityStatusUpdateRequest activityStatusUpdateRequest);
}
