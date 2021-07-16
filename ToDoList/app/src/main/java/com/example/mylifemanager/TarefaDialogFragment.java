package com.example.mylifemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.StatsLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.mylifemanager.prefdao.TarefaDAO;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TarefaDialogFragment extends DialogFragment {

    private static final String TAG = "CategoriaDialogFragment"; //Tag que identifica o dialog quando este for chamado
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private int idTarefa = 0;
    private Button btnOk, btnLocalizacao; //Botao de concluido do layout
    private TextView txtTitulo, txtCategoria, txtPrioridade, txtData;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        btnLocalizacao = view.findViewById(R.id.btn_localizacao_tarefa);

        btnLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> permissoes = new ArrayList<>();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    permissoes.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    permissoes.add(Manifest.permission.ACCESS_COARSE_LOCATION);

                    ActivityCompat.requestPermissions(getActivity(), permissoes.toArray(new String[permissoes.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                    Toast.makeText(getActivity(), "Permissão Garantida", Toast.LENGTH_SHORT).show();
                }

                MapsDialogFragment mapa = new MapsDialogFragment(getActivity());
                DialogFragment dialog = new MapsDialogFragment(getContext());
                FragmentManager fg = getChildFragmentManager();
                dialog.show(fg, "MapsDialogFragment");
            }
        });

        Tarefa tarefa = new Tarefa();
        tarefa = TarefaDAO.getInstance(getActivity()).findById(this.idTarefa);

        txtTitulo.setText(tarefa.getTitulo());
        txtCategoria.setText(tarefa.getCategoria());
        txtPrioridade.setText(tarefa.getPrioridade());
        txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(tarefa.getPrazo()));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.concluido, Toast.LENGTH_SHORT);
                getDialog().dismiss();
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Bundle bundle1 = new Bundle();
        bundle1.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(tarefa.getId()));
        bundle1.putString(FirebaseAnalytics.Param.ITEM_NAME, tarefa.getTitulo());
        bundle1.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "teste_image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle1);

        return view;
    }

    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }
}
