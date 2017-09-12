package db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqc on 17-8-24.
 */

public class Port extends DataSupport{
    int port;
    int type;               // 1 = tcp ,2 = udp, 3 = tcp+udp
    boolean offical_flag;   // true = official, false = non-official
    boolean reuse_flag;
    String description;
    int frequency;

    public static int TCP = 1;
    public static int UDP = 2;
    public static int TCP_UDP = 3;

    public void setPort(int port) {
        this.port = port;
    }
    public int getPort() {
        return port;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }
    public void setOffical(boolean flag) {
        this.offical_flag = flag;
    }
    public boolean isOffical() {
        return offical_flag;
    }
    public void setReuse(boolean flag) {
        this.reuse_flag = flag;
    }
    public boolean isReuse() {
        return this.reuse_flag;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public int getFrequency() {
        return this.frequency;
    }
}
