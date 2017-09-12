package ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.secbox.zhaoqc.secbox.R;

import module.WebCrawler;

public class WebCloneFragment extends BaseFragment {

    private TextView mURL = null;
    private Button mStart = null;
    private TextView mCode = null;

    public WebCloneFragment() {
        // Required empty public constructor
    }


    public static WebCloneFragment newInstance() {
        WebCloneFragment fragment = new WebCloneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_web_clone, container, false);
        this.mURL = (TextView)rootView.findViewById(R.id.url);
        this.mStart = (Button) rootView.findViewById(R.id.start);
        this.mCode = (TextView) rootView.findViewById(R.id.code);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebCrawler webCrawler = new WebCrawler();
                webCrawler.setTargetURL(mURL.getText().toString());
                webCrawler.setOnFinishedListener(new WebCrawler.OnFinishedListener() {
                    @Override
                    public void onFinished(String content) {
                        mCode.setText(content);
                    }
                });
                webCrawler.start();
            }
        });
        return rootView;
    }

    @Override
    public String getTitle() {
        return "Web克隆";
    }
}
