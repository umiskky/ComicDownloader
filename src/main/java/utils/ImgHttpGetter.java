package utils;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/06/01
 */
public class ImgHttpGetter {

    public static void imgDownloader(String url, String savedPath){
        String[] list = url.split("/");
        String fileName = list[list.length - 1];
        fileName = savedPath + "/" + fileName;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(50000).setConnectTimeout(50000).build();
        //设置请求头
        request.setHeader( "User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1" );
        request.setConfig(requestConfig);

        try{
            CloseableHttpResponse response = httpClient.execute(request);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                FileUtils.copyInputStreamToFile(in, new File(fileName));
//                System.out.println("Download image successfully: " + url);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
