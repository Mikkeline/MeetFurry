package com.example.meetfurryapplication.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.Model.Comment;
import com.example.meetfurryapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {


    private Activity context;

    private List<Comment> commentsList;






    public CommentsAdapter(Activity context, List<Comment> commentsList){

        this.context = context;
        this.commentsList = commentsList;




    }

    @NotNull
    @Override

    public CommentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_comment, parent, false);
        return new CommentsViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentsViewHolder holder, int position) {

        Comment comments = commentsList.get(position);

        holder.setmComment(comments.getComment());

        holder.setCommentUsername(comments.getUsername());


    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{

        TextView mComment, commentUsername;
        View mView;

        public CommentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setmComment(String comment){

            mComment = mView.findViewById(R.id.comment_tv);
            mComment.setText(comment);
        }

        public void setCommentUsername(String username){
            commentUsername = mView.findViewById(R.id.UserName_tv);
            commentUsername.setText(username);

        }






    }


}
