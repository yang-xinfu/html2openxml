package com.denisfesenko.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http 工具
 *
 * @author YangXinFu
 * @date 2024/8/29 18:15
 */
public class HttpUtil {

    public static File downloadFile(String urlStr,File file) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            httpURLConnection.setRequestMethod("GET");

            // 打开输入流从URL连接
            InputStream inputStream = httpURLConnection.getInputStream();

            // 设置文件保存的路径
            FileOutputStream fileOutputStream = FileUtils.openOutputStream(file);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            // 关闭流
            fileOutputStream.close();
            inputStream.close();

            // 关闭URL连接
            httpURLConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

}
