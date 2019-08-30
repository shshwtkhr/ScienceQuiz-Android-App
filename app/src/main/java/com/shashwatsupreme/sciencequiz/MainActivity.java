package com.shashwatsupreme.sciencequiz;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashwatsupreme.sciencequiz.Adapter.CategoryAdapter;
import com.shashwatsupreme.sciencequiz.Common.Common;
import com.shashwatsupreme.sciencequiz.Common.SpaceDecoration;
import com.shashwatsupreme.sciencequiz.DBHelper.DBHelper;

import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
{

    Toolbar toolbar;
    RecyclerView recycler_category;

    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    Button btn_sign_out;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Paper
        Paper.init(this);

        //Get value online mode
        Common.isOnlineMode = Paper.book().read(Common.KEY_SAVE_ONLINE_MODE, false); //Default false

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Science Quiz");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);

        recycler_category = findViewById(R.id.recyler_category);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(this, 1));

        //Get Screen Height
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels / 8;//Max size of item in Category
        CategoryAdapter adapter = new CategoryAdapter(MainActivity.this, DBHelper.getInstance(this).getAllCategories());
        int spaceInPixel = 4;
        recycler_category.addItemDecoration(new SpaceDecoration(spaceInPixel));
        recycler_category.setAdapter(adapter);

//        btn_sign_out = findViewById(R.id.btn_sign_out);
//        btn_sign_out.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // Sign out
//                AuthUI.getInstance()
//                        .signOut(MainActivity.this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>()
//                        {
//                            public void onComplete(@NonNull Task<Void> task)
//                            {
//                                btn_sign_out.setEnabled(false);
//                                showSignInOptions();
//                            }
//                        }).addOnFailureListener(new OnFailureListener()
//                {
//                    @Override
//                    public void onFailure(@NonNull Exception e)
//                    {
//                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                // [END auth_fui_signout]
//            }
//        });

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        showSignInOptions();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in, now get user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "Signed in as: "+((user.getEmail()!=null)?user.getEmail():user.getPhoneNumber()), Toast.LENGTH_SHORT).show();
                // ...
//                btn_sign_out.setEnabled(true);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSignInOptions()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Mytheme)
                        .build(),MY_REQUEST_CODE
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menu_settings)
        {
            showSettings();
        }
        if(item.getItemId() == R.id.btn_sign_out)
        {
            AuthUI.getInstance()
                    .signOut(MainActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        public void onComplete(@NonNull Task<Void> task)
                        {
//                            btn_sign_out.setEnabled(false);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return true;
    }

    private void showSettings()
    {
        View setting_layout = LayoutInflater.from(this).inflate(R.layout.settings_layout, null);
        final CheckBox ckb_online_mode = setting_layout.findViewById(R.id.ckb_online_mode);

        //Load data from paper, if not available just init default false
        ckb_online_mode.setChecked(Paper.book().read(Common.KEY_SAVE_ONLINE_MODE, false));

        //Show Dialog
        new MaterialStyledDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.ic_settings_white_24dp)
                .setTitle("Settings")
                .setDescription("Please choose action")
                .setCustomView(setting_layout)
                .setNegativeText("DISMISS")
                .onNegative(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("SAVE")
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        if(ckb_online_mode.isChecked())
                        {
                            Common.isOnlineMode = true;
                        }
                        else
                        {
                            Common.isOnlineMode = false;
                        }

                        //Save
                        Paper.book().write(Common.KEY_SAVE_ONLINE_MODE, ckb_online_mode.isChecked());
                    }
                }).show();
    }
}
