package db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqc on 17-8-24.
 */

public class MacInfo extends DataSupport {
    String mac;
    String manufacturer;

    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getMac() {
        return mac;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getManufacturer() {
        return manufacturer;
    }
}
