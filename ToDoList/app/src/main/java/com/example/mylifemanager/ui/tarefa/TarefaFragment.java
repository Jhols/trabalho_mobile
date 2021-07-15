package com.example.mylifemanager.ui.tarefa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylifemanager.R;
import com.example.mylifemanager.Tarefa;
import com.example.mylifemanager.prefdao.TarefaDAO;

import java.util.Collections;
import java.util.List;

public class TarefaFragment extends Fragment {

    private GalleryFragment galleryViewModel;
    private RecyclerView tarefasRecyclerView, concluidasRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager1, layoutManager2;
    List<Tarefa> tarefas, concluidas;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       /* galleryViewModel =
                new ViewModelProvider(this).get(GalleryFragment.class);*/

        View root = inflater.inflate(R.layout.fragment_tarefa, container, false);

        /*
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        //Configuracoes do recycler view
        tarefasRecyclerView = (RecyclerView) root.findViewById(R.id.lista_tarefas);
        tarefasRecyclerView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(getActivity());
        //layoutManager2 = new LinearLayoutManager(getActivity());
        tarefasRecyclerView.setLayoutManager(layoutManager1);

        /*concluidasRecyclerView = (RecyclerView) root.findViewById(R.id.lista_tarefas_concluidas);
        concluidasRecyclerView.setHasFixedSize(true);
        concluidasRecyclerView.setLayoutManager(layoutManager2);
        */
        //Obtem a lista de tarefas a ser apresentada
        tarefas = TarefaDAO.getInstance().getListaTarefas(getActivity(), TarefaDAO.CHAVE_LISTA_A_FAZER);
       // concluidas = TarefaDAO.getInstance().getListaTarefas(getActivity(), TarefaDAO.CHAVE_LISTA_CONCLUIDAS);

        //Adiciona as tarefas na lista do recycler view
        mAdapter = new TarefaRecyclerViewAdapter(tarefas, getActivity());
        tarefasRecyclerView.setAdapter(mAdapter);
        //mAdapter = new TarefaRecyclerViewAdapter(concluidas, getActivity());
       // concluidasRecyclerView.setAdapter(mAdapter);

        //Adiciona a linha de divisao de cada item da lista
        tarefasRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //concluidasRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        //Permite o efeito de Drag and Drop
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(tarefasRecyclerView);

        return root;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            tarefas = TarefaDAO.getInstance().getListaTarefas(getActivity(), TarefaDAO.CHAVE_LISTA_A_FAZER);

            Collections.swap(tarefas, fromPosition, toPosition);

            TarefaDAO.getInstance().setListaTarefas(getActivity(), tarefas, TarefaDAO.CHAVE_LISTA_A_FAZER);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}