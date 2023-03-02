package in.clear.ap.india.http.controller;

import in.clear.ap.india.http.dtos.request.BulkUnstructuredInputRequest;
import in.clear.ap.india.http.services.UnStructuredInputService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/public/unstructured/v1/")
@EnableFeignClients
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnstructuredInputController {

    private final UnStructuredInputService unStructuredInputService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createActivity(@RequestBody BulkUnstructuredInputRequest bulkUnstructuredInputRequest){
        unStructuredInputService.processUnstructuredInput(bulkUnstructuredInputRequest);
    }
}
