package com.example.qiao.backandcurrentpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qiao.backandcurrentpage.widgets.BackAndNumView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    MyAdapter myAdapter;
    BackAndNumView backAndNumView;
    List mList = new ArrayList(100);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        backAndNumView = (BackAndNumView) findViewById(R.id.back_num_view);
        backAndNumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backAndNumView.getShowTop()){
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    backAndNumView.setBackTop(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastCompleteVisible;
                if (layoutManager instanceof LinearLayoutManager){
                    lastCompleteVisible = ((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition();
                    backAndNumView.setPageNum(mList.size(),lastCompleteVisible);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        mList = getTestList();
        myAdapter.setList(mList);
        recyclerView.setAdapter(myAdapter);
    }

    private List getTestList(){
        List list = new ArrayList(100);
        for (int i = 0;i<100;i++){
            list.add(i);
        }
        return list;
    }

    class  MyAdapter extends RecyclerView.Adapter{
        public List mList;

        public void setList(List list){
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.llist_item,parent,false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RecyclerViewHolder)holder).tvPage.setText(mList.get(position)+"");
        }

        @Override
        public int getItemCount() {
            Log.d("qjb-test","current mList.size():"+mList.size());
            return mList.size();
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tvPage;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvPage = (TextView) itemView.findViewById(R.id.tv_page);
        }
    }
}
