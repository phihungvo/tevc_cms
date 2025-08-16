package carevn.luv2code.cms.tevc_cms_api.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDTO {
    UUID id;

    String name;

    String description;

    Date startDate;

    Date endDate;

    String status;

    Double budget;

    UUID projectManagerId;

    String projectManagerName;

    List<UUID> memberIds;

    int memberCount;
}
