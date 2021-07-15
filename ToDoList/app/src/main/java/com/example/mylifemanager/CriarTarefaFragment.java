package com.example.mylifemanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mylifemanager.prefdao.TarefaDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CriarTarefaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CriarTarefaFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText eTxtTituloTarefa = null;
    private Button btnData = null;
    private Spinner spinCategoria = null, spinPrioridade = null;
    private Button btnCancelar = null, btnConfimar = null;

    private SharedPreferences myPrefs = null;
    private DialogFragment dialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CriarTarefaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CriarObjetivo.
     */
    // TODO: Rename and change types and number of parameters
    public static CriarTarefaFragment newInstance(String param1, String param2) {
        CriarTarefaFragment fragment = new CriarTarefaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_criar_tarefa, container, false);

        //Obter os elementos do layout
        this.eTxtTituloTarefa = (EditText) view.findViewById(R.id.editTxt_nova_tarefa);
        this.spinCategoria = (Spinner) view.findViewById(R.id.spin_categoria_tarefa);
        this.btnData = (Button) view.findViewById(R.id.btn_data_tarefa);
        this.spinPrioridade = (Spinner) view.findViewById(R.id.spin_prioridade_tarefa);
        this.btnCancelar = (Button) view.findViewById(R.id.btn_cancelar_tarefa);
        this.btnConfimar = (Button) view.findViewById(R.id.btn_confirmar_tarefa);

        //Obtem e desativa o botao flutuante
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();

        //Acesso ao banco de preferencias
        this.myPrefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        //Retira o texto inicial ao clicar no EditText do titulo da tarefa
        eTxtTituloTarefa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    eTxtTituloTarefa.setText("");
            }
        });

        //Alimenta o combobox de categoria
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.str_arr_categoria, R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinCategoria.setAdapter(adapter1);
        spinCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Alimenta o combobox de prioridade
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.str_arr_prioridade, R.layout.support_simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinPrioridade.setAdapter(adapter2);
        spinPrioridade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*dialog = new CategoriaDialogFragment();

        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getChildFragmentManager(),
            }
        });*/

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //Cria e insere a tarefa no banco de preferencias
        btnConfimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = getString(R.string.titulo_tarefa);
                if (!eTxtTituloTarefa.getText().toString().equals("") && !eTxtTituloTarefa.getText().toString().equals(txt)) {

                    Tarefa tarefa = new Tarefa();
                    tarefa.setId(TarefaDAO.getInstance().getUltimoIndex(getActivity()));
                    tarefa.setTitulo(eTxtTituloTarefa.getText().toString());
                    tarefa.setCategoria(spinCategoria.getSelectedItem().toString());
                    tarefa.setPrioridade(spinPrioridade.getSelectedItem().toString());

                    String data = btnData.getText().toString().split(": ")[1];
                    SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date d = new java.util.Date();
                    d = sd.parse(data, new ParsePosition(0));
                    tarefa.setPrazo(d);

                    //Adiciona a tarefa ao SharedPreferences
                    TarefaDAO.getInstance().addTarefa(getActivity(), tarefa, TarefaDAO.CHAVE_LISTA_A_FAZER);

                    Toast.makeText(getActivity(), getString(R.string.concluido), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else {
                    Toast.makeText(getActivity(), getString(R.string.insira_titulo_tarefa), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    //Apresenta o calendario para selecionar uma data
    private void showDatePickerDialog() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }
    }

    //Tratamento da data apos a selecao no dialogbox
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar date = new GregorianCalendar(year, month, dayOfMonth);
        btnData.setText("Data: " + new SimpleDateFormat("dd/MM/yyyy").format(date.getTime()));
    }
}