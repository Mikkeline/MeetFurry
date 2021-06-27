package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.Adapter.CommentsAdapter;
import com.example.meetfurryapplication.Model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{
        private EditText commentEdit;
        private Button mAddCommentButton, backBtn;
        private RecyclerView mCommentRecyclerView;
        private FirebaseFirestore firestore;


    ///retrieve comments
        private CommentsAdapter adapter;
        private List<Comment> mList;


    ///
        private String currentUserId;
        private FirebaseAuth auth;


    private TextView mName;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);



        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String post_id = data.getString("postId");



        commentEdit = findViewById(R.id.comment_et);
        mAddCommentButton = findViewById(R.id.add_comment);
        mCommentRecyclerView = findViewById(R.id.Rv);
        backBtn = findViewById(R.id.BackBtn);

        mName = findViewById(R.id.name_tv);
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        currentUserId = user.getUid();

        ///retrieve comments
        mList = new ArrayList<>();





        adapter = new CommentsAdapter(CommentsActivity.this, mList);
        ////



        reference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UsersClass userProfile = snapshot.getValue(UsersClass.class);
                if(userProfile != null){
                    String Username = userProfile.username;
                    mName.setText(Username);


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(CommentsActivity.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        auth = FirebaseAuth.getInstance();


        mCommentRecyclerView.setHasFixedSize(true);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ///retrieve comments
        mCommentRecyclerView.setAdapter(adapter);



        firestore.collection("ForumPostss/" + post_id + "/Comment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : Objects.requireNonNull(value).getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){

                        Comment comments = documentChange.getDocument().toObject(Comment.class);



                        mList.add(comments);


                        adapter.notifyDataSetChanged();
                    } else{

                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });
        //


        backBtn.setOnClickListener(this);

        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEdit.getText().toString();
                String Username = mName.getText().toString();
                if(!comment.isEmpty()){

                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("comment", comment);
                    commentsMap.put("time", FieldValue.serverTimestamp());
                    commentsMap.put("userId", currentUserId);
                    commentsMap.put("username", Username);

                    firestore.collection("ForumPostss/" + post_id + "/Comment").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this, "Comment added!", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(CommentsActivity.this,"error...", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else{

                    Toast.makeText(CommentsActivity.this, "Please enter your comment!", Toast.LENGTH_SHORT).show();
                }

                commentEdit.setText("");
            }
        });



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.BackBtn:

                Toast.makeText(CommentsActivity.this, "Back to forum...", Toast.LENGTH_LONG).show();
               /* Intent i = new Intent(CommentsActivity.this, Forum.class);
                startActivity(i);*/
                finish();
                break;
        }
    }
}




