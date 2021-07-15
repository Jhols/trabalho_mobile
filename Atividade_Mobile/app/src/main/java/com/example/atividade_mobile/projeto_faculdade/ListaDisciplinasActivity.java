package com.example.atividade_mobile.projeto_faculdade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.atividade_mobile.R;
import com.example.atividade_mobile.projeto_faculdade.DAO.DisciplinaDAO;

import java.util.ArrayList;

public class ListaDisciplinasActivity extends AppCompatActivity {

    protected ListView lista = null;
    protected DisciplinaValue disciplinaValue;
    protected ArrayAdapter<DisciplinaValue> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_faculdade);

        //String[] disciplinas = {"Redes", "Algoritmos", "Programacao"};

        int layout = android.R.layout.simple_list_item_1;

        DisciplinaDAO dao = new DisciplinaDAO(this);

        DisciplinaValue disciplinaValue = new DisciplinaValue();
        disciplinaValue.setId((new Long("1").longValue()));
        disciplinaValue.setNome("Banco de Dados");
        dao.salvar(disciplinaValue);

        ArrayList<DisciplinaValue> disciplinas = (ArrayList<DisciplinaValue>) new ArrayList<>(dao.getLista());
        dao.close();

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layout, disciplinas);
        //ArrayAdapter<DisciplinaValue> adapter = new ArrayAdapter<>(this, layout, disciplinas);
        adapter = new ArrayAdapter<>(this, layout, disciplinas);

        //ListView lista = (ListView) findViewById(R.id.listaProjFacul);
        lista = (ListView) findViewById(R.id.listaProjFacul);

        lista.setAdapter(adapter);
        lista.setOnCreateContextMenuListener(this);
        registerForContextMenu(lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListaDisciplinasActivity.this, "Clicou " + position, Toast.LENGTH_SHORT).show();
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListaDisciplinasActivity.this, adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        int layout = android.R.layout.simple_list_item_1;

        DisciplinaDAO dao = new DisciplinaDAO(this);
        ArrayList<DisciplinaValue> disciplinas = (ArrayList<DisciplinaValue>) new ArrayList<>(dao.getLista());
        dao.close();

        adapter = new ArrayAdapter<>(this, layout, disciplinas);

        lista.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_disciplinas, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_disciplinas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_new) {
            startActivity(new Intent(this, DisciplinaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        disciplinaValue = (DisciplinaValue) this.adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);

        int id = item.getItemId();

        switch (id) {
            case R.id.action_new:
                startActivity(new Intent(this, DisciplinaActivity.class));
                break;
            case R.id.action_update:
                Intent intent = new Intent(this, DisciplinaActivity.class);
                intent.putExtra("disciplinaSelecionada", disciplinaValue);
                startActivity(intent);
                break;
            case R.id.action_delete:
                DisciplinaDAO dao = new DisciplinaDAO(ListaDisciplinasActivity.this);
                dao.deletar(disciplinaValue);
                dao.close();
                this.onResume();
                break;
            default:
                return false;
        }
        return true;
    }
}