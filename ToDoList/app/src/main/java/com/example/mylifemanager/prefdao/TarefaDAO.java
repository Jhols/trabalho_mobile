package com.example.mylifemanager.prefdao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.mylifemanager.Tarefa;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TarefaDAO extends SQLiteOpenHelper {

    private static TarefaDAO singleton;

    /*
    public static final String CHAVE_LISTA_A_FAZER = "lista_tarefas";
    public static final String CHAVE_LISTA_CONCLUIDAS = "lista_tarefas_concluidas";
    private static final String INDEX_TAREFA = "index_tarefa";
    */

    private static final String DATABASE = "ToDoList";
    private static final String TABELA = "Tarefa";
    private static final int VERSAO = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE "+ TABELA +" (id INTEGER PRIMARY KEY,"
                + " titulo TEXT UNIQUE NOT NULL,"
                + " categoria TEXT,"
                + " prioridade TEXT,"
                + " prazo TEXT,"
                + " localizacao TEXT,"
                + " feito INTEGER)";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(ddl);
        onCreate(db);
    }

    private TarefaDAO(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    public static TarefaDAO getInstance(Context context) {
        if (singleton == null) {
            singleton = new TarefaDAO(context);
        }
        return singleton;
    }

    public Tarefa findById(int id) {
        /*List<Tarefa> tarefas = new ArrayList<>();
        tarefas = getListaTarefas(context, CHAVE_LISTA_A_FAZER);

        Tarefa tar = null;

        for (Tarefa tarefa:
             tarefas) {
            if (id == tarefa.getId()) {
                tar = new Tarefa();
                tar = tarefa;
            }
        }
        return tar;*/

        Tarefa tarefa = null;

        String query = "SELECT * FROM "+ TABELA +" WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (cursor.moveToFirst()) {
            tarefa = new Tarefa();
            tarefa.setId(Integer.parseInt(cursor.getString(0)));
            tarefa.setTitulo(cursor.getString(1));
            tarefa.setCategoria(cursor.getString(2));
            tarefa.setPrioridade(cursor.getString(3));
            tarefa.setPrazo(sdf.parse(cursor.getString(4), new ParsePosition(0)));
            tarefa.setLocalizacao(cursor.getString(5));
            tarefa.setFeito(cursor.getInt(6) != 0 ? true : false);
        }

        return tarefa;

    }

    //Adiciona uma tarefa 'as preferencias
    public void addTarefa(Tarefa tarefa) {
        /*List<Tarefa> listaTarefas = new ArrayList<>();

        listaTarefas = getListaTarefas(context, CHAVE_LISTA);

        listaTarefas.add(tarefa);

        setListaTarefas(context, listaTarefas, CHAVE_LISTA);
        incrementaIndex(context);
         */

        ContentValues values = new ContentValues();
        values.put("titulo", tarefa.getTitulo());
        values.put("categoria", tarefa.getCategoria());
        values.put("prioridade", tarefa.getPrioridade());
        values.put("prazo", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(tarefa.getPrazo()));
        values.put("localizacao", tarefa.getLocalizacao());
        values.put("feito", 0);

        getWritableDatabase().insert(TABELA, null, values);
    }

    public boolean setFeito(int idTarefa, boolean feito) {
        /*Tarefa tarefa = new Tarefa();
        tarefa = findById(idTarefa);

        int valor = 0;
        if (feito) valor = 1;

        String sql = "UPDATE "+ TABELA +" SET feito='"+ valor +"' WHERE id = " + idTarefa;*/

        ContentValues values = new ContentValues();
        values.put("feito", feito == true ? 1 : 0);

        int concluido = getWritableDatabase().update(TABELA, values, "id=?", new String[]{Integer.toString(idTarefa)});

        if (concluido != 0) return true; return false;
    }

    /*public boolean setFeito(Context context, String CHAVE_LISTA, int idTarefa, boolean feito) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        listaTarefas = getListaTarefas(context, CHAVE_LISTA);
        boolean concluido = false;

        for (Tarefa tarefa: listaTarefas) {
            if (tarefa.getId() == idTarefa) {
                tarefa.setFeito(feito);
                setListaTarefas(context, listaTarefas, CHAVE_LISTA);
                concluido = true;
            }
        }

        return concluido;
    }*/

    public void setListaTarefas(List<Tarefa> lista) {
       onUpgrade(this.getWritableDatabase(), getWritableDatabase().getVersion(), (getWritableDatabase().getVersion()+1));

        for (Tarefa tarefa:
             lista) {
            addTarefa(tarefa);
        }

    }

    /*public void setListaTarefas(Context context, List<Tarefa> lista, String CHAVE_LISTA) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(lista);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CHAVE_LISTA, jsonString);
        editor.apply();
    }*/

    public List<Tarefa> getListaTarefas() { //Context context, String CHAVE_LISTA) {
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = prefs.getString(CHAVE_LISTA, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Tarefa>>(){}.getType();
        List<Tarefa> lista = gson.fromJson(jsonString, type);

        if (lista == null) {
            lista = new ArrayList<>();
        }

        return lista;*/

        List<Tarefa> tarefas = new LinkedList<Tarefa>();

        String query = "SELECT * FROM "+ TABELA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Tarefa tarefa = null;
        if (cursor.moveToFirst()) {
            do {
                tarefa = new Tarefa();
                tarefa.setId(Integer.parseInt(cursor.getString(0)));
                tarefa.setTitulo(cursor.getString(1));
                tarefa.setCategoria(cursor.getString(2));
                tarefa.setPrioridade(cursor.getString(3));
                tarefa.setPrazo(new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(4), new ParsePosition(0)));
                tarefa.setLocalizacao(cursor.getString(5));
                tarefa.setFeito(cursor.getInt(6) != 0 ? true : false);
                tarefas.add(tarefa);
            } while (cursor.moveToNext());
        }

        return tarefas;
    }

    public int getSize() { //Context context) {
        /*List tarefas = new ArrayList<>();
        tarefas = getListaTarefas(context, CHAVE_LISTA_A_FAZER);
        */
        String query = "SELECT COUNT(*) FROM " + TABELA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int qtd = 0;
        if(cursor.moveToFirst())
            qtd = cursor.getInt(0);

        //return tarefas.size();
        return qtd;
    }

    /*public int getUltimoIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int index = prefs.getInt(INDEX_TAREFA, 1);

        return index;
    }*/

    /*private void incrementaIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt(INDEX_TAREFA, getUltimoIndex(context)+1);
        ed.apply();
    }*/


}
