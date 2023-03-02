package in.clear.ap.india.http.services.impl;

import in.clear.ap.india.http.dtos.request.PreSignFileMetadata;
import in.clear.ap.india.http.dtos.request.PreSignedUrlGenerationRequest;
import in.clear.ap.india.http.dtos.response.PreSignedUrlResponse;
import in.clear.ap.india.http.services.S3Service;
import in.clear.ap.india.http.util.s3.S3Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class S3ServiceImpl implements S3Service {

    private final S3Helper s3Helper;

    @Override
    public PreSignedUrlResponse generatePreSignedUrl(PreSignedUrlGenerationRequest preSignedUrlGenerationRequest) {
        Map<String, String> map = new HashMap<>();
        for(PreSignFileMetadata preSignFileMetadata: preSignedUrlGenerationRequest.getFileMetadataList()){
            String preSignKey = preSignFileMetadata.getName()+"_"+ UUID.randomUUID().toString();
            String preSignedUrl = s3Helper.presignedPutUrl(preSignFileMetadata.getFileType().getMimeType(),preSignKey);
            map.put(preSignFileMetadata.getName(),preSignedUrl);
        }
        return PreSignedUrlResponse.builder().preSignedUrls(map).build();
    }
}
