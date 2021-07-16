package com.example.mylifemanager.ui.tarefa;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylifemanager.TarefaDialogFragment;
import com.example.mylifemanager.R;
import com.example.mylifemanager.Tarefa;
import com.example.mylifemanager.prefdao.TarefaDAO;

import java.util.List;

public class TarefaRecyclerViewAdapter extends RecyclerView.Adapter<TarefaRecyclerViewAdapter.MyViewHolder> {

    private List<Tarefa> tarefas;
    protected Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int id = 0; //ID do item e da tarefa que o compoe
        public TextView txtItemLista;
        private CheckBox checkItem;
        private DialogFragment dialog;
        private Context context;

        public MyViewHolder(@NonNull View itemView)  {
            super(itemView);
            txtItemLista = itemView.findViewById(R.id.lista_tarefas_item);
            checkItem = itemView.findViewById(R.id.checkBox_tarefa);
            itemView.setOnClickListener(this);

            //Clique no checkbox do item
            checkItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkItem.isChecked()) { //Faz a marcacao da tarefa ao clicar no checkbox
                        txtItemLista.setPaintFlags(txtItemLista.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        TarefaDAO.getInstance(itemView.getContext()).setFeito(id, true);
                    }
                    else { //Senao, retira a marcacao
                        txtItemLista.setPaintFlags(txtItemLista.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        TarefaDAO.getInstance(itemView.getContext()).setFeito(id, false);
                    }
                }
            });
        }

        //Clique na linha do item
        @Override
        public void onClick(View v) {
            dialog = new TarefaDialogFragment();
            Bundle arg = new Bundle();
            arg.putInt("idTarefa", this.id);
            dialog.setArguments(arg);
            FragmentManager fg = ((AppCompatActivity)context).getSupportFragmentManager();
            dialog.show(fg, "CategoriaDialogFragment");
        }

        //Retorna o id do item, que tambem e' o id da tarefa
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    //Recebe os parametros da tela que chama o recycler view para alimentar a lista
    public TarefaRecyclerViewAdapter(List<Tarefa> listaTarefas, Context context) {
        this.tarefas = listaTarefas;
        this.context = context;
    }

    @NonNull
    @Override
    public TarefaRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gera um item da lista de tarefas na tela
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_tarefas, parent, false);

        return new MyViewHolder(itemView);
    }

    //Configuracoes iniciais que cada item recebe individualmente a ser posto na lista e na tela
    @Override
    public void onBindViewHolder(@NonNull TarefaRecyclerViewAdapter.MyViewHolder holder, int position) {
        //Apresenta o nome da tarefa
        String tar = tarefas.get(position).getTitulo(); //tarefas.toArray()[position].toString().split("&")[0];
        holder.txtItemLista.setText(tar);
        holder.context = context;

        //Configura o id da tarefa no item para consultas ao clicar
        holder.setId(tarefas.get(position).getId());

        //Se a tarefa ja estiver sido feita, a tela inicia com ela marcada
        if (tarefas.get(position).isFeito()) {
            holder.checkItem.setChecked(true);
            holder.txtItemLista.setPaintFlags(holder.txtItemLista.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    public Context getContext() {
        return context;
    }

    //Obtem a quantidade de itens na lista de tarefas
    @Override
    public int getItemCount() {
        return tarefas.size();
    }
}
