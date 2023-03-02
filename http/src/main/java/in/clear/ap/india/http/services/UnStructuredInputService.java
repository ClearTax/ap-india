package in.clear.ap.india.http.services;

import in.clear.ap.india.http.dtos.request.BulkUnstructuredInputRequest;

public interface UnStructuredInputService {
    void processUnstructuredInput(BulkUnstructuredInputRequest bulkUnstructuredInputRequest);
}
