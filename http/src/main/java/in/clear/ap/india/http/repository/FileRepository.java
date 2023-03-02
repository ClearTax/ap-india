package in.clear.ap.india.http.repository;

import in.clear.ap.india.http.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,String> {
}
