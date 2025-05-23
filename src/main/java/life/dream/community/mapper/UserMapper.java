package life.dream.community.mapper;


import life.dream.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface  UserMapper {
    @Insert("insert into users (name,account_id,token,gmt_create,gmt_modified,AVATAR_URL) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarURL})")
    void insert(User user);

    //@Select("select * users where token = #{token}")

    @Select("select * from USERS where token = #{token}")
    User findbyToken(@Param("token")String token);

    @Select("select * from USERS where id = #{id}")
    User findbyId(@Param("id")Integer id);
}
