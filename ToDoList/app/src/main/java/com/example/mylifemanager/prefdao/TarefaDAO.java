package com.example.mylifemanager.prefdao;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.mylifemanager.Tarefa;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    private static TarefaDAO singleton;

    public static final String CHAVE_LISTA_A_FAZER = "lista_tarefas";
    public static final String CHAVE_LISTA_CONCLUIDAS = "lista_tarefas_concluidas";
    private static final String INDEX_TAREFA = "index_tarefa";

    private TarefaDAO() { }

    public static TarefaDAO getInstance() {
        if (singleton == null) {
            singleton = new TarefaDAO();
        }
        return singleton;
    }

    public Tarefa findById(Context context, int id) {
        List<Tarefa> tarefas = new ArrayList<>();
        tarefas = getListaTarefas(context, CHAVE_LISTA_A_FAZER);

        Tarefa tar = null;

        for (Tarefa tarefa:
             tarefas) {
            if (id == tarefa.getId()) {
                tar = new Tarefa();
                tar = tarefa;
            }
        }
        return tar;
    }

    //Adiciona uma tarefa 'as preferencias
    public void addTarefa(Context context, Tarefa tarefa, String CHAVE_LISTA) {
        List<Tarefa> listaTarefas = new ArrayList<>();

        listaTarefas = getListaTarefas(context, CHAVE_LISTA);

        listaTarefas.add(tarefa);

        setListaTarefas(context, listaTarefas, CHAVE_LISTA);
        incrementaIndex(context);
    }

    public boolean setFeito(Context context, String CHAVE_LISTA, int idTarefa, boolean feito) {
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
    }

    public void setListaTarefas(Context context, List<Tarefa> lista, String CHAVE_LISTA) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(lista);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CHAVE_LISTA, jsonString);
        editor.apply();
    }

    public List<Tarefa> getListaTarefas(Context context, String CHAVE_LISTA) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = prefs.getString(CHAVE_LISTA, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Tarefa>>(){}.getType();
        List<Tarefa> lista = gson.fromJson(jsonString, type);

        if (lista == null) {
            lista = new ArrayList<>();
        }

        return lista;
    }

    public int getSize(Context context) {
        List tarefas = new ArrayList<>();
        tarefas = getListaTarefas(context, CHAVE_LISTA_A_FAZER);

        return tarefas.size();
    }

    public int getUltimoIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int index = prefs.getInt(INDEX_TAREFA, 1);

        return index;
    }

    private void incrementaIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt(INDEX_TAREFA, getUltimoIndex(context)+1);
        ed.apply();
    }
}
