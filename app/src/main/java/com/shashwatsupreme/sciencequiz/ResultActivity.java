package com.shashwatsupreme.sciencequiz;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashwatsupreme.sciencequiz.Adapter.ResultGridAdapter;
import com.shashwatsupreme.sciencequiz.Common.Common;
import com.shashwatsupreme.sciencequiz.Common.SpaceDecoration;
import com.shashwatsupreme.sciencequiz.DBHelper.OnlineDBHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ResultActivity extends AppCompatActivity
{
    Toolbar toolbar;
    TextView txt_timer, txt_result, txt_right_answer, txt_percent;
    Button btn_filter_total, btn_filter_right, btn_filter_wrong, btn_filter_no_answer;
    RecyclerView recycler_result;
    String name = null;
    int highscoretodisplay = 0;
    ResultGridAdapter adapter, filtered_adapter;

    //personal modifications
    Button btn_sign_out;


    BroadcastReceiver backToQuestion = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().toString().equals(Common.KEY_BACK_FROM_RESULT))
            {
                int question = intent.getIntExtra(Common.KEY_BACK_FROM_RESULT, -1);
                goBackActivityWithQuestion(question);
                
            }
        }
    };

    private void goBackActivityWithQuestion(int question)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Common.KEY_BACK_FROM_RESULT, question);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(backToQuestion, new IntentFilter(Common.KEY_BACK_FROM_RESULT));


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("RESULT");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_result = findViewById(R.id.txt_result);
        txt_right_answer = findViewById(R.id.txt_right_answer);
        txt_timer = findViewById(R.id.txt_time);

        btn_filter_no_answer = findViewById(R.id.btn_filter_no_answer);
        btn_filter_right = findViewById(R.id.btn_filter_right_answer);
        btn_filter_wrong = findViewById(R.id.btn_filter_wrong_answer);
        btn_filter_total = findViewById(R.id.btn_filter_total);

        recycler_result = findViewById(R.id.recycler_result);
        recycler_result.setHasFixedSize(true);
        recycler_result.setLayoutManager( new GridLayoutManager(this, 3));

        //Set Adapter
        adapter = new ResultGridAdapter(this, Common.answerSheetList);
        recycler_result.addItemDecoration(new SpaceDecoration(4));
        recycler_result.setAdapter(adapter);

        txt_timer.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Common.timer),
                TimeUnit.MILLISECONDS.toSeconds(Common.timer) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Common.timer))));
        txt_right_answer.setText(new StringBuilder("").append(Common.right_answer_count).append("/")
        .append(Common.questionList.size()));

        btn_filter_total.setText(new StringBuilder("").append(Common.questionList.size()));
        btn_filter_right.setText(new StringBuilder("").append(Common.right_answer_count));
        btn_filter_wrong.setText(new StringBuilder("").append(Common.wrong_answer_count));
        btn_filter_no_answer.setText(new StringBuilder("").append(Common.no_answer_count));

        //Calculate result
//        if(Common.questionList.size() != 0)
            final int percent = ((Common.right_answer_count * 100) / Common.questionList.size());
        if(percent > 85)
            txt_result.setText("EXCELLENT");
        else if(percent > 75)
            txt_result.setText("GOOD");
        else if(percent > 60)
            txt_result.setText("FAIR");
        else if(percent > 50)
            txt_result.setText("SATISFACTORY");
        else if(percent > 40)
            txt_result.setText("POOR");
        else
            txt_result.setText("FAILED");

        txt_percent = findViewById(R.id.txt_percent);
        txt_percent.setText(percent+"%");

        //Event filter
        btn_filter_total.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(adapter == null)
                {
                    adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetList);
                    recycler_result.setAdapter(adapter);
                }
                else
                    recycler_result.setAdapter(adapter);
            }
        });

        btn_filter_no_answer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Common.answerSheetListFiltered.clear();
                for(int i = 0; i < Common.answerSheetList.size(); i++)
                {
                    if((Common.answerSheetList.get(i) != null)&&(Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.NO_ANSWER))
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFiltered);
                recycler_result.setAdapter(filtered_adapter);
            }
        });
        btn_filter_wrong.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Common.answerSheetListFiltered.clear();
                for(int i = 0; i < Common.answerSheetList.size(); i++)
                {
                    if((Common.answerSheetList.get(i) != null)&&(Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER))
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFiltered);
                recycler_result.setAdapter(filtered_adapter);
            }
        });
        btn_filter_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Common.answerSheetListFiltered.clear();
                for(int i = 0; i < Common.answerSheetList.size(); i++)
                {
                    if((Common.answerSheetList.get(i) != null)&&(Common.answerSheetList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER))
                        Common.answerSheetListFiltered.add(Common.answerSheetList.get(i));
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Common.answerSheetListFiltered);
                recycler_result.setAdapter(filtered_adapter);

            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String useruid = user.getUid();
        String email;
        Uri photoUrl, photoUrl_provider;
        String providerId, name_provider, email_provider;


        if (user != null)
        {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();

            Common.name = name;
            Common.email = email;
            Common.photo_url = photoUrl;

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.


            Common.user_id = useruid;

            for (UserInfo profile : user.getProviderData())
            {
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();

                // Name, email address, and profile photo Url
                name_provider = profile.getDisplayName();
                email_provider = profile.getEmail();
                photoUrl_provider = profile.getPhotoUrl();

                Common.name_provider = name_provider;
                Common.email_provider = email_provider;
                Common.photo_url_provider = photoUrl_provider;
            }

        }
        else
        {
            Common.user_Phone_No = user.getPhoneNumber();
        }

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("ScienceQuiz");

        final OnlineDBHelper onlineDBHelper = OnlineDBHelper.getInstance(this, FirebaseDatabase.getInstance());


        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String path = Common.email;
                path = path.replace(".", "dot");
                path = path.replace("@", "attherateof");
                path = path.replace("_", "underscore");
                path = path.replace("#", "hashtag");
                path = path.replace("*", "asterisk");
                path = path.replace("-", "dash");
                if(dataSnapshot.child("/Users/").hasChild(path))
                {


//                    Toast.makeText(ResultActivity.this, "read", Toast.LENGTH_SHORT).show();
                    Common.userData = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId()));
                    switch (Common.selectedCategory.getClassId())
                    {
                        case 6:
                            if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getPhysics6high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getChemistry6high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getBiology6high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getMixed6high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            break;
                        case 7:
                            if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getPhysics7high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getChemistry7high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getBiology7high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getMixed7high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            break;
                        case 8:
                            if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getPhysics8high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getChemistry8high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getBiology8high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                            {
                                highscoretodisplay = onlineDBHelper.readUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId())).getMixed8high();
                                if(percent > highscoretodisplay)
                                    highscoretodisplay = percent;
                            }
                            break;

                    }

                    onlineDBHelper.writeUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId()), highscoretodisplay, false);


                }
                else
                {
//                    Toast.makeText(ResultActivity.this, "write", Toast.LENGTH_SHORT).show();
                    onlineDBHelper.writeUserData("Users", useruid, Common.selectedCategory.getName(), String.valueOf(Common.selectedCategory.getClassId()), percent, true);
                    highscoretodisplay = percent;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(ResultActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
//        Log.d("1", "There 1");
//        Toast.makeText(ResultActivity.this, "There 1", Toast.LENGTH_SHORT).show();
        switch (item.getItemId())
        {

            case R.id.menu_do_quiz_again:
//                Log.d("2", "There 2");
//                Toast.makeText(ResultActivity.this, "There 2", Toast.LENGTH_SHORT).show();
                doQuizAgain();
                break;
            case R.id.menu_view_answer:
//                Log.d("3", "There 3");
//                Toast.makeText(ResultActivity.this, "There 3", Toast.LENGTH_SHORT).show();
                viewQuizAnswer();
                break;
            case R.id.menu_high_score:
                if(OnlineDBHelper.getInstance(this, FirebaseDatabase.getInstance()) != null)
                {
                    new MaterialStyledDialog.Builder(ResultActivity.this)
                            .setTitle("Hi "+name)
                            .setIcon(R.drawable.ic_mood_white_24dp)
                            .setDescription("Your current high score in class "+Common.selectedCategory.getClassId()+" of "+Common.selectedCategory.getName()+" category is "+highscoretodisplay+"%")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback()
                            {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                                {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                break;
            case R.id.btn_sign_out:
                AuthUI.getInstance()
                        .signOut(ResultActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            public void onComplete(@NonNull Task<Void> task)
                            {
//                                btn_sign_out.setEnabled(false);
                                startActivity(new Intent(ResultActivity.this, MainActivity.class));

                            }
                        }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(ResultActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case android.R.id.home:
//                Log.d("4", "There 4");
//                Toast.makeText(ResultActivity.this, "There 4", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Delete all activity
                startActivity(intent);
                break;
        }
        return true;
    }

    private void viewQuizAnswer()
    {
//        Log.d("5", "There 5");
//        Toast.makeText(ResultActivity.this, "There 5", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("action", "viewquizanswer");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void doQuizAgain()
    {
        new MaterialStyledDialog.Builder(ResultActivity.this)
                .setTitle("Do quiz again?")
                .setIcon(R.drawable.ic_mood_black_24dp)
                .setDescription("Do you really want to delete this data?")
                .setNegativeText("No")
                .onNegative(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("action", "doitagain");
                        setResult(Activity.RESULT_OK, returnIntent);
//                        Log.d("6", "There 6");
//                        Toast.makeText(ResultActivity.this, "There 6", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).show();
    }
}
