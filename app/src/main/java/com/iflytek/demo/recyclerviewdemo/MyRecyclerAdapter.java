package com.iflytek.demo.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.demo.R;

import java.util.List;

/**
 * Created by jianglei on 2017/2/24.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public MyRecyclerAdapter(Context context, List<String> datas){
        this. mContext=context;
        this. mDatas=datas;
        inflater=LayoutInflater. from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        int num = position % 2;
        if (num == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {   //创建第一种holder
            View view = inflater.inflate(R.layout.item_main,parent, false);
            return new MyViewHolder(view);
        } else  {
            View view = inflater.inflate(R.layout.item_main_two,parent, false);
            return new MyViewHoldertwo(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyViewHolder) {
            if(mOnItemClickLitener != null){
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = ((MyViewHolder)viewHolder).getPosition();
                        mOnItemClickLitener.onItemClick(viewHolder.itemView,pos);
                    }
                });
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        int pos = viewHolder.getPosition();
                        mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                        return false;
                    }
                });
            }
            ((MyViewHolder)viewHolder).tv.setText( mDatas.get(i));
            ((MyViewHolder)viewHolder).tv.setTextColor(mContext.getResources().getColor(R.color.black));
        } else if (viewHolder instanceof MyViewHoldertwo) {
            if(mOnItemClickLitener != null){
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = ((MyViewHolder)viewHolder).getPosition();
                        mOnItemClickLitener.onItemClick(viewHolder.itemView,pos);
                    }
                });
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        int pos = ((MyViewHolder)viewHolder).getPosition();
                        mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                        return false;
                    }
                });
            }
            ((MyViewHoldertwo)viewHolder).tv.setText( mDatas.get(i));
            ((MyViewHoldertwo)viewHolder).tv.setTextColor(mContext.getResources().getColor(R.color.brown));
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv=(TextView) view.findViewById(R.id.id_num);
        }

    }
    class MyViewHoldertwo extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHoldertwo(View view) {
            super(view);
            tv=(TextView) view.findViewById(R.id.id_num);
        }

    }
    public void addData(int position) {
        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
