package com.example.atividade_mobile.projeto_faculdade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.DeadSystemException;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.atividade_mobile.R;
import com.example.atividade_mobile.projeto_faculdade.DAO.DisciplinaDAO;

public class DisciplinaActivity extends AppCompatActivity {

    protected EditText editTextDisciplina;
    protected DisciplinaValue disciplinaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disciplina_layout);

        Button botao = (Button) findViewById(R.id.btnDisciplina);
        this.editTextDisciplina = (EditText) findViewById(R.id.eTxtDisciplina);
        this.editTextDisciplina.setText("teste1");
        Intent intent = getIntent();

        disciplinaSelecionada = (DisciplinaValue) intent.getSerializableExtra("disciplinaSelecionada");
        if (disciplinaSelecionada != null) {
            botao.setText("Alterar");
            this.editTextDisciplina.setText(disciplinaSelecionada.getNome());
        }

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisciplinaDAO dao = new DisciplinaDAO(DisciplinaActivity.this);
                if (disciplinaSelecionada == null) {
                    DisciplinaValue disciplinaValue = new DisciplinaValue();
                    disciplinaValue.setNome(editTextDisciplina.getText().toString());
                    dao.salvar(disciplinaValue);
                } else {
                    disciplinaSelecionada.setNome(editTextDisciplina.getText().toString());
                    dao.alterar(disciplinaSelecionada);
                }
                finish();
            }
        });
    }


}