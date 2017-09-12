package db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqc on 17-8-24.
 */

public class WeakPassword extends DataSupport{
    String password;
    String md5;
    String sha1;

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public String getMd5() {
        return md5;
    }
    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
    public String getSha1() {
        return sha1;
    }
}
