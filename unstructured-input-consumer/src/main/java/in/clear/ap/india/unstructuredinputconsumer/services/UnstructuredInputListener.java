package in.clear.ap.india.unstructuredinputconsumer.services;

import in.clear.ap.india.unstructuredinputconsumer.dtos.Activity;

public interface UnstructuredInputListener {
    public void poll(Activity activity);
}
