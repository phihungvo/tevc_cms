package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.User;

/**
 * Mapper interface named {@link RegisterRequestToUserEntityMapper} for mapping {@link RegisterRequest} to {@link User}.
 */
@Mapper
public interface RegisterRequestToUserEntityMapper extends BaseMapper<RegisterRequest, User> {

    /**
     * Maps a {@link RegisterRequest} instance to a {@link User} instance for saving purposes.
     *
     * @param registerRequest The {@link RegisterRequest} object to map.
     * @return A {@link User} object mapped from the {@link RegisterRequest}.
     */
    @Named("mapForSaving")
    default User mapForSaving(RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }

    /**
     * Initializes and returns an instance of {@code RegisterRequestToUserEntityMapper}.
     *
     * @return An initialized {@code RegisterRequestToUserEntityMapper} instance.
     */
    static RegisterRequestToUserEntityMapper initialize() {
        return Mappers.getMapper(RegisterRequestToUserEntityMapper.class);
    }
}
