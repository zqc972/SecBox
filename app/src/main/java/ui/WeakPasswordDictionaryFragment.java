package ui;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.secbox.zhaoqc.secbox.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import db.WeakPassword;


public class WeakPasswordDictionaryFragment extends BaseFragment {

    private RecyclerView recyclerView = null;
    private List<WeakPassword> weakPasswordsList = null;
    private ListItemAdapter adapter = null;

    private EditText mSearchContent;
    private ImageButton mExpandButton;

    private boolean isExpand = false;

    public WeakPasswordDictionaryFragment() {
        // Required empty public constructor
    }

    public static WeakPasswordDictionaryFragment newInstance() {
        WeakPasswordDictionaryFragment fragment = new WeakPasswordDictionaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_password_tools;
        position = 0;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weak_password_dictionary, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.weak_password_list);
        weakPasswordsList = new ArrayList<WeakPassword>();
        adapter = new ListItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        mSearchContent = (EditText) rootView.findViewById(R.id.search_content);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadData(s.toString());
            }
        };
        mSearchContent.addTextChangedListener(textWatcher);

        mExpandButton = (ImageButton) rootView.findViewById(R.id.expand_button);
        mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand) {
                    mExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                } else {
                    mExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                isExpand = !isExpand;
                showPopupWindow();
            }
        });
        loadData(null);

        return rootView;
    }

    @Override
    public String getTitle() {
        return "弱密码字典";
    }

    private class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

        @Override
        public ListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_of_weak_password_dictionary,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.password.setText("原始密码:" + weakPasswordsList.get(position).getPassword());
            holder.md5.setText("MD5:" + weakPasswordsList.get(position).getMd5());
            holder.sha1.setText("SHA-1:" + weakPasswordsList.get(position).getSha1());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return weakPasswordsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView password;
            private TextView md5;
            private TextView sha1;

            public ViewHolder(View itemView) {
                super(itemView);
                password = (TextView) itemView.findViewById(R.id.password);
                md5 = (TextView) itemView.findViewById(R.id.md5);
                sha1 = (TextView) itemView.findViewById(R.id.sha1);
            }
        }
    }

    public void loadData(String search_content) {
        if(search_content != null) {
            weakPasswordsList = DataSupport.where("password like ?",search_content + "%").find(WeakPassword.class);
            weakPasswordsList.addAll(DataSupport.where("md5 like ?",search_content + "%").find(WeakPassword.class));
            weakPasswordsList.addAll(DataSupport.where("sha1 like ?",search_content + "%").find(WeakPassword.class));
        } else {
            weakPasswordsList = DataSupport.findAll(WeakPassword.class);
        }
        adapter.notifyDataSetChanged();
    }

    private void showPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.popupwindow_weak_password_dictionary,null);
        PopupWindow popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,false);
        // 需要设置一下此参数，点击外边可消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(getView().findViewById(R.id.expand_button));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isExpand = false;
                mExpandButton.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });
    }

}
