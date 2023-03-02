package in.clear.ap.india.http.repository;

import in.clear.ap.india.http.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File,String> {
    List<File> findByActivity_IdAndIdIn(String activityId, List<String> ids);
}
