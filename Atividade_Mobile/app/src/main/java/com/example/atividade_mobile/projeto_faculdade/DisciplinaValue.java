package com.example.atividade_mobile.projeto_faculdade;

import com.example.atividade_mobile.projeto_faculdade.DAO.DisciplinaDAO;

public class DisciplinaValue implements java.io.Serializable {

    private Long id;
    private String nome;

    public DisciplinaValue() {
    }

    public DisciplinaValue(String nome) {
        this();
        this.nome = nome;
    }

    public DisciplinaValue(Long id, String nome) {
        this(nome);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "DisciplinaValue{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

    public void setDisciplina(Long id, String nome) {
        this.setId(id);
        this.setNome(nome);
    }
}
