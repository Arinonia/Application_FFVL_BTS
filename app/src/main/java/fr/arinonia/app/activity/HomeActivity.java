package fr.arinonia.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import fr.arinonia.app.R;
import fr.arinonia.app.balise.Balise;
import fr.arinonia.app.balise.BaliseFavoris;
import fr.arinonia.app.balise.Data;
import fr.arinonia.app.balise.DataBaliseFavoris;
import fr.arinonia.app.utils.Constants;
import fr.arinonia.app.utils.IJsonReadable;
import fr.arinonia.app.utils.JsonUtils;

public class HomeActivity extends AppCompatActivity implements IJsonReadable {

    private LinearLayout layout;
    private String userId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_home);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }
        Bundle bundle = this.getIntent().getExtras();

        this.userId = bundle.getString("id");

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#686868"));
            window.setNavigationBarColor(Color.parseColor("#1e1e1e"));
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConstraintLayout constraintLayout = this.findViewById(R.id.constraint_layout);
        constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.scrollpane));


        layout = this.findViewById(R.id.linear_layout);
        ScrollView scrollView = this.findViewById(R.id.scrollbar);

        scrollView.setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            scrollView.setBottomEdgeEffectColor(Color.parseColor("#1e1e1e"));
            scrollView.setTopEdgeEffectColor(Color.parseColor("#1e1e1e"));
        }
        new Thread() {
            @Override
            public void run() {
                jsonDeserialize();
            }
        }.start();

        MaterialButton otherBaliseBtn = this.findViewById(R.id.otherBaliseBtn);
        otherBaliseBtn.setOnClickListener(e -> {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("idUser", userId);
            this.startActivity(intent);
        });
    }


    /**
     * Retire la possibilité de revenir en arrière
     */
    @Override
    public void onBackPressed() {}

    @SuppressLint("SetTextI18n")
    @Override
    public void jsonDeserialize() {
        DataBaliseFavoris data = JsonUtils.gson.fromJson(this.getUrlContents(Constants.API_URL + "/favoris/" + this.userId, this), DataBaliseFavoris.class);

        if (data != null) {
            this.runOnUiThread(() -> {
                for (BaliseFavoris balise : data.getBalises()) {
                    MaterialButton btn = new MaterialButton(this);
                    btn.setText("Balise "  + balise.getNom());
                    btn.setTextColor(Color.WHITE);
                    btn.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
                    btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                    btn.setCornerRadius(0);
                    btn.setStrokeWidth(1);
                    btn.setOnClickListener(e -> {
                        Toast.makeText(HomeActivity.this,"Display Balise", Toast.LENGTH_LONG).show();
                        Intent detailsIntent = new Intent(this, DetailsActivity.class);
                        detailsIntent.putExtra("id", balise.getIdBalise());
                        detailsIntent.putExtra("idUser", userId);
                        this.startActivity(detailsIntent);
                    });
                    layout.addView(btn);
                }
            });
        }

    }
}