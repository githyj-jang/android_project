package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Detail_Show extends AppCompatActivity {
        Show show;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_detail);

            // Intent에서 Show 객체 받기
            Intent intent = getIntent();
            if (intent != null) {
                show = (Show) intent.getSerializableExtra("show");
                if (show == null) {
                    Toast.makeText(this, "전달된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            ImageView imageView = findViewById(R.id.image_show);
            Glide.with(this).load(show.imageObject).into(imageView);

            TextView titleTextView = findViewById(R.id.title);
            titleTextView.setText(show.title);
            TextView periodTextView = findViewById(R.id.period);
            periodTextView.setText("기간"+show.period);
            TextView contactTextView = findViewById(R.id.contactPoint);
            contactTextView.setText("전화번호"+show.contactPoint);
            TextView urlTextView = findViewById(R.id.url);
            urlTextView.setText("주소"+show.url);



            Document doc = Jsoup.parse(show.description);
            Elements paragraphs = doc.select("p");
            StringBuilder text = new StringBuilder();
            for (String paragraph : paragraphs.eachText()) {
                if (!paragraph.trim().isEmpty()) {
                    text.append(paragraph).append("\n");
                }
            }
            TextView programTextView = findViewById(R.id.description);
            programTextView.setText(text.toString());

            ImageButton Like_check_button = (ImageButton) findViewById(R.id.like_check);
            Like_check_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show != null) {
                        DatabaseHelper dbHelper = new DatabaseHelper(Detail_Show.this);
                        dbHelper.insertShow(show,"Like_List");
                    }
                }
            });
            ImageButton Schedule_check_button = (ImageButton) findViewById(R.id.schedule_check);
            Schedule_check_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show != null) {
                        DatabaseHelper dbHelper = new DatabaseHelper(Detail_Show.this);
                        dbHelper.insertShow(show,"Schedule");
                    }
                }
            });

            ImageButton home = (ImageButton) findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Detail_Show.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            ImageButton showlist = (ImageButton) findViewById(R.id.showlists);
            showlist.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Detail_Show.this, List_Like.class);
                    startActivity(intent);
                }
            });

            ImageButton likelist = (ImageButton) findViewById(R.id.likelist);
            likelist.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Detail_Show.this, List_Like.class);
                    startActivity(intent);
                }
            });

            ImageButton calendar = (ImageButton) findViewById(R.id.calendar);
            calendar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Detail_Show.this, Calendar.class);
                    startActivity(intent);
                }
            });
        }
}
