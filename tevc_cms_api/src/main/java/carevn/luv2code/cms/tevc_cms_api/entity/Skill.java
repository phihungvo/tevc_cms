package carevn.luv2code.cms.tevc_cms_api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import carevn.luv2code.cms.tevc_cms_api.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "skills")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    String description;

    @Enumerated(EnumType.STRING)
    SkillLevel level;

    LocalDateTime lastUpdated;

    @ManyToMany(mappedBy = "skills")
    @JsonBackReference
    List<Employee> employees = new ArrayList<>();
}
