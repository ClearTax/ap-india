package in.clear.ap.india.http.util.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import in.clear.ap.india.http.util.Constants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("PMD")
public class S3Helper {
    private static final String DEFAULT_REGION = "ap-south-1";
    // private final S3Presigner s3Presigner;
    private final S3ClientConfigurarionProperties s3config;

    private static void copyInputStreamToFile(InputStream inputStream, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[81920];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
                // log.info("writing to file");
            }
            // bytes=null;
            outputStream.close();
        } catch (IOException ex) {
            log.info("io excelption", ex.getMessage());
        }
    }

    public String presignedPutUrl(@NonNull String key, @NonNull String contentType) {
        return presignedPutUrl(key, contentType, s3config.getBucket());
    }

    private String presignedPutUrl(@NonNull String key, @NonNull String contentType, @Nullable String bucket) {

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000L * 60 * s3config.getSignatureDurationForPut();
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3config.getBucket(),
                key).withMethod(HttpMethod.PUT).withExpiration(expiration).withContentType(contentType);
        AmazonS3 s3Client = this.buildClient(s3config.getAccessKeyId(), s3config.getSecretAccessKey(),
                s3config.getRegion().toString());
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private AmazonS3 buildClient(@Nullable String accessKey, @Nullable String secretKey, @Nullable String region) {

        return S3ClientSingleton.getBuildClientInstance(accessKey, secretKey, region, DEFAULT_REGION).amazonS3;
    }

    public URL getUrl(@Nullable String accessKey, @Nullable String secretKey, String bucketName, String key,
            @Nullable String region, boolean signed, @Nullable Date expiration) {
        return this.getUrl(this.buildClient(accessKey, secretKey, region), bucketName, key, signed, expiration);
    }

    private URL getUrl(AmazonS3 s3, String bucketName, String key, boolean signed, @Nullable Date expiration) {
        URL url;
        if (signed) {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
            if (Objects.nonNull(expiration)) {
                request.setExpiration(expiration);
            }

            url = s3.generatePresignedUrl(request);
        } else {
            url = s3.getUrl(bucketName, key);
        }

        log.debug("Got URL = {} for s3 bucket = {} and key = {}", url, bucketName, key);
        return url;
    }

    public URL uploadFileToS3(@NonNull String filePath, String uploadFileName) {
        return uploadFileToS3(filePath, uploadFileName, true);
    }

    public URL uploadFileToS3(@NonNull String filePath, String uploadFileName, boolean cleanup) {

        File uploadFile = new File(filePath);
        URL url = uploadFileToS3(uploadFile, uploadFileName);
        if (!cleanup) {
            boolean flag = uploadFile.delete();
            log.info("{} deleted : {}", filePath, flag);
        }
        return url;
    }

    public URL uploadFileToS3(File uploadFile, String uploadFileName) {

        AmazonS3 s3 = this.buildClient(s3config.getAccessKeyId(), s3config.getSecretAccessKey(),
                s3config.getRegion().toString());
        log.info("Starting upload to s3 for bucket = {} and", s3config.getBucket());
        log.info("{}", s3);
        if (StringUtils.isBlank(s3config.getBucket())) {
            throw new RuntimeException("bucket name cannot be empty");
        }

        s3.putObject(s3config.getBucket(), uploadFileName, uploadFile);
        log.info("Upload to s3 completed for bucket = {} ", s3config.getBucket());
        URL url = this.getUrl(s3, s3config.getBucket(), uploadFileName, false, null);
        log.info("Generated URL = {} for s3 bucket = {} and key = {}", url, s3config.getBucket(), uploadFileName);

        return url;
    }

    public String downloadFileFromS3AndGetLocalPath(@NonNull String downloadKey, String path) throws IOException {
        log.info("file key ====> {}", downloadKey);
        String fileKey = downloadKey.substring(1);
        AmazonS3 s3 = this.buildClient(s3config.getAccessKeyId(), s3config.getSecretAccessKey(),
                s3config.getRegion().toString());
        S3Object s3object = s3.getObject(new GetObjectRequest(s3config.getBucket(), fileKey));
        InputStream stream = s3object.getObjectContent();
        File file = createFolderStructureInProvidedPath(path, fileKey);
        s3 = null;
        copyInputStreamToFile(stream, file);
        s3object.close(); // closing to clean all the threads, else timeout may happen for big files for next download
        return file.getPath();
    }

    @NotNull
    private File createFolderStructureInProvidedPath(String path, String fileKey) {
        File homeDirectory = new File(Constants.ROOT_DIR);
        File directory = new File(homeDirectory, path);
        boolean directoryCreated = directory.mkdir();
        if (!directoryCreated) {
            log.info("directory already exists : {}", directory.getPath());
        }
        return new File(Paths.get(directory.getPath(), extractFileNameFromPath(fileKey)).toString());
    }

    private String extractFileNameFromPath(String path) {
        String[] nameParts = path.split(Pattern.quote("/"));
        return nameParts[nameParts.length - 1];
    }

    public URL generatePresignedUrl(String filePath, String fileName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 10);
        AmazonS3 s3 = this.buildClient(s3config.getAccessKeyId(), s3config.getSecretAccessKey(),
                s3config.getRegion().toString());
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3config.getBucket(),
                filePath).withMethod(HttpMethod.GET).withExpiration(calendar.getTime());
        if (!com.amazonaws.util.StringUtils.isNullOrEmpty(fileName)) {
            ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
            responseHeaders.setContentDisposition("attachment; filename=\"" + fileName + "\"");
            generatePresignedUrlRequest.setResponseHeaders(responseHeaders);
        }
        return s3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public String generateStringPreSignedUrl(String fileUrl) {
        try {
            return Objects.toString(generatePresignedUrl(new URL(fileUrl).getPath().substring(1), ""));
        } catch (MalformedURLException e) {
            log.error("MalformedURLException occurred while generatePresignedUrl: ", e);
        }
        return null;
    }

    public ObjectMetadata getFileObjectMeta(URL url) {
        String filekey = url.getFile().substring(1);
        GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(s3config.getBucket(), filekey);
        AmazonS3 s3 = this.buildClient(s3config.getAccessKeyId(), s3config.getSecretAccessKey(),
                s3config.getRegion().toString());
        final ObjectMetadata objectMetadata = s3.getObjectMetadata(metadataRequest);
        return objectMetadata;
    }

    public boolean isEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
        return false;
    }
}
