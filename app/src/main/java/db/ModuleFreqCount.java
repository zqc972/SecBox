package db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqc on 17-9-7.
 */

public class ModuleFreqCount extends DataSupport{

    public int navigationItemId;
    public int position;
    public int count;
    public long usetime;

    public void setNavigationItemId(int id) {
        this.navigationItemId = id;
    }
    public int getNavigationItemId() {
        return this.navigationItemId;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return this.position;
    }

    public void setUsetime () {
        usetime = System.currentTimeMillis();
    }
    public long getUsetime() {
        return usetime;
    }

    public void eraseCount() {
        this.count = 0;
    }
    public int getCount() {
        return this.count;
    }
    public void addCount() {
        this.count++;
    }
}
