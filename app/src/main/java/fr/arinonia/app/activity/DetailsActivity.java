package fr.arinonia.app.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.arinonia.app.R;
import fr.arinonia.app.balise.Balise;
import fr.arinonia.app.balise.WindSpeed;
import fr.arinonia.app.exception.DetailsBaliseException;
import fr.arinonia.app.exception.RegisterUnavailableException;
import fr.arinonia.app.utils.Constants;
import fr.arinonia.app.utils.IJsonReadable;
import fr.arinonia.app.utils.JsonUtils;
import fr.arinonia.app.utils.RequestResponse;


public class DetailsActivity extends AppCompatActivity implements IJsonReadable {

    private LineChart chart;
    private String idBalise;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.details_activity);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#686868"));
            window.setNavigationBarColor(Color.parseColor("#1e1e1e"));
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = this.getIntent().getExtras();
        this.idBalise = bundle.getString("id");
        this.idUser = bundle.getString("idUser");

        new Thread(this::jsonDeserialize).start();

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void jsonDeserialize() {
        Balise balise = JsonUtils.gson.fromJson(this.getUrlContents(Constants.API_URL + "balises/" + idBalise, this), Balise.class);
        ImageView imageView = this.findViewById(R.id.favbtn);
        TextView pressionAtmospherique = this.findViewById(R.id.pression_atmospherique);
        TextView temperature = this.findViewById(R.id.temperature);
        TextView hygrometrie = this.findViewById(R.id.hygrometrie);
        TextView vitesse_vent = this.findViewById(R.id.vitesse_vent);
        TextView vent_minimum = this.findViewById(R.id.vent_minimum);
        TextView vent_maximum = this.findViewById(R.id.vent_maximum);
        this.chart = this.findViewById(R.id.chart);


        this.runOnUiThread(() -> {
            imageView.setOnClickListener(e -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    new Thread(() -> {
                        try {
                            RequestResponse result = sendJsonPostRequest(new URL(Constants.API_URL + "users/" + idUser + "/balises/" + idBalise), JsonUtils.baliseFavToJson(idBalise, idUser), null);
                            System.out.println(Constants.API_URL + "/users/" + idUser + "balises/" + idBalise);
                            if (result.isSuccessful()) {
                                System.out.println("Requeste send and response is good");
                            } else {
                                System.err.println("Error POST request in DetailsActivity.java");
                            }
                        } catch (DetailsBaliseException detailsBaliseException) {
                            detailsBaliseException.printStackTrace();
                        } catch (MalformedURLException malformedURLException) {
                            malformedURLException.printStackTrace();
                        }
                    }).start();
                }
            });

            imageView.getLayoutParams().height = 200;
            imageView.getLayoutParams().width = 200;
            imageView.requestLayout();
            pressionAtmospherique.setText(balise.getPression_atmospherique() + " hPa");
            temperature.setText(balise.getTemperature() + " °C");
            hygrometrie.setText(balise.getHygrometrie() + " g/m³");
            vitesse_vent.setText("Vitesse du vent " + balise.getVitesse_vent() + " km/s");
            vent_minimum.setText("Vitesse du vent minimum " + balise.getVent_minimum() + " km/s");
            vent_maximum.setText("Vitesse du vent maximum " + balise.getVent_maximum() + " km/s");

            //TODO DIAGRAMME
            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);
            chart.setBackgroundColor(Color.DKGRAY);



            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.WHITE);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(true);
            xAxis.setTextColor(Color.rgb(255, 192, 56));
            xAxis.setCenterAxisLabels(true);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    long millis = TimeUnit.HOURS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
            leftAxis.setDrawGridLines(true);
            leftAxis.setGranularityEnabled(true);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setYOffset(-9f);
            leftAxis.setTextColor(Color.rgb(255, 192, 56));

            this.setData(10, 50);
            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);
            chart.invalidate();
        });

    }

    private void setData(int count, float range) {

        // now in hours
        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        ArrayList<Entry> values = new ArrayList<>();

        // count = hours
        float to = now + count;

        // increment by 1 hour
        for (float x = now; x < to; x++) {

            float y = getRandom(range, 50);
            values.add(new Entry(x, y)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "Vitesse du vent en Km/H");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }
    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private RequestResponse sendJsonPostRequest(URL requestUrl, String payload, Proxy proxy) throws DetailsBaliseException {
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
            throw new DetailsBaliseException();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }

}