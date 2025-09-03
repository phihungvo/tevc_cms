package carevn.luv2code.cms.tevc_cms_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.entity.model.UserModel;

/**
 * Mapper interface namec {@link UserEntityToUserMapper} for mapping {@link User} to {@link User}.
 */
@Mapper
public interface UserEntityToUserMapper extends BaseMapper<User, UserModel> {

    /**
     * Maps a {@link User} object to a {@link User} object.
     *
     * @param source The {@link User} object to map.
     * @return A {@link User} object mapped from the {@link User}.
     */
    @Override
    UserModel map(User source);

    /**
     * Initializes and returns an instance of {@code UserEntityToUserMapper}.
     *
     * @return An initialized {@code UserEntityToUserMapper} instance.
     */
    static UserEntityToUserMapper initialize() {
        return Mappers.getMapper(UserEntityToUserMapper.class);
    }
}
