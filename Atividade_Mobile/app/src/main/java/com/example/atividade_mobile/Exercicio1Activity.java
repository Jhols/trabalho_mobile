package com.example.atividade_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Exercicio1Activity extends AppCompatActivity {
    protected EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atividades1_e_internacionalizacao);

        Button btn = (Button) findViewById(R.id.btn_colocar_nome);
        this.editText = (EditText) findViewById(R.id.editTextTextPersonName2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", String.valueOf(R.string.msg1));
                editText.setText(R.string.nome_pessoa);
                Toast.makeText(Exercicio1Activity.this, R.string.msg1, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
