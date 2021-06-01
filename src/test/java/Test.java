import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import utils.ImgHttpGetter;
import utils.ImgToPdf;
import utils.UrlHttpGetter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/06/01
 */
public class Test {
    @org.junit.jupiter.api.Test
    public void test(){
//        System.out.println(MyHttpGet.doGet("http://admin.xingmanapp.com:1010/api/content?chapter_url=%2F10101%2F2%2F1.html&source=coco"));
        String s = UrlHttpGetter.doGet("http://admin.xingmanapp.com:1010/api/content?chapter_url=%2F11021%2F1%2F2.html&source=coco");
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (Object o : jsonArray) {
            System.out.println(o);
        }
        System.out.println("Test");
    }

    @org.junit.jupiter.api.Test
    public void test2(){
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(new File("D:\\WorkSpace\\Idea\\ComicDownloader\\url.yml"));
            Map<Integer, Object> obj = yaml.load(inputStream);
            System.out.println("Test");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    public void test3(){
        String string = "https://img.cocomanhua.com/comic/11021/NEN4YTh1MW03MG1qL2U5THMrejdRNzlZbkQ2K0tZQXMvOWxZazBEbURwRT0=/0004.jpg";
        String[] list = string.split("/");
        System.out.println(list[list.length-1]);
        System.out.println("Test");
    }

    @org.junit.jupiter.api.Test
    public void test4(){
        String url = "https://img.cocomanhua.com/comic/11021/NEN4YTh1MW03MG1qL2U5THMrejdRNzlZbkQ2K0tZQXMvOWxZazBEbURwRT0=/0004.jpg";
        String path = "D:\\WorkSpace\\Idea\\ComicDownloader";
        ImgHttpGetter.imgDownloader(url, path);
    }

    @org.junit.jupiter.api.Test
    public void test5(){
        File file = new File("C:\\Users\\UmiSkky\\Desktop\\Chapter1");
        File[] fileList = file.listFiles();
        List<String> paths = new ArrayList<>();
        for(File tmpFile : fileList){
            paths.add(tmpFile.getAbsolutePath());
        }
        try {
            ImgToPdf.converter(paths, "C:\\Users\\UmiSkky\\Desktop\\Chapter1\\merge.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Test");
    }
}
