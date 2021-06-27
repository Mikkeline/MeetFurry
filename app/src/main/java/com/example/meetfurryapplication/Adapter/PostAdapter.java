package com.example.meetfurryapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetfurryapplication.CommentsActivity;
import com.example.meetfurryapplication.Model.Post;
import com.example.meetfurryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class  PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {





    private List<Post> mList;
    private Activity context;
    private FirebaseFirestore firestore;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String currentUserId;


    public PostAdapter(Activity context, List<Post> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull

    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_post, parent, false);

        firestore = FirebaseFirestore.getInstance();


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        currentUserId = user.getUid();



        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = mList.get(position);
        holder.setPostPic(post.getForumImage());
        holder.setPostCaption(post.getCaption());
        holder.setPostUsername(post.getUsername());


        long milliseconds = post.getTime().getTime();
        String date = DateFormat.format("MM/dd/yyyy", new Date(milliseconds)).toString();
        holder.setPostDate(date);


        //like btn
        String postId = post.PostId;

        holder.likePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("ForumPostss/" + postId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {


                        if(!Objects.requireNonNull(task.getResult()).exists()) {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());
                            firestore.collection("ForumPostss/" + postId + "/Likes").document(currentUserId).set(likesMap);
                        } else{
                            firestore.collection("ForumPostss/" + postId + "/Likes").document(currentUserId).delete();

                        }

                    }
                });

            }
        });
        //like color change
        firestore.collection("ForumPostss/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(error == null){

                    if(Objects.requireNonNull(value).exists()){
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.after_like));

                    }else{
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.before_like));
                    }
                }
            }
        });

        //like count
        firestore.collection("ForumPostss/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(!Objects.requireNonNull(value).isEmpty()){
                    int count = value.size();
                    holder.setPostLikes(count);

                }else{

                    holder.setPostLikes(0);
                }
            }
        });




    //comments implementation

        holder.commentsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, CommentsActivity.class);

                Bundle data1 = new Bundle();
                data1.putString("postId", postId);
                commentIntent.putExtras(data1);
                context.startActivity(commentIntent);

                //commentIntent.putExtra("postId", postId);

              //  context.startActivity(commentIntent);
            }
        });


        //comment count
        firestore.collection("ForumPostss/" + postId + "/Comment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(!Objects.requireNonNull(value).isEmpty()){
                    int count = value.size();
                    holder.setCommentCount(count);

                }else{

                    holder.setCommentCount(0);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class PostViewHolder extends RecyclerView.ViewHolder{

        ImageView postPic, commentsPic, likePic;
        TextView postUsername, postDate, postCaption, postLikes, commentCount;

        View mView;

        public PostViewHolder(@NonNull View itemView){

            super(itemView);
            mView = itemView;



            //likes
            likePic = mView.findViewById(R.id.like_btn);


            //Comments
            commentsPic = mView.findViewById(R.id.comments_post);
        }






        public void setPostPic(String img_url){

            postPic = mView.findViewById(R.id.user_post);
            Glide.with(context).load(img_url).into(postPic);

        }

        public void setPostUsername(String Username){
            postUsername = mView.findViewById(R.id.UserName_tv);
            postUsername.setText(Username);

        }
        public void setPostDate(String date){
            postDate = mView.findViewById(R.id.date_tv);
            postDate.setText(date);

        }
        public void setPostCaption(String caption){
            postCaption = mView.findViewById(R.id.caption_tv);
            postCaption.setText(caption);

        }

        public void setPostLikes(int count) {
            postLikes = mView.findViewById(R.id.count_tv);
            postLikes.setText(Integer.toString(count) + " likes");
        }

        public void setCommentCount(int count) {
            commentCount = mView.findViewById(R.id.comment_count_tv);
            commentCount.setText(Integer.toString(count) + " comments");

        }
    }


}
