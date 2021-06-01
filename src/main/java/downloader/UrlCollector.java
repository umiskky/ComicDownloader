package downloader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;
import utils.UrlHttpGetter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/06/01
 */
public class UrlCollector {
    private Map<String, String> urlSettings;
    private String savedFileName = "url";
    private String savedFilePath;

    public UrlCollector(Map<String, String> urlSettings) {
        this.urlSettings = urlSettings;
        String projectPath = System.getProperty("user.dir");
        this.savedFilePath = projectPath + File.separator + savedFileName + ".yml";
    }

    public UrlCollector(Map<String, String> urlSettings, String savedFileName) {
        this.urlSettings = urlSettings;
        this.savedFileName = savedFileName;
        String projectPath = System.getProperty("user.dir");
        this.savedFilePath = projectPath + File.separator + savedFileName + ".yml";
    }

    public void collectUrl() {
        if(createFile()){
            Map<Integer, List<String>> urls = createUrlMap();
            try {
                FileWriter fileWriter = new FileWriter(new File(savedFilePath));
                Yaml yaml = new Yaml();
                yaml.dump(urls, fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Collect Urls Success!");
        }else{
            System.out.println("Collect Urls Failed!");
        }
    }

    private Boolean createFile() {
        File file = new File(savedFilePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            boolean res = file.createNewFile();
            if(res){
                System.out.println("File was created at:" + savedFilePath);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Map<Integer, List<String>> createUrlMap(){
        Map<Integer, List<String>> map = new HashMap();
        boolean flag = true;
        //每个请求错误重试次数
        final int tryTimes = 3;
        //允许总的错误数，达到总错误数后退出
        final int errorTimes = 3;
        //每次出错后等待时间ms
        final int waitTimes = 500;
        int index = Integer.parseInt(urlSettings.get("start_chapter"));
        int errorCount = 0;
        while(flag){
            String getUrl = urlSettings.get("url_left") + index + urlSettings.get("url_right");
            //首次尝试请求
            String res = UrlHttpGetter.doGet(getUrl);
            if("e".equals(res)){
                for(int i=0; i<tryTimes; i++){
                    try {
                        Thread.sleep(waitTimes);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    res = UrlHttpGetter.doGet(getUrl);
                    if(!"e".equals(res)){
                        break;
                    }
                }
            }
            //判断最终请求结果
            if("e".equals(res)){
                errorCount ++;
            }else {
                JSONObject jsonObject = JSONObject.parseObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<String> urlList = new ArrayList<>();
                for (Object o : jsonArray) {
                    urlList.add((String) o);
                }
                map.put(index, urlList);
            }

            if(errorCount == errorTimes){
                flag = false;
            }
            index ++;
//            if(index == 10){
//                flag = false;
//            }
        }
        return map;
    }
}
