package com.desmond.androiddesignlibraryfun;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    public static final String TAG = ListFragment.class.getSimpleName();

    public static Fragment newInstance() {
        return new ListFragment();
    }

    public ListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view;
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getRandomList(30)));
    }

    private List<String> getRandomList(int amount) {
        List<String> list = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            list.add("Line " + i);
        }
        return list;
    }

    private static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private List<String> mItems;
        private int mBackground;

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            mItems = items;
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item, viewGroup, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final String title = mItems.get(position);
            viewHolder.mTextView.setText(title);

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    Intent detailsIntent = new Intent(context, DetailsActivity.class);
                    detailsIntent.putExtra(DetailsActivity.EXTRA_INFO, title);

                    context.startActivity(detailsIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final View mView;
            private final TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
