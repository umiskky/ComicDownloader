import downloader.ImgDownloader;
import downloader.UrlCollector;
import utils.ImgToPdf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/31
 */
public class Main {
    public static void main(String[] args) {
        Map<String, String> urlSettings = new HashMap<>();
        urlSettings.put("url_left", "http://admin.xingmanapp.com:1010/api/content?chapter_url=/11021/1/");
        urlSettings.put("url_right", ".html&source=coco");
        urlSettings.put("start_chapter", "1");

        UrlCollector urlCollector = new UrlCollector(urlSettings);
        urlCollector.collectUrl();

        ImgDownloader imgDownloader = new ImgDownloader();
        imgDownloader.execute();

        //##############################################################################################
        List<String> paths = new ArrayList<>();
        File chapterDirectory = new File("D:\\WorkSpace\\Idea\\ComicDownloader\\Download");
        int length = chapterDirectory.listFiles()==null ? 0 : chapterDirectory.listFiles().length;
        for(int index=0; index<length; index++){
            File chapter = new File("D:\\WorkSpace\\Idea\\ComicDownloader\\Download\\Chapter" + (index + 1));
            File[] imgFileList = chapter.listFiles();
            if(imgFileList!=null && imgFileList.length>0){
                for(File file : imgFileList){
                    paths.add(file.getAbsolutePath());
                }
            }
        }
        try {
            ImgToPdf.converter(paths, "D:\\WorkSpace\\Idea\\ComicDownloader\\Download\\merge.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
