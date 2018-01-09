package com.xingjian.custom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * http://www.cocoachina.com/android/20151030/13971.html
 * http://blog.csdn.net/lmj623565791/article/details/45460089
 * 屏幕适配教程
 * 先生成320*480等分辨率的px文件，然后选择一款机型进行界面的加载
 * 生成的文件放在values-480x320 大数字写在前
 * 图片根据不同的dpi放在各自的文件夹中
 * mdpi:120-160
 * hdpi:160-240
 * xhdpi:240-320
 * xxhdpi:320-480
 * xxxhdpi:480-640
 * 如果是适配大屏幕的pad，则要多面板布局：layout-sw600dp或者layout-large等文件夹
 * 该文件为生成xml文件
 * Created by thinkpad on 2018/1/9.
 */

public class CreateLayoutXml {
    private final static String rootPath = "D:\\haha\\";
    private final static float dw = 320f;
    private final static float dh = 480f;

    private final static String WTemplate = "[dimen name=\"x{0}\"]{1}px[/dimen]\n";
    private final static String HTemplate = "[dimen name=\"y{0}\"]{1}px[/dimen]\n";

    public static void main(String[] args) {
//        makeString(320, 480);
//        makeString(480, 800);
//        makeString(480, 854);
//        makeString(540, 960);
//        makeString(600, 1024);
//        makeString(720, 1184);
//        makeString(720, 1196);
//        makeString(720, 1280);
//        makeString(768, 1024);
//        makeString(800, 1280);
//        makeString(1080, 1812);
//        makeString(1080, 1920);
        makeString(1440, 2560);
    }

    public static void makeString(int w, int h) {

        StringBuffer sb = new StringBuffer();
        sb.append("[?xml version=\"1.0\" encoding=\"utf-8\"?]\n");
        sb.append("[resources]");
        float cellw = w / dw;
        for (int i = 1; i < 320; i++) {
            sb.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sb.append(WTemplate.replace("{0}", "320").replace("{1}", w + ""));
        sb.append("[/resources]");

        StringBuffer sb2 = new StringBuffer();
        sb2.append("[?xml version=\"1.0\" encoding=\"utf-8\"?]\n");
        sb2.append("[resources]");
        float cellh = h / dh;
        for (int i = 1; i < 480; i++) {
            sb2.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sb2.append(HTemplate.replace("{0}", "480").replace("{1}", h + ""));
        sb2.append("[/resources]");

        String path = rootPath.replace("{0}", h + "").replace("{1}", w + "");
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File layxFile = new File(path + "lay_x.xml");
        File layyFile = new File(path + "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sb.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sb2.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }
}
