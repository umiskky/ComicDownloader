package utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/31
 * Apache封装好的CloseableHttpClient
 * 【参考资料】
 *  https://www.cnblogs.com/siv8/p/6222709.html
 *  https://blog.csdn.net/qq_35860138/article/details/82967727
 */
public class UrlHttpGetter {

    private static String tokenString = "";
    private static CloseableHttpClient httpClient = null;

    public static String doGet(String url){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        try {
            if (tokenString != null && !"".equals(tokenString)){
                tokenString = getToken();
            }
            //api_gateway_auth_token自定义header头，用于token验证使用
            get.addHeader("api_gateway_auth_token", tokenString);
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回json格式
                return EntityUtils.toString(response.getEntity(), "utf-8");
            } else if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                return "e";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取第三方接口的token
     */
    public static String getToken(){

        String token = "";

        JSONObject object = new JSONObject();
        object.put("appid", "appid");
        object.put("secretkey", "secretkey");

        try {
            if (httpClient == null){
                httpClient = HttpClientBuilder.create().build();
            }
            HttpPost post = new HttpPost("http://localhost/login");

            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");

            StringEntity s = new StringEntity(object.toString());
            s.setContentEncoding("UTF-8");
            //发送json数据需要设置contentType
            s.setContentType("application/x-www-form-urlencoded");
            //设置请求参数
            post.setEntity(s);
            HttpResponse response = httpClient.execute(post);

            //这里可以把返回的结果按照自定义的返回数据结果，把string转换成自定义类
            //ResultTokenBO result = JSONObject.parseObject(response, ResultTokenBO.class);

            //把response转为jsonObject
            JSONObject result = JSONObject.parseObject(String.valueOf(response));
            if (result.containsKey("token")){
                token = result.getString("token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}