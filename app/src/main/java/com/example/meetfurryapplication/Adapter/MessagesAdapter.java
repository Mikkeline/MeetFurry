package com.example.meetfurryapplication.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.R;
import com.example.meetfurryapplication.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter <MessagesAdapter.MessageViewHolder> {

    private List<Messages> UserMessageList;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    public MessagesAdapter(List<Messages> UserMessageList)
    {
        this.UserMessageList=UserMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout,parent,false);
        MessageViewHolder messageViewHolder= new MessageViewHolder(view);
        mAuth=FirebaseAuth.getInstance();

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        String messagesenderid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        final Messages messages=UserMessageList.get(position);

        String fromuserid=messages.getFrom();
        String frommessagetype=messages.getType();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(fromuserid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("imageUrl"))
                {
                    String receiverprofileimage= Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString();
                    Picasso.get().load(receiverprofileimage).placeholder(R.drawable.default_userpic).into(holder.receiverprofileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.receivermessagetext.setVisibility(View.INVISIBLE);
        holder.receiverprofileimage.setVisibility(View.INVISIBLE);
        holder.sendermessagetext.setVisibility(View.INVISIBLE);
        holder.messageSenderPicture.setVisibility(View.INVISIBLE);
        holder.messageReceiverPicture.setVisibility(View.INVISIBLE);
        if(frommessagetype.equals("text"))
        {
            if(fromuserid.equals(messagesenderid))
            {
                holder.sendermessagetext.setVisibility(View.VISIBLE);
                holder.sendermessagetext.setBackgroundResource(R.drawable.sender_shape);
                holder.sendermessagetext.setText(messages.getMessage()+"\n \n"+messages.getTime()+" - "+messages.getDate());
            }
            else
            {
                holder.receivermessagetext.setVisibility(View.VISIBLE);
                holder.receiverprofileimage.setVisibility(View.VISIBLE);


                holder.receivermessagetext.setBackgroundResource(R.drawable.receiver_shape);
                holder.receivermessagetext.setText(messages.getMessage()+"\n \n"+messages.getTime()+" - "+messages.getDate());
            }
        }
        else  if(frommessagetype.equals("image"))
        {
            if(fromuserid.equals(messagesenderid))
            {
                holder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageSenderPicture);
            }
            else
            {

                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                holder.receiverprofileimage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageReceiverPicture);
            }
        }
        if(fromuserid.equals(messagesenderid))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserMessageList.get(position).getType().equals("text") )
                    {
                        CharSequence[] options =new CharSequence[]
                                {
                                        "Delete for me","Cancel","Delete for everyone"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    deleteSentMessage(position,holder);

                                }else if(which==1)
                                {
                                    //for cancel do not do anything
                                }else if(which==2)
                                {
                                    deleteMessageForEveryone(position,holder);

                                }

                            }
                        });

                        builder.show();
                    }
                    else  if(UserMessageList.get(position).getType().equals("image") )
                    {
                        CharSequence[] options =new CharSequence[]
                                {
                                        "Delete for me","Cancel","Delete for everyone"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    deleteSentMessage(position,holder);

                                }else if(which==1)
                                {

                                }else if(which==2)
                                {
                                    deleteMessageForEveryone(position,holder);
                                }

                            }
                        });

                        builder.show();
                    }
                }
            });
        }
        else
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserMessageList.get(position).getType().equals("text") )
                    {
                        CharSequence[] options =new CharSequence[]
                                {
                                        "Delete for me","Cancel"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    deleteReceiveMessage(position,holder);

                                }else if(which==1)
                                {
                                    //for cancel do not do anything
                                }

                            }
                        });

                        builder.show();
                    }
                    else  if(UserMessageList.get(position).getType().equals("image") )
                    {
                        CharSequence[] options =new CharSequence[]
                                {
                                        "Delete for me","Cancel"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    deleteReceiveMessage(position,holder);

                                }else if(which==1)
                                {
                                }

                            }
                        });

                        builder.show();
                    }
                }
            });
        }
    }
    private void deleteSentMessage(final int position,final MessageViewHolder holder)
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        rootRef.child("MessagesActivity").child(UserMessageList.get(position).getFrom())
                .child(UserMessageList.get(position).getTo()).child(UserMessageList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    notifyItemRemoved(position);
                    UserMessageList.remove(position);
                    notifyItemRangeChanged(position, UserMessageList.size());
                    Toast.makeText(holder.itemView.getContext(),"Message deleted...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteReceiveMessage(final int position,final MessageViewHolder holder)
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        rootRef.child("MessagesActivity").child(UserMessageList.get(position).getTo())
                .child(UserMessageList.get(position).getFrom()).child(UserMessageList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    notifyItemRemoved(position);
                    UserMessageList.remove(position);
                    notifyItemRangeChanged(position, UserMessageList.size());
                    Toast.makeText(holder.itemView.getContext(),"Message deleted...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteMessageForEveryone(final int position,final MessageViewHolder holder)
    {
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        rootRef.child("MessagesActivity").child(UserMessageList.get(position).getFrom())
                .child(UserMessageList.get(position).getTo()).child(UserMessageList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
                    rootRef.child("MessagesActivity").child(UserMessageList.get(position).getTo())
                            .child(UserMessageList.get(position).getFrom()).child(UserMessageList.get(position).getMessageID())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                notifyItemRemoved(position);
                                UserMessageList.remove(position);
                                notifyItemRangeChanged(position, UserMessageList.size());
                                Toast.makeText(holder.itemView.getContext(),"Message deleted...",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(),"Error...",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return UserMessageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView sendermessagetext,receivermessagetext;
        public CircleImageView receiverprofileimage;
        public ImageView messageSenderPicture,messageReceiverPicture;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermessagetext=itemView.findViewById(R.id.msg_sText);
            receivermessagetext=itemView.findViewById(R.id.msg_rText);
            receiverprofileimage=itemView.findViewById(R.id.msg_rProfilePic);
            messageSenderPicture=itemView.findViewById(R.id.msg_sImageView);
            messageReceiverPicture=itemView.findViewById(R.id.msg_rImageView);
        }
    }

}
