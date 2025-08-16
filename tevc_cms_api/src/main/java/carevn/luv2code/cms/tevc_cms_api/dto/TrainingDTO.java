package carevn.luv2code.cms.tevc_cms_api.dto;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingDTO {
    UUID id;

    String name;

    String description;

    Date startDate;

    Date endDate;

    String trainer;

    String location;

    Double cost;

    String status;

    List<UUID> participantIds;

    int participantCount;
}
