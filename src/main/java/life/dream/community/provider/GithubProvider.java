package life.dream.community.provider;

import com.alibaba.fastjson.JSON;
import life.dream.community.dto.GithubUser;
import okhttp3.*;
import life.dream.community.dto.AccessTokenDTO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Configuration
class OkHttpClientConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                // 设置连接超时时间为30秒
                .connectTimeout(30, TimeUnit.SECONDS)
                // 设置读超时为30秒
                .readTimeout(30, TimeUnit.SECONDS)
                // 设置写超时为30秒
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}

@Component
public class GithubProvider {

    private final OkHttpClient client;

    // 通过构造方法注入我们配置好的 OkHttpClient
    @Autowired
    public GithubProvider(OkHttpClient client) {
        this.client = client;
    }

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json");

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            // 强化解析，确保返回结果能够正确获取 token
            String[] pairs = string.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2 && "access_token".equals(kv[0])) {
                    return kv[1];
                }
            }
        } catch (Exception e) {
            // 打印错误信息或记录日志，方便定位问题
            System.err.println("获取 access token 失败：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            // 将 JSON 字符串转换为 GithubUser 对象
            return JSON.parseObject(string, GithubUser.class);
        } catch (IOException e) {
            System.err.println("获取 Github 用户信息失败：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
