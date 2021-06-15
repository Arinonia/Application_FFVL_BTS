package fr.arinonia.app.utils;

import android.app.AlertDialog;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public interface IJsonReadable {
    void jsonDeserialize();
    default String getUrlContents(String theUrl, ComponentActivity appCompatActivity) {
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
            appCompatActivity.runOnUiThread(() ->  {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(appCompatActivity);
                alertDialogBuilder.setTitle("Une erreur est survenue");
                alertDialogBuilder
                        .setMessage("Impossible de comuniquer avec " + e.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("Oui", (dialog, id) -> appCompatActivity.finish())
                        .setNegativeButton("Non", (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
            e.printStackTrace();
        }
        return content.toString();
    }
}
