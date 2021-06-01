package utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/06/01
 */
public class ImgToPdf {

    /**
     * @description The method converter is used to merge images to a pdf file.
     * @param imgPathList 输入图片的路径列表
     * @param outputPath 输出pdf路径
     * @return void
     * @author umiskky
     * @date 2021/6/1-14:20
     */
    public static void converter(List<String> imgPathList, String outputPath) throws Exception{
        Document document = new Document(PageSize.A5,50,50,50,50);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();
        for(String imgPath : imgPathList){
            Image image = Image.getInstance(imgPath);
            Map<String,Float> param = getHeighWidth(image);
            image.scaleAbsolute(param.get("imageWidth"),param.get("imageHeight"));
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
        }
        document.close();
    }


    public static Map<String,Float> getHeighWidth(Image image) {
        float imageHeight = image.getScaledHeight();
        float imageWidth = image.getScaledWidth();
        Map<String, Float> resultParam = new HashMap<String, Float>();
        int i = 0;
        while (imageHeight > 500 || imageWidth > 500) {
            image.scalePercent(100 - i);
            i++;
            imageHeight = image.getScaledHeight();
            imageWidth = image.getScaledWidth();
        }
        resultParam.put("imageWidth", imageWidth);
        resultParam.put("imageHeight", imageHeight);
        return resultParam;
    }
}
