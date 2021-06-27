package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFilterFunction extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    private FirebaseUser user;
    private String userID;


    private Button dogBtn, catBtn, birdBtn, fishBtn, rabbitBtn, typeOthersBtn,
            maleBtn, femaleBtn, neutralBtn, blackBtn, brownBtn, whiteBtn,
            blackWBtn, brownWBtn, colorOthersBtn, longHairBtn, shortHairBtn, mixBtn,
            applyBtn, cancelBtn;


    private TextView tv1, tv2, tv3, tv4, tv5;
    private int type = 0;
    private int gender = 0;
    private int color = 0;
    private int breed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_filter_function);


        dogBtn = findViewById(R.id.dog_btn);
        dogBtn.setOnClickListener(this);

        catBtn = findViewById(R.id.cat_btn);
        catBtn.setOnClickListener(this);

        birdBtn = findViewById(R.id.bird_btn);
        birdBtn.setOnClickListener(this);

        fishBtn = findViewById(R.id.fish_btn);
        fishBtn.setOnClickListener(this);

        rabbitBtn = findViewById(R.id.rabbit_btn);
        rabbitBtn.setOnClickListener(this);



        typeOthersBtn = findViewById(R.id.others_btn);
        typeOthersBtn.setOnClickListener(this);

        maleBtn = findViewById(R.id.male_btn);
        maleBtn.setOnClickListener(this);

        femaleBtn = findViewById(R.id.female_btn);
        femaleBtn.setOnClickListener(this);

        neutralBtn = findViewById(R.id.neutral_btn);
        neutralBtn.setOnClickListener(this);

        blackBtn = findViewById(R.id.black_btn);
        blackBtn.setOnClickListener(this);

        brownBtn = findViewById(R.id.brown_btn);
        brownBtn.setOnClickListener(this);

        whiteBtn = findViewById(R.id.white_btn);
        whiteBtn.setOnClickListener(this);

        blackWBtn = findViewById(R.id.blackWhiteBtn);
        blackWBtn.setOnClickListener(this);

        brownWBtn = findViewById(R.id.brownWhiteBtn);
        brownWBtn.setOnClickListener(this);

        colorOthersBtn = findViewById(R.id.othersBtn);
        colorOthersBtn.setOnClickListener(this);

        longHairBtn = findViewById(R.id.longHairBtn);
        longHairBtn.setOnClickListener(this);

        shortHairBtn = findViewById(R.id.shortHairBtn);
        shortHairBtn.setOnClickListener(this);

        mixBtn = findViewById(R.id.mixBtn);
        mixBtn.setOnClickListener(this);

        applyBtn = findViewById(R.id.ApplyBtn);
        applyBtn.setOnClickListener(this);

        cancelBtn = findViewById(R.id.CancelBtn);
        cancelBtn.setOnClickListener(this);

        tv1 = findViewById(R.id.textView2);
        tv2 = findViewById(R.id.textView3);
        tv3 = findViewById(R.id.textView4);
        tv4 = findViewById(R.id.textView5);
        tv5 = findViewById(R.id.textView6);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dog_btn:

                type = 1;


                catBtn.setBackgroundColor(getResources().getColor(R.color.white));
                birdBtn.setBackgroundColor(getResources().getColor(R.color.white));
                fishBtn.setBackgroundColor(getResources().getColor(R.color.white));
                rabbitBtn.setBackgroundColor(getResources().getColor(R.color.white));
                typeOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                dogBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv1.setText("Dog");

                break;

            case R.id.cat_btn:

                type = 1;

                dogBtn.setBackgroundColor(getResources().getColor(R.color.white));

                birdBtn.setBackgroundColor(getResources().getColor(R.color.white));
                fishBtn.setBackgroundColor(getResources().getColor(R.color.white));
                rabbitBtn.setBackgroundColor(getResources().getColor(R.color.white));
                typeOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                catBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv1.setText("Cat");

                break;

            case R.id.bird_btn:

                type = 1;

                dogBtn.setBackgroundColor(getResources().getColor(R.color.white));
                catBtn.setBackgroundColor(getResources().getColor(R.color.white));

                fishBtn.setBackgroundColor(getResources().getColor(R.color.white));
                rabbitBtn.setBackgroundColor(getResources().getColor(R.color.white));
                typeOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                birdBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv1.setText("Bird");

                break;

            case R.id.fish_btn:

                type = 1;

                dogBtn.setBackgroundColor(getResources().getColor(R.color.white));
                catBtn.setBackgroundColor(getResources().getColor(R.color.white));
                birdBtn.setBackgroundColor(getResources().getColor(R.color.white));

                rabbitBtn.setBackgroundColor(getResources().getColor(R.color.white));
                typeOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                fishBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv1.setText("Fish");

                break;

            case R.id.rabbit_btn:

                type = 1;

                dogBtn.setBackgroundColor(getResources().getColor(R.color.white));
                catBtn.setBackgroundColor(getResources().getColor(R.color.white));
                birdBtn.setBackgroundColor(getResources().getColor(R.color.white));
                fishBtn.setBackgroundColor(getResources().getColor(R.color.white));

                typeOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                rabbitBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv1.setText("Rabbit");

                break;



            case R.id.others_btn:

                type = 1;
                dogBtn.setBackgroundColor(getResources().getColor(R.color.white));
                catBtn.setBackgroundColor(getResources().getColor(R.color.white));
                birdBtn.setBackgroundColor(getResources().getColor(R.color.white));
                fishBtn.setBackgroundColor(getResources().getColor(R.color.white));
                rabbitBtn.setBackgroundColor(getResources().getColor(R.color.white));


                typeOthersBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv1.setText("Others");

                break;


            case R.id.male_btn:

                gender = 1;

                femaleBtn.setBackgroundColor(getResources().getColor(R.color.white));
                neutralBtn.setBackgroundColor(getResources().getColor(R.color.white));

                maleBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv2.setText("Male");

                break;

            case R.id.female_btn:

                gender = 1;

                maleBtn.setBackgroundColor(getResources().getColor(R.color.white));
                neutralBtn.setBackgroundColor(getResources().getColor(R.color.white));

                femaleBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv2.setText("Female");

                break;

            case R.id.neutral_btn:

                gender = 1;

                maleBtn.setBackgroundColor(getResources().getColor(R.color.white));
                femaleBtn.setBackgroundColor(getResources().getColor(R.color.white));

                neutralBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv2.setText("Gender-neutral");

                break;


            case R.id.black_btn:

                color = 1;
                brownBtn.setBackgroundColor(getResources().getColor(R.color.white));
                whiteBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                brownWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                colorOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                blackBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv3.setText("Black");

                break;

            case R.id.brown_btn:

                color = 1;
                brownWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                whiteBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackBtn.setBackgroundColor(getResources().getColor(R.color.white));
                colorOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                brownBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv3.setText("Brown");

                break;


            case R.id.white_btn:

                color = 1;
                brownWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                brownBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackBtn.setBackgroundColor(getResources().getColor(R.color.white));
                colorOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                whiteBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv3.setText("White");

                break;



            case R.id.blackWhiteBtn:

                color = 1;
                brownWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                brownBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackBtn.setBackgroundColor(getResources().getColor(R.color.white));
                whiteBtn.setBackgroundColor(getResources().getColor(R.color.white));
                colorOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                blackWBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv3.setText("Black and white");

                break;

            case R.id.brownWhiteBtn:

                color = 1;
                blackWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                brownBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackBtn.setBackgroundColor(getResources().getColor(R.color.white));
                whiteBtn.setBackgroundColor(getResources().getColor(R.color.white));
                colorOthersBtn.setBackgroundColor(getResources().getColor(R.color.white));

                brownWBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv3.setText("Brown and white");

                break;


            case R.id.othersBtn:

                color = 1;
                blackWBtn.setBackgroundColor(getResources().getColor(R.color.white));
                brownBtn.setBackgroundColor(getResources().getColor(R.color.white));
                blackBtn.setBackgroundColor(getResources().getColor(R.color.white));
                whiteBtn.setBackgroundColor(getResources().getColor(R.color.white));
                brownWBtn.setBackgroundColor(getResources().getColor(R.color.white));

                colorOthersBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv3.setText("Others");

                break;


            case R.id.longHairBtn:

                breed = 1;

                shortHairBtn.setBackgroundColor(getResources().getColor(R.color.white));
                mixBtn.setBackgroundColor(getResources().getColor(R.color.white));

                longHairBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv4.setText("Domestic Long Hair");

                break;

            case R.id.shortHairBtn:

                breed = 1;

                longHairBtn.setBackgroundColor(getResources().getColor(R.color.white));
                mixBtn.setBackgroundColor(getResources().getColor(R.color.white));

                shortHairBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv4.setText("Domestic Short Hair");

                break;

            case R.id.mixBtn:

                breed = 1;

                longHairBtn.setBackgroundColor(getResources().getColor(R.color.white));
                shortHairBtn.setBackgroundColor(getResources().getColor(R.color.white));

                mixBtn.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                tv4.setText("Mix");

                break;


            case R.id.ApplyBtn:

                if (type != 1) {

                    Toast.makeText(this, "Please select the type of pets you want!!", Toast.LENGTH_SHORT).show();
                }

                if (gender != 1) {

                    Toast.makeText(this, "Please select the gender of pets you want!!", Toast.LENGTH_SHORT).show();
                }

                if (color != 1) {

                    Toast.makeText(this, "Please select the color of pets you want!!", Toast.LENGTH_SHORT).show();
                }

                if (breed != 1) {

                    Toast.makeText(this, "Please select the breed of pets you want!!", Toast.LENGTH_SHORT).show();
                }


                if (type == 1 && color == 1 && breed == 1 && gender == 1) {
                    tv5.setText(tv1.getText().toString() + tv2.getText().toString() + tv3.getText().toString() + tv4.getText().toString());
                    Intent intent = new Intent(UserFilterFunction.this, FilteredUserPage.class);
                    Bundle data1 = new Bundle();
                    data1.putString("Filter", tv5.getText().toString());
                    intent.putExtras(data1);
                    startActivity(intent);
                }

                break;



            case R.id.CancelBtn:
                startActivity(new Intent(UserFilterFunction.this, userHomepage.class) );
                break;
        }
    }
}