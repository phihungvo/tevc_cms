package carevn.luv2code.cms.tevc_cms_api.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Student {
    @Id
    @GeneratedValue
    UUID id;

    private String name;
    private String email;
    private String mobileNo;
}
