package com.example.atividade_mobile.projeto_faculdade.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.atividade_mobile.projeto_faculdade.DisciplinaValue;

import java.util.LinkedList;
import java.util.List;

public class DisciplinaDAO extends SQLiteOpenHelper {

    private static final String DATABASE = "BancoDisciplinas";
    private static final String TABELA = "Disciplina";
    private static final int VERSAO = 1;

    public DisciplinaDAO(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE "+ TABELA +" (id INTEGER PRIMARY KEY,"
                + " nome TEXT UNIQUE NOT NULL);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(ddl);
        onCreate(db);
    }

    public void salvar(DisciplinaValue disciplinaValue) {
        ContentValues values = new ContentValues();
        values.put("nome", disciplinaValue.getNome());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public List getLista() {
        List<DisciplinaValue> disciplinas = new LinkedList<DisciplinaValue>();

        String query = "SELECT * FROM Disciplina order by nome";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        DisciplinaValue disciplina = null;
        if (cursor.moveToFirst()) {
            do {
                disciplina = new DisciplinaValue();
                disciplina.setId(Long.parseLong(cursor.getString(0)));
                disciplina.setNome(cursor.getString(1));
                disciplinas.add(disciplina);
            } while (cursor.moveToNext());
        }

        return disciplinas;
    }

    public void alterar(DisciplinaValue disciplinaValue) {
        ContentValues values = new ContentValues();
        values.put("nome", disciplinaValue.getNome());

        getWritableDatabase().update(TABELA, values, "id=?", new String[]{disciplinaValue.getId().toString()});
    }

    public void deletar(DisciplinaValue disciplinaValue) {
        String[] args = {disciplinaValue.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    public void dropAll() {
    }
}
