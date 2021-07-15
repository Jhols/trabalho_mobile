package com.example.atividade_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ExercicioAnalytics extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Button btnOk = (Button) findViewById(R.id.btn_ok_analytic);
        ImageView img = (ImageView) findViewById(R.id.imageView_analytic);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "onCreate");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString(FirebaseAnalytics.Param.ITEM_ID, "teste_id");
        bundle1.putString(FirebaseAnalytics.Param.ITEM_NAME, "teste_name");
        bundle1.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "teste_image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle1);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putInt("ButtonID", v.getId());
                String btnNome = "ok_exercicio_analytic";

                Log.d( "Evento Ex Analytic","Exercicio Analytic -> Botao Clicado");

                mFirebaseAnalytics.logEvent(btnNome, params);
            }
        });

        mFirebaseAnalytics.setCurrentScreen(this, null, null);
    }

}