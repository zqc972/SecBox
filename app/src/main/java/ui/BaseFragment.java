package ui;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by zhaoqc on 17-8-27.
 */

public class BaseFragment extends Fragment {

    public static int navigationItemId = 0;
    public static int position = 0;

    public String getTitle() {
        return null;
    }

    public void setData(Intent intent) {

    }
}
