package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secbox.zhaoqc.secbox.ModuleActivity;
import com.secbox.zhaoqc.secbox.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import db.ModuleFreqCount;
import ui.ArpCheatFragment;
import ui.ScanHostsFragment;
import ui.ScanPortsFragment;
import ui.SnifferFragment;
import ui.WeakPasswordDictionaryFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FunctionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FunctionListFragment extends Fragment {

    private ArrayList<FunctionListItem> items = new ArrayList<FunctionListItem>();
    private FunctionListAdapter adapter;
    private static int navigationItemId = 0;

    public FunctionListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FunctionListFragment newInstance(int id) {
        FunctionListFragment fragment = new FunctionListFragment();
        navigationItemId = id;
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
        View rootView = inflater.inflate(R.layout.fragment_function_list, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FunctionListAdapter();
        recyclerView.setAdapter(adapter);
        loadData();
        return rootView;
    }

    //Load list info
    private void loadData() {
        String [] titles = {};
        String [] subtitles = {};
        switch (navigationItemId) {
            case R.id.nav_scan_tools:
                titles = getResources().getStringArray(R.array.nav_scan_tools_titles);
                subtitles = getResources().getStringArray(R.array.nav_scan_tools_subtitles);
                break;
            case R.id.nav_cheat_attack:
                titles = getResources().getStringArray(R.array.nav_cheat_attack_titles);
                subtitles = getResources().getStringArray(R.array.nav_cheat_attack_subtitles);
                break;
            case R.id.nav_sniffer:
                titles = getResources().getStringArray(R.array.nav_sniffer_titles);
                subtitles = getResources().getStringArray(R.array.nav_sniffer_subtitles);
                break;
            case R.id.nav_password_tools:
                titles = getResources().getStringArray(R.array.nav_password_tools_titles);
                subtitles = getResources().getStringArray(R.array.nav_password_tools_subtitles);
                break;
            case R.id.nav_other_tools:
                titles = getResources().getStringArray(R.array.nav_other_tools_titles);
                subtitles = getResources().getStringArray(R.array.nav_other_tools_subtitles);
                break;
            default:

        }
        for(int i = 0; i < titles.length && i < subtitles.length ; i++ ){
            FunctionListItem item = new FunctionListItem();
            item.setTitle(titles[i]);
            item.setSubtitle(subtitles[i]);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private class FunctionListAdapter extends RecyclerView.Adapter<FunctionListAdapter.ViewHolder> {

        @Override
        public FunctionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_of_function_list,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.title.setText(items.get(position).getTitle());
            holder.subtitle.setText(items.get(position).getSubtitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ModuleActivity.class);
                    intent.putExtra("navigationItemId",navigationItemId);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView title;
            public TextView subtitle;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            }
        }
    }

    private class FunctionListItem {
        private String title;
        private String subtitle;

        public void setTitle(String title){
            this.title = title;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }

}
