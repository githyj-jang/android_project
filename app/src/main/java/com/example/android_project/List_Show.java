package com.example.android_project;

import static java.lang.Math.log;
import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;



import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class List_Show extends AppCompatActivity {
    ArrayList<Show> items = new ArrayList<Show>();
    private int currentPage = 0;
    private final int pageSize = 9;
    static RequestQueue requestQueue;

    ShowAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_list);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        ImageButton home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(List_Show.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton likelist = (ImageButton) findViewById(R.id.likelist);
        likelist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(List_Show.this, List_Like.class);
                startActivity(intent);
            }
        });

        ImageButton calendar = (ImageButton) findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(List_Show.this, Calendar.class);
                startActivity(intent);
            }
        });

        makeRequest();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShowAdapter();
        recyclerView.setAdapter(adapter);

        for (int i =0; i<=92;i++){
            adapter.addItem(makeSample(i));
            items.add(makeSample(i));
        }

        ImageButton nextButton = findViewById(R.id.rightButton);
        nextButton.setOnClickListener(v -> {
            if ((currentPage + 1) * pageSize < items.size()) {
                currentPage++;
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "더 이상 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton prevButton = findViewById(R.id.leftButton);
        prevButton.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "첫 페이지입니다.", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void makeRequest() {
        String url = "http://api.kcisa.kr/openapi/CNV_060/request"; // Open API URL

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                params.put("serviceKey", "b70b1916-5c60-4bd5-9d82-7587f68a78c4");
                params.put("numOfRows", "90");
                params.put("pageNo", "1");

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄.");
    }

    public void processResponse(String response) {

        Gson gson = new Gson();
        ShowList showList = gson.fromJson(response, ShowList.class);

        for (int i = 0; i < showList.showResult.items.size(); i++) {
            Show show = showList.showResult.items.get(i);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Document doc = (Document) Jsoup.connect(show.url).get();
                        Element img = doc.select("div.img > img").first();
                        String imgSrc = img.absUrl("src");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show.imageObject = imgSrc;
                                adapter.addItem(show);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
    class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
        ArrayList<Show> items = new ArrayList<Show>();
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view =inflater.inflate(R.layout.image_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Show item = items.get(currentPage * pageSize + position);
            Glide.with(holder.itemView.getContext()).load(item.imageObject).into(holder.imageButton);
            Log.d("DatabaseHelper", "Like_List table is empty");
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), Detail_Show.class);
                    intent.putExtra("show", item ); // "show"는 키이며, item은 전달할 Show 객체입니다.
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }
        public void addItem(Show item) {
            items.add(item);
        }

        @Override
        public int getItemCount() {
            return Math.min(pageSize, items.size() - currentPage * pageSize);
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageButton imageButton;

            public ViewHolder(View itemView) {
                super(itemView);

                imageButton = itemView.findViewById(R.id.imageButton);
            }
        }
    }
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


}
