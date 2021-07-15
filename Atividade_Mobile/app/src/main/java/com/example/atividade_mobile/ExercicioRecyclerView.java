package com.example.atividade_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ExercicioRecyclerView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio_recycler_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycview);
        recyclerView.setHasFixedSize(true);

        String[][] myDataset = {
                {"Redes", "Sistemas Operacionais"},
                {"Algoritmos", "Programacao"},
                {"Engenharia de Software", "Banco de Dados"}
        };

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter = new RecyclerAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);

    }
}