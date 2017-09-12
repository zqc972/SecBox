package com.secbox.zhaoqc.secbox;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ui.BaseFragment;

import static org.litepal.LitePalApplication.getContext;

public class BackgroundTaskActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    private ModuleListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("后台任务");

        recyclerView = (RecyclerView) findViewById(R.id.list);
        adapter = new ModuleListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ViewHolder> {

        @Override
        public ModuleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_of_background_activity,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTitle.setText(FragmentQueue.fragmentList.get(position).getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFragment fragment = FragmentQueue.fragmentList.get(position);
                    if(fragment != null) {
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                        Intent intent = new Intent(getContext(), ModuleActivity.class);
                        intent.putExtra("navigationItemId",fragment.navigationItemId);
                        intent.putExtra("position",fragment.position);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return FragmentQueue.fragmentList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mTitle;
            public Button mDelButton;

            public ViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mDelButton = (Button) itemView.findViewById(R.id.kill);
            }
        }
    }
}
