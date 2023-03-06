package in.clear.ap.india.unstructuredinputconsumer.services;

import in.clear.ap.india.commonmodels.dtos.request.Activity;

public interface UnstructuredInputListener {
    public void poll(Activity activity);
}
