package com.example.mylifemanager;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mylifemanager.prefdao.TarefaDAO;

import java.text.SimpleDateFormat;

public class TarefaDialogFragment extends DialogFragment {

    private static final String TAG = "CategoriaDialogFragment"; //Tag que identifica o dialog quando este for chamado
    private int idTarefa = 0;
    private Button btnOk; //Botao de concluido do layout
    private TextView txtTitulo, txtCategoria, txtPrioridade, txtData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.idTarefa = getArguments().getInt("idTarefa");
        View view = inflater.inflate(R.layout.categoria_dialog, container, false);

        getDialog().setContentView(R.layout.categoria_dialog); //Layout do dialog

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Background do dialog
            getDialog().getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background_dialog));
        }
        //Configura largura e altura
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(false); //Usuario nao conseguira' remover o dialog ao pressionar o botao de voltar
        getDialog().getWindow().getAttributes().windowAnimations = R.style.animacao_dialog; //Configura a animacao do dialog

        btnOk = view.findViewById(R.id.btn_ok_categoria);
        txtTitulo = view.findViewById(R.id.txt_titulo_tarefa);
        txtCategoria = view.findViewById(R.id.item_categoria_tarefa);
        txtPrioridade = view.findViewById(R.id.item_prioridade_tarefa);
        txtData = view.findViewById(R.id.item_prazo_tarefa);

        Tarefa tarefa = new Tarefa();
        tarefa = TarefaDAO.getInstance().findById(getActivity(), this.idTarefa);

        txtTitulo.setText(tarefa.getTitulo());
        txtCategoria.setText(tarefa.getCategoria());
        txtPrioridade.setText(tarefa.getPrioridade());
        txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(tarefa.getPrazo().getTime()));


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.concluido, Toast.LENGTH_SHORT);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }
}
