package in.clear.ap.india.http.models;

import in.clear.ap.india.commonmodels.dtos.request.FileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "activity-files")
public class File {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "s3Url", nullable = false)
    private String s3Url;

    @ManyToOne
    @JoinColumn(name="activity_id", nullable=false)
    private Activity activity;

    @Column(name = "fileStatus", nullable = false)
    private FileStatus fileStatus;

    @Column(name = "errorMessage")
    private String errorMessage;

    @Column(name = "ocrId")
    private String ocrId;

}
