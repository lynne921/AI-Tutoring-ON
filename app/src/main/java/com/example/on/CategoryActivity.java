package com.example.on;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    List<CategoryItem> list;
    String stMsg;
    //데이터베이스 연결
    private FirebaseAuth mAuth;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String stID = getIntent().getStringExtra("email");
        listView = findViewById(R.id.listview_real);
        list = new ArrayList<>();

        //db에서 마지막 대화 카테고리별 긁어와서 list에 추가하기.(카테고리이름, 마지막 메세지)
        CategoryItem c = new CategoryItem("k-pop", "what's your favorite singer?");
        list.add(c);
        c = new CategoryItem("Toeic spicking", "what does she ~~?");
        list.add(c);

        //카테고리 어댑터
        CategoryAdapter adapter = new CategoryAdapter(this, list);

        //화면과 list내용 어댑터로 연결.
        listView.setAdapter(adapter);


        //리스트뷰의 한 카테고리 클릭시
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                Intent in = new Intent(CategoryActivity.this, ChatActivity.class);
                //ChatActivity로 이메일 넘기기
                in.putExtra("email", stID);
                startActivity(in);
                /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                //chat activity로 넘겨서 거기서 그 카테고리에 맞는 대화들 쭉 긁어와 화면에 뿌림->화면 1개만 사용.
                intent.putExtra("category", list.get(position).getCategory());
                intent.putExtra("message", list.get(position).getMessage());
                startActivity(intent);
            }
        });
    }
}