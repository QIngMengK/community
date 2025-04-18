package life.dream.community.provider;
import com.alibaba.fastjson.JSON;
import life.dream.community.dto.GithubUser;
import okhttp3.*;
import life.dream.community.dto.AccessTokenDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
   public String getAccessToken(AccessTokenDTO accessTokenDTO) {
       MediaType mediaType = MediaType.get("application/json");

       OkHttpClient client = new OkHttpClient();

       RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
       Request request = new Request.Builder()
               .url("https://github.com/login/oauth/access_token")
               .post(body)
               .build();
       try (Response response = client.newCall(request).execute()) {
           String string = response.body().string();
           String token = string.split("&")[0].split("=")[1];
           return token;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class); //把string对象自动解析成java的类对象
            return githubUser;
        } catch (IOException e) {

        }
        return null;
    }
}
