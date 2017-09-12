package utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaoqc on 17-7-29.
 */

public class FileUtils {
    public static boolean isFileExist(Context context,String filePath) {
        try {
            File file = new File(filePath);
            return !file.isDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDirectoryExist(Context context,String directoryPath) {
        try {
            File file = new File(directoryPath);
            return file.isDirectory();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyAllFilesFromAssets(Context context,String assetsPath,String savePath) {
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名

            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    if(fileName.equals("images") || fileName.equals("sounds") || fileName.equals("webkit"))
                        continue;
                    String tmpPath = assetsPath + "/" + fileName;
                    if(tmpPath.indexOf("/") == 0)
                        tmpPath = tmpPath.substring(1,tmpPath.length());
                    copyAllFilesFromAssets(context, tmpPath , savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFileFromAssets(Context context,String assetsPath,String savePath) {
        InputStream is = null;
        try {
            is = context.getAssets().open(assetsPath);
            FileOutputStream fos = new FileOutputStream(new File(savePath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean makeFileExecutable(String destFile) {
        try {
            String command = "chmod 755 " + destFile;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
//            if(proc.waitFor() != 0 )
//                return false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
