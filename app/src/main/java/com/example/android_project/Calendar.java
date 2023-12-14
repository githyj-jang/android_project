package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calendar extends AppCompatActivity {
    private DatabaseHelper db;
    public Show makeSample(int i) {
        Show show = new Show();
        String k = String.valueOf(i);
        show.title = "title" +k;
        show.type = "type";
        if (i %4== 1){
            show.period = "20231213 ~ 20231213";
        } else if (i%4==2) {
            show.period = "20231216 ~ 20231217";
        } else if (i%4==3) {
            show.period = "20231215 ~ 20231215";
        }else{
            show.period = "20231219 ~ 20231219";
        }
        show.eventPeriod = "eventPeriod";
        show.eventSite = "eventSite";
        show.charge = "charge";
        show.contactPoint = "contactPoint";
        show.url ="https://www.mcst.go.kr/kor/s_culture/culture/cultureView.jsp?pSeq=40770";
        if (i %10 == 1){
            show.imageObject = "https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702513157312.jpg";
        } else if (i %10==2) {
            show.imageObject = "https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702442615283.jpg";
        } else if (i %10==3) {
            show.imageObject = "https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702427175230.jpg";
        } else if (i %10==4){
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702514099694.gif";
        } else if (i %10==5){
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702514134298.jpg";
        } else if (i %10==6){
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1699417551991.jpg";
        } else if (i %10==7){
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1701653101315.jpg";
        } else if (i %10==8){
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1701739949828.jpg";
        } else if(i %10==9){
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702267476272.jpg";
        } else{
            show.imageObject ="https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1701848885467.jpg";
        }
        show.description = "description";
        show.viewCount = "viewCount";
        return show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        ImageButton home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton showlist = (ImageButton) findViewById(R.id.showlists);
        showlist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Calendar.class);
                startActivity(intent);
            }
        });

        ImageButton likelist = (ImageButton) findViewById(R.id.likelist);
        likelist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, List_Like.class);
                startActivity(intent);
            }
        });

        /*
        db = new DatabaseHelper(this);
*/

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.format(Locale.getDefault(), "%04d%02d%02d", year, month + 1, dayOfMonth);
                //List<Show> shows = db.getShowsByDate(date);
                int day = Integer.parseInt(date.substring(6));
                Show sampleShow = makeSample(6);

                // 생성한 샘플 데이터를 List에 추가합니다.
                List<Show> shows = new ArrayList<>();
                shows.add(sampleShow);
                Schedulelist fragment = Schedulelist.newInstance(shows);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            }
        });
    }

}
