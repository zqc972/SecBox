package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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



public class RecentUseFragment extends Fragment {

    private RecyclerView recyclerView = null;
    private ArrayList<FunctionListItem> items = new ArrayList<FunctionListItem>();
    private FunctionListAdapter adapter = null;

    public RecentUseFragment() {
        // Required empty public constructor
    }

    public static RecentUseFragment newInstance() {
        RecentUseFragment fragment = new RecentUseFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_recent_use, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FunctionListAdapter();
        recyclerView.setAdapter(adapter);
        loadData();
        return rootView;
    }

    private void loadData() {
        List<ModuleFreqCount> freqCountList = DataSupport.order("usetime desc").find(ModuleFreqCount.class);
        if(freqCountList != null) {
            for(int i = 0; i < freqCountList.size();i++ ) {
                int navigationItemId = freqCountList.get(i).getNavigationItemId();
                int position = freqCountList.get(i).getPosition();
                int titlesId;
                int subtitlesId;
                switch (navigationItemId) {
                    case R.id.nav_scan_tools:
                        titlesId = R.array.nav_scan_tools_titles;
                        subtitlesId = R.array.nav_scan_tools_subtitles;
                        Log.i("RecentUseFragment","nav_scan_tools = "+navigationItemId+",titlesId = " + R.array.nav_scan_tools_titles + " subtitlesId = " + R.array.nav_scan_tools_subtitles);
                        break;
                    case R.id.nav_cheat_attack:
                        titlesId = R.array.nav_cheat_attack_titles;
                        subtitlesId = R.array.nav_cheat_attack_subtitles;
                        Log.i("RecentUseFragment","nav_cheat_attack = "+navigationItemId+",titlesId = " + R.array.nav_cheat_attack_titles + " subtitlesId = " + R.array.nav_cheat_attack_subtitles);
                        break;
                    case R.id.nav_sniffer:
                        titlesId = R.array.nav_sniffer_titles;
                        subtitlesId = R.array.nav_sniffer_subtitles;
                        Log.i("RecentUseFragment","nav_sniffer = "+ navigationItemId +",titlesId = " + R.array.nav_sniffer_titles + " subtitlesId = " + R.array.nav_sniffer_subtitles);
                        break;
                    case R.id.nav_password_tools:
                        titlesId = R.array.nav_password_tools_titles;
                        subtitlesId = R.array.nav_password_tools_subtitles;
                        Log.i("RecentUseFragment","nav_password_tools = " +navigationItemId+ ",titlesId = " + R.array.nav_password_tools_titles + " subtitlesId = " + R.array.nav_password_tools_subtitles);
                        break;
                    case R.id.nav_other_tools:
                        titlesId = R.array.nav_other_tools_titles;
                        subtitlesId = R.array.nav_other_tools_subtitles;
                        Log.i("RecentUseFragment","nav_other_tools" + navigationItemId+",titlesId = " + R.array.nav_other_tools_titles + " subtitlesId = " + R.array.nav_other_tools_subtitles);
                        break;
                    default:
                        titlesId = 0;
                        subtitlesId = 0;
                        Log.i("RecentUseFragment","default");
                }
                Log.i("RecentUseFragment","position = " + position);
                if(titlesId != 0 && subtitlesId != 0) {
                    FunctionListItem item = new FunctionListItem();
                    item.setTitle( getResources().getStringArray(titlesId) [position] );
                    item.setSubtitle(getResources().getStringArray(subtitlesId) [position]);
                    item.setNavigationItemId(navigationItemId);
                    item.setPosition(position);
                    items.add(item);
                    adapter.notifyDataSetChanged();
                }

            }
        }

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
                    intent.putExtra("navigationItemId",items.get(position).getNavigationItemId());
                    intent.putExtra("position",items.get(position).getPosition());
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
        private int navigationItemId;
        private int position;

        public void setTitle(String title){
            this.title = title;
        }
        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
        public void setNavigationItemId(int navigationItemId) {
            this.navigationItemId = navigationItemId;
        }
        public void setPosition(int position) {
            this.position = position;
        }

        public String getTitle() {
            return title;
        }
        public String getSubtitle() {
            return subtitle;
        }
        public int getNavigationItemId() {
            return navigationItemId;
        }
        public int getPosition() {
            return position;
        }
    }
}
