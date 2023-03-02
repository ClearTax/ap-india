package in.clear.ap.india.http.services;

import in.clear.ap.india.http.dtos.request.FileStatusUpdateRequest;

public interface FileService {
    void updateFileStatus(FileStatusUpdateRequest fileStatusUpdateRequest);
}
