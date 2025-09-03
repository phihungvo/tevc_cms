package carevn.luv2code.cms.tevc_cms_api.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import carevn.luv2code.cms.tevc_cms_api.entity.model.Token;
import carevn.luv2code.cms.tevc_cms_api.service.InvalidTokenService;
import carevn.luv2code.cms.tevc_cms_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomBearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final InvalidTokenService invalidTokenService;

    /**
     * Processes the HTTP request and performs bearer token authentication if a valid bearer token is present.
     *
     * @param httpServletRequest  the HTTP request.
     * @param httpServletResponse the HTTP response.
     * @param filterChain         the filter chain.
     * @throws ServletException if an exception occurs that interferes with the filter chain's operation.
     * @throws IOException      if an I/O error occurs during processing.
     */
    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest httpServletRequest,
            @NonNull final HttpServletResponse httpServletResponse,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("API Request was secured with Security!");

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Token.isBearerToken(authorizationHeader)) {

            final String jwt = Token.getJwt(authorizationHeader);

            tokenService.verifyAndValidate(jwt);

            final String tokenId = tokenService.getId(jwt);

            invalidTokenService.checkForInvalidityOfToken(tokenId);

            final UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(jwt);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
