package in.clear.ap.india.http.controller;

import in.clear.ap.india.commonmodels.dtos.request.PreSignedUrlGenerationRequest;
import in.clear.ap.india.commonmodels.dtos.response.PreSignedUrlResponse;
import in.clear.ap.india.http.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/public/storage/v1/")
@EnableFeignClients
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping(value = "/storage-urls", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreSignedUrlResponse generateBulkPreSignedUrl(@RequestBody PreSignedUrlGenerationRequest preSignedUrlGenerationRequest){
        return s3Service.generatePreSignedUrl(preSignedUrlGenerationRequest);
    }
}
