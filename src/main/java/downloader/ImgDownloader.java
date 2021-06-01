package downloader;

import org.yaml.snakeyaml.Yaml;
import utils.ImgHttpGetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/06/01
 */
public class ImgDownloader {
    @SuppressWarnings("AlibabaThreadShouldSetName")
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(4,
            Integer.MAX_VALUE,
            5000,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    private String ymlPath;

    public ImgDownloader(String ymlPath) {
        this.ymlPath = ymlPath;
    }

    public ImgDownloader() {
    }

    public void execute(){
        if(ymlPath == null || "".equals(ymlPath)){
            ymlPath = System.getProperty("user.dir") + File.separator + "url.yml";
        }
        Yaml yaml = new Yaml();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(ymlPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<Integer, Object> downloadChapterMap = yaml.load(inputStream);
        for(int chapterIndex : downloadChapterMap.keySet()){
            List<String> urls = (List<String>) downloadChapterMap.get(chapterIndex);
            String chapterPath = System.getProperty("user.dir") + File.separator + "Download" + File.separator + "Chapter" + chapterIndex;
            File file = new File(chapterPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            ChapterDownloadTask chapterDownloadTask = new ChapterDownloadTask(chapterIndex, chapterPath, urls);
            executor.execute(chapterDownloadTask);
        }
        executor.shutdown();
        System.out.println("All Download Task Submit Successfully!");
    }
}

class ChapterDownloadTask implements Runnable{

    private int chapter;
    private String path;
    private List<String> urls;

    public ChapterDownloadTask(int chapter, String path, List<String> urls) {
        this.chapter = chapter;
        this.path = path;
        this.urls = urls;
    }

    @Override
    public void run() {
        for(String url : urls){
            ImgHttpGetter.imgDownloader(url, path);
        }
        System.out.println("Chapter " + chapter + " Download Successfully!");
    }
}
