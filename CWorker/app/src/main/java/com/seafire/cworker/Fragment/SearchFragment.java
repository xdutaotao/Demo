package com.seafire.cworker.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.seafire.cworker.Activity.HomeDetailActivity;
import com.seafire.cworker.Bean.HomeResponseBean;
import com.seafire.cworker.Bean.SearchBean;
import com.seafire.cworker.Bean.SearchResponseBean;
import com.seafire.cworker.Common.Constants;
import com.seafire.cworker.Model.User;
import com.seafire.cworker.Present.SearchPresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.RxBus;
import com.seafire.cworker.Utils.ShareUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.SearchView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seafire.cworker.Common.Constants.LOGIN_AGAIN;

/**
 * create by
 */
public class SearchFragment extends BaseFragment implements SearchView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    SearchPresenter presenter;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.flow_layout)
    TagFlowLayout flowLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.result_recycler_view)
    GRecyclerView resultRecyclerView;
    @BindView(R.id.search_fragment)
    LinearLayout searchFragment;
    private String mParam1;

    private List<String> hotList = new ArrayList<>();
    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean> resultAdapter;
    private SearchBean bean = new SearchBean();
    private TagAdapter<String> tagAdapter;

    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                itemClickSearch(hotList.get(position));
                return true;
            }
        });


        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.search_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.search_text, s);
            }
        };

        adapter.setOnItemClickListener((view1, i) -> itemClickSearch(adapter.getItem(i)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setStackFromEnd(true);  //列表再底部开始展示，反转后由上面开始展示
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        clear.setOnClickListener(this);
        cancleAction.setOnClickListener(this);
        back.setOnClickListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_SEARCH) || (keyEvent != null) && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    doSearchAction(textView.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() > 0)
                    cancleAction.setVisibility(View.VISIBLE);
                else
                    cancleAction.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initResultRecyclerView();
        presenter.getHistoryData();
        presenter.getHotWord(getContext());


        searchFragment.setOnClickListener(v -> {
            SearchFragment.this.closeSoftKeyboard(editText, getContext());
        });
        return view;
    }

    private void itemClickSearch(String s) {
        editText.setText(s);
        doSearchAction(s);
    }

    private void initBean() {
        bean.setPageNo(1);
        bean.setPageSize(15);
        bean.setKeywords("");
        bean.setToken(User.getInstance().getUserId());
        bean.setType(User.getInstance().getUserInfo().getPerson().getType());
        bean.setVipRes(User.getInstance().getUserInfo().getPerson().getVIP());
        bean.setSort(0);
        bean.setProject(User.getInstance().getUserInfo().getProject().getProject());
    }

    private void initResultRecyclerView() {
        resultAdapter = new RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.TopicBean.GroupDataBean dataBean) {
                baseViewHolder.setImageUrl(R.id.item_img, dataBean.getPic(), R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_title, dataBean.getTitle());
                baseViewHolder.setText(R.id.item_author, "作者:" + dataBean.getAuthor());
                baseViewHolder.setVisible(R.id.item_vip, dataBean.getVipRes() != 0);
                baseViewHolder.setText(R.id.item_txt, dataBean.getDescription());
            }
        };

        resultAdapter.setOnItemClickListener((view, i) -> HomeDetailActivity.startActivity(getContext(), resultAdapter.getAllData().get(i)));

        resultRecyclerView.setAdapterDefaultConfig(resultAdapter, this, this);
    }


    private void doSearchAction(String key) {
        if (!TextUtils.isEmpty(key)) {
            if (User.getInstance().getUserInfo() == null) {
                ToastUtil.show("请登录");
                return;
            }
            initBean();
            bean.setKeywords(key);
            bean.setPageNo(1);
            List<String> searchList = ShareUtils.getStringList(Constants.SEARCH_KEY);
            searchList.add(key);
            ShareUtils.saveStringList(Constants.SEARCH_KEY, searchList);
            closeSoftKeyboard(editText, getContext());
            scrollView.setVisibility(View.GONE);
            resultRecyclerView.setVisibility(View.VISIBLE);
            resultAdapter.clear();
            back.setVisibility(View.VISIBLE);
            presenter.getSearchResult(bean);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                adapter.clear();
                ShareUtils.saveStringList(Constants.SEARCH_KEY, adapter.getAllData());
                clear.setVisibility(View.GONE);
                break;

            case R.id.cancle_action:
                editText.setText("");
                editText.setCursorVisible(true);
                showSoftKeyboard(editText, getContext());
//                scrollView.setVisibility(View.VISIBLE);
//                resultRecyclerView.setVisibility(View.GONE);
                break;

            case R.id.back:
                resultRecyclerView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                presenter.getHistoryData();
                break;
        }
    }

    @Override
    public void getData(SearchResponseBean searchResponseBean) {
        if (bean.getPageNo() == 1) {
            resultAdapter.clear();
        }
        resultAdapter.addAll(searchResponseBean.getData());
    }

    @Override
    public void getHistoryList(List<String> list) {
        adapter.clear();
        //去重
        HashSet set = new HashSet(list);
        adapter.addAll(set);
        if (list.size() == 0) {
            clear.setVisibility(View.GONE);
        } else {
            clear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fail(String msg) {
        ToastUtil.show(msg);
        if (resultAdapter.getAllData().size() == 0)
            resultRecyclerView.setVisibility(View.GONE);
        else
            resultAdapter.stopMore();

        if (TextUtils.equals(msg, "请登录!")) {
            User.getInstance().clearUser();
            RxBus.getInstance().post(LOGIN_AGAIN);
        }
    }

    @Override
    public void getHotWord(List<String> list) {
        for (String s : list) {
            if (!TextUtils.isEmpty(s)) {
                hotList.add(s);
            }
        }

        if (hotList.get(0).length() < 1)
            return;

        tagAdapter = new TagAdapter<String>(hotList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tv, flowLayout, false);
                view.setText(hotList.get(position));
                return view;
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }

    @Override
    public void onRefresh() {
        bean.setPageNo(1);
        presenter.getSearchResult(bean);
    }

    @Override
    public void onLoadMore() {
        bean.setPageNo(bean.getPageNo() + 1);
        presenter.getSearchResult(bean);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
