package com.xunao.diaodiao.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.R;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/5.
 */

public class CitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<CitiesBean.DatasBean> cities;
    private List<String> hotCity;

    private final int HEAD = 0;
    private final int WORD = 1;
    private final int CITY = 2;

    private RecyclerArrayAdapter<String> adapter;
    private MyHotItemClickListener hotItemClickListener;


    public MyHotItemClickListener getHotItemClickListener() {
        return hotItemClickListener;
    }

    public void setHotItemClickListener(MyHotItemClickListener hotItemClickListener) {
        this.hotItemClickListener = hotItemClickListener;
    }

    public CitiesAdapter(Context context, List<CitiesBean.DatasBean> cities,
                         List<String> hotCity){
        this.context = context;
        this.cities = cities;
        this.hotCity = hotCity;
    }

    public List<CitiesBean.DatasBean> getData() {
        return cities;
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if(cities==null || cities.size()==0) return count;
        count +=cities.size();
        for(CitiesBean.DatasBean datasBean:cities){
            count += datasBean.getAddressList().size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        if(position==count) return HEAD;//下标为0的固定显示头部布局。

        for(int i = 0; i < cities.size(); i++){
            count++;
            if(position==count){
                return WORD;
            }
            List<CitiesBean.DatasBean.AddressListBean> addressList = cities.get(i).getAddressList();
            for(int j =0;j<addressList.size();j++){
                count++;
                if(position==count){
                    return CITY;
                }
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD:
                View head = LayoutInflater.from(context).inflate(R.layout.layout_head, parent, false);
                return new HeadViewHolder(head);
            case WORD:
                View word = LayoutInflater.from(context).inflate(R.layout.layout_word, parent, false);
                return new WordViewHolder(word);
            case CITY:
                View city = LayoutInflater.from(context).inflate(R.layout.layout_city, parent, false);
                return new CityViewHolder(city);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int count = 0;
        if(position==count){
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            GridLayoutManager manager = new GridLayoutManager(context, 3);
            headViewHolder.recyclerView.setLayoutManager(manager);
            adapter = new RecyclerArrayAdapter<String>(context, R.layout.single_text_view) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, String s) {
                    baseViewHolder.setText(R.id.hot_city_text, s);
                }
            };

            adapter.setOnItemClickListener((view, i) -> {
                if (hotItemClickListener != null)
                    hotItemClickListener.onItemClick(view, adapter.getAllData().get(i));
            });

            headViewHolder.recyclerView.setAdapter(adapter);
            adapter.addAll(hotCity);
        }else{

            for(int i = 0; i < cities.size(); i++){
                count++;
                if(position==count){
                    WordViewHolder wordViewHolder = (WordViewHolder) holder;
                    CitiesBean.DatasBean datasBean = cities.get(i);
                    wordViewHolder.textWord.setText(datasBean.getAlifName());
                }
                List<CitiesBean.DatasBean.AddressListBean> addressList = cities.get(i).getAddressList();
                for(int j =0;j<addressList.size();j++){
                    count++;
                    if(position==count){
                        CityViewHolder cityViewHolder = (CityViewHolder) holder;
                        CitiesBean.DatasBean.AddressListBean addressListBean = addressList.get(j);
                        cityViewHolder.textCity.setText(addressListBean.getName());
                        cityViewHolder.textCity.setOnClickListener(v -> {
                            if (hotItemClickListener != null)
                                hotItemClickListener.onItemClick(v, addressListBean.getName());
                        });
                    }
                }
            }

        }


    }


    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public HeadViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            GridLayoutManager manager = new GridLayoutManager(view.getContext(), 3);
            recyclerView.setLayoutManager(manager);

        }
    }
    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView textWord;
        public WordViewHolder(View view) {
            super(view);
            textWord = (TextView) view.findViewById(R.id.textWord);
        }
    }
    public static class CityViewHolder extends RecyclerView.ViewHolder {

        TextView textCity;
        public CityViewHolder(View view) {
            super(view);
            textCity = (TextView) view.findViewById(R.id.textCity);
        }
    }

    public interface MyHotItemClickListener {
        public void onItemClick(View view,String data);
    }
}
