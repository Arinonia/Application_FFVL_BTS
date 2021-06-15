package fr.arinonia.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import fr.arinonia.app.R;
import fr.arinonia.app.users.UserData;
import fr.arinonia.app.utils.Constants;
import fr.arinonia.app.utils.IJsonReadable;
import fr.arinonia.app.utils.JsonUtils;

public class LoginActivity extends AppCompatActivity implements IJsonReadable {

    private final Intent homeIntent = new Intent(this, HomeActivity.class);
    private final EditText emailField = this.findViewById(R.id.inputEmail);
    private final EditText passwordField = this.findViewById(R.id.inputPassword);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#686868"));
            window.setNavigationBarColor(Color.parseColor("#1e1e1e"));
        }

        TextView createAccount = this.findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this::setupListeners);
        Button buttonLogin = this.findViewById(R.id.button);
        buttonLogin.setOnClickListener(this::setupListeners);
        TextView forgotPassword = this.findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this::setupListeners);
    }

    @SuppressLint("NonConstantResourceId")
    private void setupListeners(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);

        switch (view.getId()) {
            case R.id.createAccount:
                this.startActivity(registerIntent);
                break;

            case R.id.forgotPassword:
                //TODO Faire un serveur de Mail en Java : https://www.jmdoudoux.fr/java/dej/chap-javamail.htm à lire pour créer le serveur.
                break;

            case R.id.button:
                if (this.emailField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci d'entrer un email !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (this.passwordField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci d'entrer un mot de passe !", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(this::jsonDeserialize).start();
                break;
        }
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void jsonDeserialize() {
        String urlContent = this.getUrlContents(Constants.API_URL + "users/mail/" + this.emailField.getText().toString(), this);
        if (!urlContent.contains("false")) {
            UserData data = JsonUtils.gson.fromJson(urlContent, UserData.class);
            if (data.getPassword().equalsIgnoreCase(this.passwordField.getText().toString())) {
                this.homeIntent.putExtra("id", data.getId());
                this.startActivity(this.homeIntent);
            } else {
                this.runOnUiThread(() -> Toast.makeText(this, "Mot de passe invalide !", Toast.LENGTH_SHORT).show());
            }
        } else {
            this.runOnUiThread(() -> Toast.makeText(this, "Email invalide !", Toast.LENGTH_SHORT).show());
        }
    }
}