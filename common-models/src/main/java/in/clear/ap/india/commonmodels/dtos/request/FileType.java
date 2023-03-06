package in.clear.ap.india.commonmodels.dtos.request;

public enum FileType {
    PDF(".pdf","application/pdf"),
    JPG(".jpg", "image/jpg"),
    JPEG(".jpeg","image/jpeg"),
    PNG(".png", "image/png");

    private final String extension;
    private final String mimeType;

    FileType(String key, String value) {
        this.extension = key;
        this.mimeType = value;
    }

    public String getExtension() {
        return extension;
    }
    public String getMimeType() {
        return mimeType;
    }
}
