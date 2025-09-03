package carevn.luv2code.cms.tevc_cms_api.dto.requests;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Email(message = "Please enter valid e-mail address")
    @Size(min = 7, message = "Minimum e-mail length is 7 characters.")
    private String email;

    private String username;

    @Size(min = 8)
    private String password;

    @NotBlank(message = "First name can't be blank.")
    private String firstName;

    @NotBlank(message = "Last name can't be blank.")
    private String lastName;

    @NotBlank(message = "Phone number can't be blank.")
    @Size(min = 11, max = 20)
    private String phoneNumber;

    @NotEmpty(message = "Role can't be empty.")
    private List<Integer> roleIds;
}
