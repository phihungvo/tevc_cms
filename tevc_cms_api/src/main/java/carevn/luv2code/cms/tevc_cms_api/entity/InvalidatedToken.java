package carevn.luv2code.cms.tevc_cms_api.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "invalidated_tokens")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidatedToken {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    private Date expiryTime;
}