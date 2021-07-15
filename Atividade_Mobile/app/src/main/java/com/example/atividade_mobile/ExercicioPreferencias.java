package com.example.atividade_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExercicioPreferencias extends AppCompatActivity {

    private Button btnSalvar = null;
    private EditText editText = null;
    private SharedPreferences myPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio_preferencias);

        btnSalvar = (Button) findViewById(R.id.salvar);
        editText = (EditText) findViewById(R.id.txtFieldPref);

        myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);

        String prefNome = myPrefs.getString("nome", "");

        if (prefNome!=null) {
            editText.setText(prefNome);
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ePrefs = myPrefs.edit();
                ePrefs.putString("nome", editText.getText().toString());
                ePrefs.commit();

                Log.i("TAG", String.valueOf(R.string.msg1));
                Toast.makeText(ExercicioPreferencias.this, R.string.msg_salvo_sucesso, Toast.LENGTH_SHORT).show();
            }
        });

    }
}