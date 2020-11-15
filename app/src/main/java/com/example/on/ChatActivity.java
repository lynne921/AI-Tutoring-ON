package com.example.on;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerView;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText etText;
    Intent intent;
    Button btnSend;
    Button btnTTS;
    String stID;
    FirebaseDatabase database;
    SpeechRecognizer mRecognizer;
    private TextToSpeech tts;
    ArrayList<Chat> chatArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        stID = getIntent().getStringExtra("email");
        btnSend = findViewById(R.id.btnSend);
        etText = findViewById(R.id.etText);
        // DB에 메시지 저장(write)
        database = FirebaseDatabase.getInstance();
        btnTTS = findViewById(R.id.btnTTS);

        chatArrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        mRecognizer.setRecognitionListener(recognitionListener);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = {"daily","kpop","hobby","movie"};
        mAdapter = new MyAdapter(chatArrayList, stID);
        recyclerView.setAdapter(mAdapter);

        //실시간이어서 DB 수정될 때마다 발동
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();
                String stID = chat.getEmail();
                String stText = chat.getText();

                Log.d(TAG, "stID: "+stID);
                Log.d(TAG, "stText: "+stText);
                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();

                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                Chat chat = dataSnapshot.getValue(Chat.class);
                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        DatabaseReference ref = database.getReference("message");
        ref.addChildEventListener(childEventListener);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB에 올리는 채팅 메시지 stText
                String stText = etText.getText().toString();
                Toast.makeText(ChatActivity.this, "msg: " + stText, Toast.LENGTH_LONG).show();

                // DB에 메시지 저장(write)
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Calendar c= Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss aa");
                String datetime = dateFormat.format(c.getTime());
                System.out.println(datetime);

                DatabaseReference myRef = database.getReference("message").child((datetime));

                Hashtable<String, String> numbers = new Hashtable<String, String>();
                numbers.put("email",stID);
                numbers.put("text",stText);

                myRef.setValue(numbers);
            }
        });
    }
    // ...
}