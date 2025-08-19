package com.devdroid.shoppingnew.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devdroid.shoppingnew.R;
import com.devdroid.shoppingnew.adapter.FavoriteAdapter;
import com.devdroid.shoppingnew.modelclass.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView favoriteRecycler;
    private TextView tvNoFavorites;
    private ImageView backArrow;

    private FavoriteAdapter adapter;
    private List<Favorite> favoriteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        favoriteRecycler = view.findViewById(R.id.favoriteRecycler);
        tvNoFavorites = view.findViewById(R.id.tv_no_favorites);
        backArrow = view.findViewById(R.id.back_arrow);

        // Back button logic (if needed)
        backArrow.setOnClickListener(v -> requireActivity().onBackPressed());

        favoriteList = new ArrayList<>();
        // Add dummy data (you can fetch from shared preferences or DB)
        favoriteList.add(new Favorite(R.drawable.cabbage, "$2.22 x 4", 3, "Fresh Broccoli", "1.50 lbs"));
        favoriteList.add(new Favorite(R.drawable.grapes, "$2.22 x 4", 4, "Black Grapes", "5.0 lbs"));
        favoriteList.add(new Favorite(R.drawable.guava, "$2.22 x 4", 5, "Avocado", "1.50 lbs"));
        favoriteList.add(new Favorite(R.drawable.pineapple, "$2.22 x 4", 8, "Pineapple", "1.50 lbs"));

        adapter = new FavoriteAdapter(requireContext(), favoriteList, new FavoriteAdapter.OnFavoriteChangeListener() {
            @Override
            public void onQuantityZero(int position) {
                favoriteList.remove(position);
                adapter.notifyItemRemoved(position);
                updateEmptyState();
            }

            @Override
            public void onItemLongPressed(int position) {
                favoriteList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(requireContext(), "Item removed", Toast.LENGTH_SHORT).show();
                updateEmptyState();
            }
        });

        favoriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteRecycler.setAdapter(adapter);

        updateEmptyState();
        return view;
    }

    private void updateEmptyState() {
        if (favoriteList.isEmpty()) {
            tvNoFavorites.setVisibility(View.VISIBLE);
            favoriteRecycler.setVisibility(View.GONE);
        } else {
            tvNoFavorites.setVisibility(View.GONE);
            favoriteRecycler.setVisibility(View.VISIBLE);
        }
    }
}
