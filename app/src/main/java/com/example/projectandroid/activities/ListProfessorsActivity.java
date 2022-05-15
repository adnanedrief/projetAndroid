package com.example.projectandroid.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListProfessorsActivity extends AppCompatActivity {
    ListView myListView;
    private List<QueryDocumentSnapshot> userList = new ArrayList<>();

    ArrayList<String> myArrayList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listprofessors);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(ListProfessorsActivity.this, android.R.layout.simple_list_item_1, myArrayList);

        myListView = (ListView) findViewById(R.id.ListView1);
        myListView.setAdapter(myArrayAdapter);

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                userList.clear();

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        userList.add(document);
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }

                List<String> namesList = userList.stream().map(user -> user.getString("firstname")+user.getString("lastname")).collect(Collectors.toList());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, namesList);
                adapter.notifyDataSetChanged();
                myListView.setAdapter(adapter);
            }

        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("firstname", userList.get(position).getString("firstname"));
                intent.putExtra("lastname", userList.get(position).getString("lastname"));
                List<String> years = (List<String>) userList.get(position).get("years");
                String yearString = "";
                for(String year:years){
                    if(years.lastIndexOf(year) == years.size()-1) {
                        yearString += year;
                    }else{
                        yearString += year + " & ";
                    }
                }
                intent.putExtra("years",yearString );
                intent.putExtra("email", userList.get(position).getString("email"));
                intent.putExtra("phone", userList.get(position).getString("phone"));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //delete(position);
                getApplicationContext().startActivity(intent);
            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {
                delete(position);

                return false;
            }

        });
    }


        @RequiresApi(api = Build.VERSION_CODES.N)
        public void delete(int position){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String id = userList.get(position).getId();
            db.collection("professors").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            userList.remove(position);
                            List<String> namesList = userList.stream().map(user -> user.getString("firstname")+user.getString("lastname")).collect(Collectors.toList());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, namesList);
                            adapter.notifyDataSetChanged();
                            myListView.setAdapter(adapter);
                            Log.d("Deleted", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("re", "Error deleting document", e);
                        }
                    });
        }




}
