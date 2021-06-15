package fr.arinonia.app.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;


import fr.arinonia.app.R;
import fr.arinonia.app.components.UnderlineTextView;
import fr.arinonia.app.exception.RegisterUnavailableException;
import fr.arinonia.app.users.UserData;
import fr.arinonia.app.utils.Constants;
import fr.arinonia.app.utils.IJsonReadable;
import fr.arinonia.app.utils.JsonUtils;
import fr.arinonia.app.utils.RequestResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;


public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#686868"));
            window.setNavigationBarColor(Color.parseColor("#1e1e1e"));
        }

        Button registerButton = this.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this::setupListener);
        UnderlineTextView alwreadyHaveAccountText = this.findViewById(R.id.HaveAccountRegister);
        alwreadyHaveAccountText.setOnClickListener(this::setupListener);
    }

    @SuppressLint("NonConstantResourceId")
    private void setupListener(View view) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        Intent loginIntent = new Intent(this, LoginActivity.class);

        switch (view.getId()) {
            case R.id.registerButton :
                EditText usernameField = this.findViewById(R.id.nomRegister);
                EditText userFirstNameField = this.findViewById(R.id.prenomRegister);
                EditText emailField = this.findViewById(R.id.emailRegister);
                EditText passwordField = this.findViewById(R.id.passwordRegister);
                EditText passwordConfirmField = this.findViewById(R.id.confirmPasswordRegister);

                if (usernameField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci d'entrer votre nom !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userFirstNameField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci d'entrer votre prénom !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci d'entrer votre email !", Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO Check pattern of emailField (trouvé sur google : "^[A-Za-z0-9+_.-]+@(.+)$" fais à la main : "^[\\w!#$%&amp;’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
                if (passwordField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci d'entrer votre mot de passe !", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Check if password contain maj min number
                if (passwordConfirmField.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Merci de confirmer votre mot de passe !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordField.getText().toString().equals(passwordConfirmField.getText().toString())) {
                    passwordField.setText("");
                    passwordConfirmField.setText("");
                    Toast.makeText(this, "Les deux mots de passe ne sont pas identique !", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(() -> {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            RequestResponse result = sendJsonPostRequest(new URL(Constants.API_URL + "/users"), JsonUtils.credentialsToJson(usernameField.getText().toString() ,userFirstNameField.getText().toString(),emailField.getText().toString(),passwordConfirmField.getText().toString()), null);
                            if(result.isSuccessful()){
                                System.out.println("Ajout d'un utilisateur dans la bdd");
                                UserData userData =  getUserData(emailField.getText().toString());
                                homeIntent.putExtra("id", userData.getId());
                                this.startActivity(homeIntent);
                            } else {
                                System.err.println("Error");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (RegisterUnavailableException e) {
                        e.printStackTrace();
                    }
                }).start();

                break;
            case R.id.HaveAccountRegister :
                this.startActivity(loginIntent);
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static RequestResponse sendJsonPostRequest(URL requestUrl, String payload, Proxy proxy) throws RegisterUnavailableException {
        HttpURLConnection connection = null;
        try {
            byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
            connection = (HttpURLConnection) (proxy != null ? requestUrl.openConnection(proxy) : requestUrl.openConnection());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset","UTF-8");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Content-Length",String.valueOf(payloadBytes.length));
            connection.setUseCaches(false);
            OutputStream out = connection.getOutputStream();
            out.write(payloadBytes, 0, payloadBytes.length);
            out.close();
            int responseCode = connection.getResponseCode();
            BufferedReader reader = null;
            String response;
            switch (responseCode){
                case 200:
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    response = reader.readLine();
                    break;
                case 204:
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                    response = reader.readLine();
                    break;
                default:
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                    response = reader.readLine();
                    break;
            }
            if(reader != null){
                reader.close();
            }
            Map<String, Object> map = JsonUtils.gson.fromJson(response, JsonUtils.stringObjectMap);
            return new RequestResponse(responseCode,map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegisterUnavailableException();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    public String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch(Exception e) {
            this.runOnUiThread(() ->  {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Une erreur est survenue");
                alertDialogBuilder
                        .setMessage("Impossible de comuniquer avec " + e.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("Oui", (dialog, id) -> this.finish())
                        .setNegativeButton("Non", (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
            e.printStackTrace();
        }
        return content.toString();
    }

    public UserData getUserData(String mail) {
        System.out.println(this.getUrlContents(Constants.API_URL + "users/mail/" + mail));
        UserData data = JsonUtils.gson.fromJson(this.getUrlContents(Constants.API_URL + "users/mail/" + mail), UserData.class);
        return data;
    }
}