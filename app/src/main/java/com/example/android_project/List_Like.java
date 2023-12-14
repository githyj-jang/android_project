package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class List_Like extends AppCompatActivity{

    DatabaseHelper dbHelper;
    List<Show> shows;
    ShowAdapter adapter;
    int currentPage = 0;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_list);
        dbHelper = new DatabaseHelper(List_Like.this);
        shows = dbHelper.getAllLikeListShows();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter initialize and set
        adapter = new ShowAdapter();
        recyclerView.setAdapter(adapter);
        adapter.items.addAll(shows);

        // Load initial data
        loadPage();

        ImageButton home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(List_Like.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton showlist = (ImageButton) findViewById(R.id.showlist);
        showlist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(List_Like.this, List_Show.class);
                startActivity(intent);
            }
        });

        ImageButton calendar = (ImageButton) findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(List_Like.this, Calendar.class);
                startActivity(intent);
            }
        });
        // Button event
        ImageButton prevButton = findViewById(R.id.leftButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    currentPage--;
                    loadPage();
                }
            }
        });
        ImageButton nextButton = findViewById(R.id.rightButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((currentPage + 1) * 9 < shows.size()) {
                    currentPage++;
                    loadPage();
                }
            }
        });
    }

    private void loadPage() {
        adapter.notifyDataSetChanged();
    }

    class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

        List<Show> items = new ArrayList<>();

        @Override // 레이아웃 생성
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            return new ViewHolder(view);
        }

        @Override // 각항목 이미지 설정
        public void onBindViewHolder(ViewHolder holder, int position) {
            Show item = items.get(currentPage * 9 + position); // adjust with page size
            Glide.with(holder.itemView.getContext()).load(item.imageObject).into(holder.imageButton);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), Detail_Show.class);
                    intent.putExtra("show", item ); // "show"는 키이며, item은 전달할 Show 객체입니다.
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            int start = currentPage * 9;
            return Math.min(9, items.size() - start); // adjust with page size
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageButton imageButton;

            ViewHolder(View itemView) {
                super(itemView);
                imageButton = itemView.findViewById(R.id.imageButton);
            }
        }
    }
}
