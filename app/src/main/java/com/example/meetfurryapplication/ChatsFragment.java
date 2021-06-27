package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference chatRef,userRef;
    private FirebaseAuth mAuth;
    private String userID;

    public ChatsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.activity_chats_fragment, container, false);
        mAuth=FirebaseAuth.getInstance();
        userID=mAuth.getCurrentUser().getUid();
        chatRef= FirebaseDatabase.getInstance().getReference().child("ChatLists").child(userID);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView=view.findViewById(R.id.chats_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ChatLists> options=new FirebaseRecyclerOptions.Builder<ChatLists>()
                .setQuery(chatRef, ChatLists.class).build();

        FirebaseRecyclerAdapter<ChatLists,ChatViewHolder> adapter=new FirebaseRecyclerAdapter<ChatLists, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull ChatLists model) {
                final String userid=getRef(position).getKey();
                final String[] image = {"default_image"};
                userRef.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.hasChild("imageUrl"))
                            {
                                image[0] =dataSnapshot.child("imageUrl").getValue().toString();
                                Picasso.get().load(image[0]).placeholder(R.drawable.default_userpic).into(holder.profile_image);
                            }
                            final String name=dataSnapshot.child("username").getValue().toString();

                            holder.username.setText(name);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent chatIntent=new Intent(getContext(), MessagesActivity.class);
                                    chatIntent.putExtra("visit_user_id",userid);
                                    chatIntent.putExtra("visit_user_name",name);
                                    chatIntent.putExtra("visit_image", image[0]);
                                    startActivity(chatIntent);

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getContext()).inflate(R.layout.item_user_row,parent,false);
                ChatViewHolder chatViewHolder=new ChatViewHolder(view);
                return chatViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_image;
        TextView username;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image=itemView.findViewById(R.id.iur_pic);
            username=itemView.findViewById(R.id.iur_name);

        }
    }
}
