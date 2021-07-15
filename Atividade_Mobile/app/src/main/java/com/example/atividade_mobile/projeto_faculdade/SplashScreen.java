package com.example.atividade_mobile.projeto_faculdade;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.widget.TextView;

import com.example.atividade_mobile.R;
import com.example.atividade_mobile.projeto_faculdade.DAO.DisciplinaDAO;
import com.google.android.gms.common.util.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView) this.findViewById(R.id.txt_splash);
        textView.setText("Aguarde, carregando...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new JSONParse().execute();
            }
        }, SPLASH_TIME_OUT);
    }

    public class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SplashScreen.this);

            pDialog.setMessage("Obtendo Dados");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json = null;
            DisciplinaDAO dao = new DisciplinaDAO(SplashScreen.this);
            dao.dropAll();
            JSONArray link = null;

            json = Json();

            int count = 0;
            try {
                link = json.getJSONArray("Lista");
                for (int i = 0; i < link.length(); i++) {
                    JSONObject c = link.getJSONObject(i);
                    DisciplinaValue disciplinaValue = new DisciplinaValue();
                    disciplinaValue.setNome(c.getString("disciplina"));
                    dao.salvar(disciplinaValue);
                    dao.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                pDialog.dismiss();
                startActivity(new Intent(SplashScreen.this, ListaDisciplinasActivity.class));
                finish();
            } catch (Exception e) {

            }
        }

    }

    public static JSONObject Json() {
        JSONObject json = null;
        String resp;

        try {
            URL url = new URL ("http://www.ictios.com.br/emjorge/appfaculdade/" + "index1.php");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            InputStream inputStream = conn.getInputStream();
            resp = IOUtils.toByteArray(inputStream).toString();
            json = new JSONObject(resp);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}