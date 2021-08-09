package com.example.meetfurryapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.Adapter.MessagesAdapter;
import com.example.meetfurryapplication.Notification.NotificationSender;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends AppCompatActivity {
    private String messageReceiverId, getMessageReceivername,messagereceiverimage,messageSenderId;
    private ImageButton sendFileButton;
    private CircleImageView sendMessageButton;
    private EditText messagesentinput;
    private FirebaseAuth mauth;
    private DatabaseReference RootRef;
    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messageAdapter;
    private RecyclerView usermessagerecyclerview;


    private String savecurrentTime,savecurrentDate;
    private String checker="",myUrl="";
    private StorageTask uploadTask;
    private Uri fileuri;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_activity);
        loadingBar=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
        messageSenderId=mauth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        messageReceiverId=getIntent().getExtras().get("visit_user_id").toString();
        getMessageReceivername =getIntent().getExtras().get("visit_user_name").toString();
        messagereceiverimage=getIntent().getExtras().get("visit_image").toString();

        TextView rName = findViewById(R.id.receiverName);
        CircleImageView rProfileImg = findViewById(R.id.receiverProfileImg);
        ImageButton btn_backChat = findViewById(R.id.btnBackToStartingPage);

        sendMessageButton=findViewById(R.id.send_message_btn);
        sendFileButton=findViewById(R.id.send_files_btn);

        messagesentinput=findViewById(R.id.input_messages);

        messageAdapter=new MessagesAdapter(messagesList);
        usermessagerecyclerview=findViewById(R.id.private_message_list_of_users);
        linearLayoutManager=new LinearLayoutManager(this);
        usermessagerecyclerview.setLayoutManager(linearLayoutManager);
        usermessagerecyclerview.setAdapter(messageAdapter);

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        savecurrentTime=currentTime.format(calendar.getTime());

        rName.setText(getMessageReceivername);
        Picasso.get().load(messagereceiverimage).placeholder(R.drawable.default_userpic).into(rProfileImg);

        btn_backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MessagesActivity.this, "Back to Chat Page....", Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[]=new CharSequence[]{
                        "Images"
                };

                AlertDialog.Builder builder=new AlertDialog.Builder(MessagesActivity.this);
                builder.setTitle("Select File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            checker="image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Select Image"),555);

                        }
                    }
                });

                builder.show();
            }
        });

        RootRef.child("Messages").child(messageSenderId).child(messageReceiverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                usermessagerecyclerview.smoothScrollToPosition(usermessagerecyclerview.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==555 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            loadingBar.setTitle("Sending Image");
            loadingBar.setMessage("please hold on, we are sending the image....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileuri=data.getData();
            if(checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messageSenderRef="Messages/"+messageSenderId+"/"+ messageReceiverId;
                final String messageReceiverRef="Messages/"+ messageReceiverId +"/"+messageSenderId;

                DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageReceiverId).push();
                final String messagePushID=Usermessagekeyref.getKey();

                final StorageReference filepath=storageReference.child(messagePushID+"."+"jpg");
                uploadTask =filepath.putFile(fileuri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl=task.getResult();
                            myUrl=downloadUrl.toString();

                            Map<String, String> messageTextBody=new HashMap<>();
                            messageTextBody.put("message",myUrl);
                            messageTextBody.put("name",fileuri.getLastPathSegment());
                            messageTextBody.put("type",checker);
                            messageTextBody.put("from",messageSenderId);
                            messageTextBody.put("to", messageReceiverId);
                            messageTextBody.put("messageID",messagePushID);
                            messageTextBody.put("time",savecurrentTime);
                            messageTextBody.put("date",savecurrentDate);

                            Map<String, Object> messageBodyDetails =new HashMap<String, Object>();
                            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
                            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

                            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        //Message sent Successfully
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(MessagesActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                                    }
                                    messagesentinput.setText("");
                                }
                            });
                        }
                    }
                });

            }
            else
            {
                loadingBar.dismiss();
                Toast.makeText(this,"please select file",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SendMessage() {
        String messagetext=messagesentinput.getText().toString();
        if(TextUtils.isEmpty(messagetext))
        {
            Toast.makeText(this,"Please enter message first..",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef="Messages/"+messageSenderId+"/"+ messageReceiverId;
            String messageReceiverRef="Messages/"+ messageReceiverId +"/"+messageSenderId;

            DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageReceiverId).push();
            String messagePushID=Usermessagekeyref.getKey();
            Map<String, String> messageTextBody=new HashMap<String, String>();
            messageTextBody.put("message",messagetext);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderId);
            messageTextBody.put("to", messageReceiverId);
            messageTextBody.put("messageID",messagePushID);
            messageTextBody.put("time",savecurrentTime);
            messageTextBody.put("date",savecurrentDate);

            Map<String, Object> messageBodyDetails =new HashMap<>();
            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        String currentuserID = user.getUid();

                        String msg;

                        if(checker.equals("image")){
                            msg = "*Image*";
                        }else{
                            msg = messagesentinput.getText().toString().trim();
                        }
                        final String[] title = new String[1];
                        reference.child(currentuserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                title[0] = snapshot.child("username").getValue().toString();
                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                        String body = msg;
                        FirebaseDatabase.getInstance().getReference()
                                .child("Tokens")
                                .child(messageReceiverId)
                                .child("token")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        String token=snapshot.getValue(String.class);

                                        NotificationSender notificationSender = new NotificationSender(token , title[0], body,getApplicationContext(),
                                                MessagesActivity.this );
                                        notificationSender.SendNotifications();
                                        Toast.makeText(MessagesActivity.this,"Message sent Successfully...",Toast.LENGTH_SHORT).show();
                                    }


                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                    }
                    else
                    {
                        Toast.makeText(MessagesActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                    }
                    messagesentinput.setText("");
                }
            });
        }
    }
}
