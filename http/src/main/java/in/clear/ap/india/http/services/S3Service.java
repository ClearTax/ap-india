package in.clear.ap.india.http.services;

import in.clear.ap.india.http.dtos.request.PreSignedUrlGenerationRequest;
import in.clear.ap.india.http.dtos.response.PreSignedUrlResponse;

public interface S3Service {
    PreSignedUrlResponse generatePreSignedUrl(PreSignedUrlGenerationRequest preSignedUrlGenerationRequest);
}
