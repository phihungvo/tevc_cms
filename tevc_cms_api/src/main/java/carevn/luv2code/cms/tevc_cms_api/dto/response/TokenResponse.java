package carevn.luv2code.cms.tevc_cms_api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;

    private Long accessTokenExpiresAt;

    private String refreshToken;
}
