package com.example.asif.firebaseauthdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView profileTV;
    private Button logoutBtn;


    private Button add_room;
    private EditText room_name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        FirebaseUser user=firebaseAuth.getCurrentUser();


        profileTV= (TextView) findViewById(R.id.profile_textview);
        logoutBtn= (Button) findViewById(R.id.log_out_button);


        //String temp_name=getIntent().getStringExtra("user_name");
        //Toast.makeText(ProfileActivity.this,temp_name,Toast.LENGTH_LONG).show();
        if(user.getDisplayName()!=null)
        {
            profileTV.setText("Welcome "+user.getDisplayName());
            name=user.getDisplayName();
        }

        if(user.getDisplayName()==null)
        {
            profileTV.setText("Welcomeee "+getIntent().getStringExtra("user_name"));
            name=getIntent().getStringExtra("user_name");
        }




        logoutBtn.setOnClickListener(this);








        //new starts


        add_room = (Button) findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);

        listView.setAdapter(arrayAdapter);



        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);

            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),Chat_Room.class);
                intent.putExtra("room_name",((TextView)view).getText().toString() );
                intent.putExtra("user_name",name);
                startActivity(intent);
            }
        });


        //new ends












    }

    @Override
    public void onClick(View v) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this,LogInActivity.class));
    }
}
