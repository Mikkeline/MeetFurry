package com.example.meetfurryapplication;

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
public class ContactsFragment extends Fragment {

    private View view;
    private RecyclerView myrecyclerview;
    private DatabaseReference chatListsRef,userRef;
    private FirebaseAuth mauth;
    private String currentUserId;
    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.activity_contacts_fragment, container, false);
        myrecyclerview=view.findViewById(R.id.contacts_recyclerview_list);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        mauth=FirebaseAuth.getInstance();
        currentUserId=mauth.getCurrentUser().getUid();

        chatListsRef= FirebaseDatabase.getInstance().getReference().child("ChatLists").child(currentUserId);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<ChatLists>()
                .setQuery(chatListsRef,ChatLists.class)
                .build();

        FirebaseRecyclerAdapter<ChatLists,ContactsViewHolder> adapter=new FirebaseRecyclerAdapter<ChatLists, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull ChatLists model) {

                String userId=getRef(position).getKey();
                userRef.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.hasChild("imageUrl"))
                            {
                                String image=dataSnapshot.child("imageUrl").getValue().toString();
                                String name=dataSnapshot.child("name").getValue().toString();

                                holder.username.setText(name);
                                Picasso.get().load(image).placeholder(R.drawable.default_userpic).into(holder.profilepicture);
                            }
                            else
                            {

                                String name=dataSnapshot.child("name").getValue().toString();

                                holder.username.setText(name);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_row,parent,false);
                ContactsViewHolder viewHolder=new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        myrecyclerview.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView username,userstatus;
        CircleImageView profilepicture;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.iur_name);
            profilepicture=itemView.findViewById(R.id.iur_pic);
        }
    }
}
