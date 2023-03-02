package in.clear.ap.india.http.repository;

import in.clear.ap.india.http.models.Activity;
import in.clear.ap.india.http.models.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {
    List<Activity> findByCreatedAtBeforeAndActivityStatusNotIn(List<ActivityStatus> activityStatuses, LocalDateTime expiryDate);
}
