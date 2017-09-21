package utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhaoqc on 17-7-29.
 */

public class ShellUtils {

    public static int run_for_resultCode(String[] cmd,String workdirectory){
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            Process process = builder.start();
            int code = process.waitFor();
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static synchronized String run(String[] cmd, String workdirectory)
            throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            // 创建操作系统进程（也可以由Runtime.exec()启动）
            // Runtime runtime = Runtime.getRuntime();
            // Process proc = runtime.exec(cmd);
            // InputStream inputstream = proc.getInputStream();
            ProcessBuilder builder = new ProcessBuilder(cmd);

            InputStreamReader in = null;
            // 设置一个路径（绝对路径了就不一定需要）
            if (workdirectory != null) {
                // 设置工作目录（同上）
                builder.directory(new File(workdirectory));
            }
            // 合并标准错误和标准输出
            builder.redirectErrorStream(true);
            // 启动一个新进程
            Process process = builder.start();
            // 读取进程标准输出流
            in = new InputStreamReader(process.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            String line;

            // 读取输出
            while ((line = reader.readLine()) != null) {
                result.append(line + "\n");
            }

            // 关闭输入流
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.toString();

    }


}
