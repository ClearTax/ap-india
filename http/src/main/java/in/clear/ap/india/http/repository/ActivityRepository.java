package in.clear.ap.india.http.repository;

import in.clear.ap.india.http.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {
}
