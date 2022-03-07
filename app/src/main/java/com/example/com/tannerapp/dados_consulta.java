package com.example.com.tannerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import bottomnav.hitherejoe.com.tannerapp.R;


public class dados_consulta extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabaseInstance;
    /************************************************************/
    Button grafico;
    ArrayAdapter<String> dataMama, dataPelo;
    int nivel_mama = 0;
    int nivel_pelo = 0;
    EditText data_consulta;
    EditText edit_Peso;
    EditText edit_Estatura;
    EditText edit_MenarcaConsul;
    EditText dtIniTratamento;
    EditText dtFimTratamento;
    String value;
    String uuid_consulta;
    ProgressBar progressBar;
    String alertaMenarca = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefPacientes;

    String nome_paciente = "";
    String id_medico = "";
    String sexo = "";
    String uuid_paciente = "";

    String data_nascimento_consul = "";
    String idadePconsulta = "";
    String idadeUconsulta = "";
    int qtdConsulta = 0;
    String idade_menarca = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_consulta);
/*********************************************************************************************************************/
        getSupportActionBar().setTitle("Endocrin App");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(dados_consulta.this, telainicial.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseReference = LibraryClass.getFirebase();
        databaseReference.getRef();
        final FirebaseUser user;
/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/***************************************************************************************************************/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Intent i = getIntent();
        nome_paciente = i.getStringExtra("nome_paciente");
        id_medico = i.getStringExtra("id_medico");
        sexo = i.getStringExtra("sexo");
        uuid_paciente = i.getStringExtra("uuid_paciente");
        data_nascimento_consul = i.getStringExtra("data_nascimento");
        idadePconsulta = i.getStringExtra("idadePconsulta");
        idadeUconsulta = i.getStringExtra("idadeUconsulta");
        qtdConsulta = i.getIntExtra("qtdConsulta", 0);
        idade_menarca = i.getStringExtra("idade_menarca");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**************************************************************************************************************/
        final CheckBox chEscTanner = (CheckBox) findViewById(R.id.chEscTanner);
        //final CheckBox chMenarca = (CheckBox) findViewById(R.id.chMenarca);
        //Toast.makeText(getApplicationContext(), "SEXO PACIENTE: " + sexo, Toast.LENGTH_LONG).show();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_consulta);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_menu:
                        Intent home = new Intent(dados_consulta.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.action_cadastro_pacientes:
                        Intent cadastro_pacientes = new Intent(dados_consulta.this, dados_paciente.class);
                        cadastro_pacientes.putExtra("nome_paciente", nome_paciente);
                        cadastro_pacientes.putExtra("id_medico", id_medico);
                        cadastro_pacientes.putExtra("sexo", sexo);
                        cadastro_pacientes.putExtra("uuid_paciente", uuid_paciente);
                        cadastro_pacientes.putExtra("data_nascimento", data_nascimento_consul);
                        cadastro_pacientes.putExtra("idadePconsulta", idadePconsulta);
                        cadastro_pacientes.putExtra("idadeUconsulta", idadeUconsulta);
                        cadastro_pacientes.putExtra("qtdConsulta", qtdConsulta);
                        cadastro_pacientes.putExtra("idade_menarca", idade_menarca);
                        startActivity(cadastro_pacientes);
                        break;
                    case R.id.action_cadastro_consulta:

                        break;
                    case R.id.action_hist_consultas:
                        Intent hist_consulta = new Intent(dados_consulta.this, hist_consulta.class);
                        hist_consulta.putExtra("nome_paciente", nome_paciente);
                        hist_consulta.putExtra("id_medico", id_medico);
                        hist_consulta.putExtra("sexo", sexo);
                        hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                        hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                        hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                        hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                        hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                        hist_consulta.putExtra("idade_menarca", idade_menarca);
                        startActivity(hist_consulta);
                        break;
                    case R.id.action_grafico:
                        Intent grafico = new Intent(dados_consulta.this, grafico.class);
                        grafico.putExtra("nome_paciente", nome_paciente);
                        grafico.putExtra("id_medico", id_medico);
                        grafico.putExtra("sexo", sexo);
                        grafico.putExtra("uuid_paciente", uuid_paciente);
                        grafico.putExtra("data_nascimento", data_nascimento_consul);
                        grafico.putExtra("idadePconsulta", idadePconsulta);
                        grafico.putExtra("idadeUconsulta", idadeUconsulta);
                        grafico.putExtra("qtdConsulta", qtdConsulta);
                        grafico.putExtra("idade_menarca", idade_menarca);
                        startActivity(grafico);
                        break;
                }
                return false;
            }
        });
/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
        final List<String> data_nascimento = new ArrayList<>();
        final int[] ano_nascimento = new int[1];
        final int[] mes_nascimento = new int[1];
        final int[] dia_nascimento = new int[1];

        myRefPacientes = database.getReference("pacientes");

        ValueEventListener valueEventListener = myRefPacientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot respostaSnapshot) {
                Iterable<DataSnapshot> respostaChildren = respostaSnapshot.getChildren();
                final List<String> nomeConsulta = new ArrayList<String>();

                for (DataSnapshot resposta : respostaSnapshot.getChildren()) {
                    String consultaName = resposta.child("nome").getValue().toString();
                    String dta_nascimento = resposta.child("data_nascimento").getValue().toString();

                    if (resposta.child("id_medico").getValue().toString().equals(id_medico) && resposta.child("uuid").getValue().toString().equals(uuid_paciente)) {
                        String[] text_data = dta_nascimento.split("/");
                        dia_nascimento[0] = Integer.parseInt(text_data[0]);
                        mes_nascimento[0] = Integer.parseInt(text_data[1]);
                        ano_nascimento[0] = Integer.parseInt(text_data[2]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit_MenarcaConsul = (EditText) findViewById(R.id.editMenarca);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        EditText editDataConsulta = (EditText) findViewById(R.id.editDataConsulta);
        dtIniTratamento = (EditText) findViewById(R.id.editIniTratamento);
        dtFimTratamento = (EditText) findViewById(R.id.editFimTratamento);
        dtIniTratamento.addTextChangedListener(MaskEditUtil.mask(dtIniTratamento, MaskEditUtil.FORMAT_DATE));
        dtFimTratamento.addTextChangedListener(MaskEditUtil.mask(dtFimTratamento, MaskEditUtil.FORMAT_DATE));
        editDataConsulta.addTextChangedListener(MaskEditUtil.mask(editDataConsulta, MaskEditUtil.FORMAT_DATE));
        editDataConsulta = (EditText) findViewById(R.id.editDataConsulta);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        editDataConsulta.setText(sdf.format(new Date()));

        edit_MenarcaConsul.addTextChangedListener(MaskEditUtil.mask(edit_MenarcaConsul, MaskEditUtil.FORMAT_DATE));
        SimpleDateFormat dtMenarca = new SimpleDateFormat("dd/mm/yyyy");
        //edit_MenarcaConsul.setText(dtMenarca.format(new Date()));

        TextView txtEditTanner = (TextView) findViewById(R.id.txtEscalaTanner);
        TextView txtMenarca = (TextView) findViewById(R.id.txtMenarcaConsulta);
        // Spinner element
        final Spinner spinnerMama = (Spinner) findViewById(R.id.spinnerMama);
        final Spinner spinnerPelo = (Spinner) findViewById(R.id.spinnerPelo);
        // Spinner click listener
        spinnerMama.setOnItemSelectedListener(this);
        spinnerPelo.setOnItemSelectedListener(this);
        //Spinner Drop down elements MAMA
        final List<String>[] escTanner = new List[]{new ArrayList<String>()};
        if (sexo.equals("F")) {
            txtEditTanner.setText("Mama");
            escTanner[0].add("");
            escTanner[0].add("M1");
            escTanner[0].add("M2");
            escTanner[0].add("M3");
            escTanner[0].add("M4");
            escTanner[0].add("M5");
        } else {
            txtEditTanner.setText("Gônada");
            escTanner[0].add("");
            escTanner[0].add("G1");
            escTanner[0].add("G2");
            escTanner[0].add("G3");
            escTanner[0].add("G4");
            escTanner[0].add("G5");
        }


        //Spinner Drop down elements MAMA
        final List<String>[] Pelo = new List[]{new ArrayList<String>()};
        Pelo[0].add("");
        Pelo[0].add("P1");
        Pelo[0].add("P2");
        Pelo[0].add("P3");
        Pelo[0].add("P4");
        Pelo[0].add("P5");

        // Creating adapter for spinner
        dataMama = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, escTanner[0]);
        dataPelo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Pelo[0]);

        // Drop down layout style - list view with radio button
        dataMama.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMama.setAdapter(dataMama);
        spinnerMama.setOnItemSelectedListener(this);

        dataPelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPelo.setAdapter(dataPelo);
        spinnerPelo.setOnItemSelectedListener(this);

        spinnerMama.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case "M1":
                        nivel_mama = 1;
                        break;
                    case "M2":
                        nivel_mama = 2;
                        break;
                    case "M3":
                        nivel_mama = 3;
                        break;
                    case "M4":
                        nivel_mama = 4;
                        break;
                    case "M5":
                        nivel_mama = 5;
                        break;
                    case "G1":
                        nivel_mama = 1;
                        break;
                    case "G2":
                        nivel_mama = 2;
                        break;
                    case "G3":
                        nivel_mama = 3;
                        break;
                    case "G4":
                        nivel_mama = 4;
                        break;
                    case "G5":
                        nivel_mama = 5;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerMama.setEnabled(false);
        spinnerPelo.setEnabled(false);
        chEscTanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chEscTanner.isChecked()) {
                    spinnerMama.setEnabled(false);
                    spinnerPelo.setEnabled(false);
                } else if (chEscTanner.isChecked() == true) {
                    spinnerMama.setEnabled(true);
                    spinnerPelo.setEnabled(true);
                }
            }
        });

        if (sexo.equals("M")) {
            txtMenarca.setVisibility(View.GONE);
            edit_MenarcaConsul.setVisibility(View.GONE);
        }

        spinnerPelo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case "P1":
                        nivel_pelo = 1;
                        break;
                    case "P2":
                        nivel_pelo = 2;
                        break;
                    case "P3":
                        nivel_pelo = 3;
                        break;
                    case "P4":
                        nivel_pelo = 4;
                        break;
                    case "P5":
                        nivel_pelo = 5;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
/***********************************************************************************************************/
        grafico = findViewById(R.id.btn_cad_consulta);
        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_consulta = (EditText) findViewById(R.id.editDataConsulta);
                edit_Peso = (EditText) findViewById(R.id.editPeso);
                edit_Estatura = (EditText) findViewById(R.id.editEstatura);

                boolean erro = true;
                String msgErro = "";

                if (TextUtils.isEmpty(edit_Peso.getText().toString())) {
                    msgErro += "Campo Peso(kg) é obrigatório\n";
                    erro = false;
                }
                if (TextUtils.isEmpty(data_consulta.getText().toString())) {
                    msgErro += "Campo Data consulta é obrigatório\n";
                    erro = false;
                } else {
                    String[] text_data_consulta = data_consulta.getText().toString().split("/");
                    //Toast.makeText(getApplicationContext(), "DIA: " + text_data_consulta[0] + "\nMÊS: " + text_data_consulta[1] + "\nANO: " + text_data_consulta[2], Toast.LENGTH_LONG).show();
                    Date data = new Date();
                    SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataFormatada = formataData.format(data);
                    String[] dataatual = dataFormatada.split("/");
                    //Toast.makeText(getApplicationContext(), "Data: " + data, Toast.LENGTH_LONG).show();
                    if (Integer.parseInt(text_data_consulta[0]) < 01 || Integer.parseInt(text_data_consulta[0]) > 31 || Integer.parseInt(text_data_consulta[1]) > 12 || Integer.parseInt(text_data_consulta[1]) < 01 || Integer.parseInt(text_data_consulta[2]) < 1950 || Integer.parseInt(text_data_consulta[2]) > Integer.parseInt(dataatual[2])) {
                        msgErro += "Data consulta inválida\n";
                        erro = false;
                    }
                }

                if (!dtIniTratamento.equals("") && dtFimTratamento.equals("")) {
                    msgErro += "Você deve definir o fim do tratamento\n";
                    erro = false;
                } else if (dtIniTratamento.equals("") && !dtFimTratamento.equals("")) {
                    msgErro += "Você deve definir o início do tratamento\n";
                    erro = false;
                } else if (!dtIniTratamento.getText().toString().equals("") && !dtFimTratamento.getText().toString().equals("")) {
                    String[] tratIni = dtIniTratamento.getText().toString().split("/");
                    String[] tratFim = dtFimTratamento.getText().toString().split("/");
                    Date data = new Date();
                    SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataFormatada = formataData.format(data);
                    String[] dataatual = dataFormatada.split("/");
                    //Toast.makeText(getApplicationContext(), "Data: " + data, Toast.LENGTH_LONG).show();
                    if (Integer.parseInt(tratIni[0]) < 01 || Integer.parseInt(tratIni[0]) > 31 || Integer.parseInt(tratIni[1]) > 12 || Integer.parseInt(tratIni[1]) < 01 || Integer.parseInt(tratIni[2]) < 1950 || Integer.parseInt(tratFim[0]) < 01 || Integer.parseInt(tratFim[0]) > 31 || Integer.parseInt(tratFim[1]) > 12 || Integer.parseInt(tratFim[1]) < 01 || Integer.parseInt(tratFim[2]) < 1950) {
                        msgErro += "Data início/fim tratamento inválida\n";
                        erro = false;
//                    } else {
//                        if (Integer.parseInt(tratFim[2]) < Integer.parseInt(tratIni[2]) && Integer.parseInt(tratFim[1]) < Integer.parseInt(tratIni[1]) && Integer.parseInt(tratFim[0]) <= Integer.parseInt(tratIni[0])){
//                            msgErro += "Data início tratamento não pode ser superior ao fim\n";
//                            erro = false;
//                        }
//                        else if(Integer.parseInt(tratFim[2]) < Integer.parseInt(tratIni[2]) || Integer.parseInt(tratFim[1]) < Integer.parseInt(tratIni[1])){
//                            msgErro += "Data início tratamento não pode ser superior ao fim\n";
//                            erro = false;
//                        }
////                        else if(Integer.parseInt(tratFim[2]) >= Integer.parseInt(tratIni[2]) && Integer.parseInt(tratFim[1]) >= Integer.parseInt(tratIni[1]) && Integer.parseInt(tratFim[0]) <= Integer.parseInt(tratIni[0])){
////                            msgErro += "Data início tratamento não pode ser superior ou igaul ao fim\n";
////                            erro = false;
////                        }
                    }
                }

                if (TextUtils.isEmpty(edit_Estatura.getText().toString())) {
                    msgErro += "Campo Estatura(cm) é obrigatório";
                    erro = false;
                }

                if (erro) {
                    mFirebaseDatabaseInstance = FirebaseDatabase.getInstance();
                    mDatabaseReference = mFirebaseDatabaseInstance.getReference("consulta");

                    Query query1 = mDatabaseReference.orderByChild("data_consulta").equalTo(data_consulta.getText().toString());

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            if (dataSnapshot.exists()){
                                Iterable<DataSnapshot> respostaChildren = dataSnapshot.getChildren();
                                boolean existe = false;
                                for (DataSnapshot resposta : respostaChildren) {
                                    String uuid = resposta.child("uuid_paciente").getValue().toString();
                                    if(uuid.equals(uuid_paciente)){
                                        existe = true;
                                    }
                                }
                                if(!existe){
                                    //Toast.makeText(getApplicationContext(), "NÃO EXISTE CONSULTA CADASTRADA NESSA DATA", Toast.LENGTH_LONG).show();
                                    int hdia, ndia;
                                    int hmes, nmes;
                                    int hano, nano;
                                    int dia, mes, ano;
                                    int diferenca;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    double peso = Double.parseDouble(edit_Peso.getText().toString().replace(",", "."));
                                    double estatura = Double.parseDouble(edit_Estatura.getText().toString().replace(",", "."));
                                    String[] text_data_consulta = data_consulta.getText().toString().split("/");
                                    double data_consulta_ano = Double.parseDouble(text_data_consulta[2]);
                                    double teste = Double.parseDouble(text_data_consulta[1]);


                                    hdia = Integer.parseInt(text_data_consulta[0]);  /* dia de hoje                                               */
                                    ndia = dia_nascimento[0];  /* dia de nascimento                                         */
                                    hmes = Integer.parseInt(text_data_consulta[1]);  /* mes de hoje                                               */
                                    nmes = mes_nascimento[0];  /* mes de nascimento                                         */
                                    hano = Integer.parseInt(text_data_consulta[2]);  /* ano de hoje                                               */
                                    nano = ano_nascimento[0];  /* ano de nascimento                                         */

                                    /* PROCESSAMENTO DE  DADOS                                                */
                                    diferenca = 365 * hano + 30 * hmes + hdia
                                            - 365 * nano - 30 * nmes - ndia;

                                    ano = diferenca / 365;
                                    diferenca = diferenca % 365;

                                    mes = diferenca / 30;
                                    diferenca = diferenca % 30;

                                    String idadeatual = (String) (ano + "." + mes);

                                    //Toast.makeText(getApplicationContext(),"ano: " + idadeatual, Toast.LENGTH_LONG).show();
                                    if (!edit_MenarcaConsul.getText().toString().equals("") && !sexo.equals("M")) {
                                        String[] dt_menarca = edit_MenarcaConsul.getText().toString().split("/");
                                        int dia_menarca = Integer.parseInt(dt_menarca[0]);
                                        int mes_menarca = Integer.parseInt(dt_menarca[1]);
                                        int ano_menarca = Integer.parseInt(dt_menarca[2]);

                                        int hdiaM, ndiaM;
                                        int hmesM, nmesM;
                                        int hanoM, nanoM;
                                        int diaM, mesM, anoM;
                                        int diferencaM;

                                        hdiaM = dia_menarca;  /* dia menarca                                               */
                                        ndiaM = dia_nascimento[0];  /* dia de nascimento                                         */
                                        hmesM = mes_menarca;  /* mes menarca                                               */
                                        nmesM = mes_nascimento[0];  /* mes de nascimento                                         */
                                        hanoM = ano_menarca;  /* ano menarca                                               */
                                        nanoM = ano_nascimento[0];  /* ano de nascimento                                         */

                                        /* PROCESSAMENTO DE  DADOS                                                */
                                        diferencaM = 365 * hanoM + 30 * hmesM + hdiaM
                                                - 365 * nanoM - 30 * nmesM - ndiaM;

                                        anoM = diferencaM / 365;
                                        diferencaM = diferencaM % 365;

                                        mesM = diferencaM / 30;
                                        diferenca = diferencaM % 30;

                                        idade_menarca = (String) (anoM + "." + mesM);


                                        if (Double.parseDouble(idade_menarca) < 11.5 || Double.parseDouble(idade_menarca) > 15.6) {
                                            alertaMenarca = "Idade menarca fora do normal!";
                                        }
                                    }
                                    else{
                                        idade_menarca = "";
                                    }

                                    if (nivel_pelo == 0 && nivel_mama == 0) {
                                        //Toast.makeText(getApplicationContext(),"ENTREI NO IF", Toast.LENGTH_LONG).show();
                                        myRefPacientes = FirebaseDatabase.getInstance().getReference();
                                        uuid_consulta = myRefPacientes.child("consulta").push().getKey();
                                        Consulta con = new Consulta(uuid_consulta, estatura, peso, nome_paciente, data_consulta.getText().toString(), Double.parseDouble(idadeatual), idade_menarca, uuid_paciente, dtIniTratamento.getText().toString(), dtFimTratamento.getText().toString());
                                        cadastroConsulta(con);
                                        if (!alertaTanner(nivel_pelo, nivel_mama, sexo, Double.parseDouble(idadeatual), alertaMenarca)) {
                                            Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                            hist_consulta.putExtra("nome_paciente", nome_paciente);
                                            hist_consulta.putExtra("id_medico", id_medico);
                                            hist_consulta.putExtra("sexo", sexo);
                                            hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                            hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                                            hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                                            hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                                            hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                                            hist_consulta.putExtra("idade_menarca", idade_menarca);
                                            startActivity(hist_consulta);
                                            finish();
                                        }

                                    } else {
                                        //Toast.makeText(getApplicationContext(),"ENTREI NO ELSE", Toast.LENGTH_LONG).show();
                                        myRefPacientes = FirebaseDatabase.getInstance().getReference();
                                        uuid_consulta = myRefPacientes.child("consulta").push().getKey();
                                        Consulta con = new Consulta(uuid_consulta, nivel_mama, nivel_pelo, estatura, peso, nome_paciente, data_consulta.getText().toString(), Double.parseDouble(idadeatual), idade_menarca, uuid_paciente, dtIniTratamento.getText().toString(), dtFimTratamento.getText().toString());
                                        cadastroConsulta(con);
                                        if (!alertaTanner(nivel_pelo, nivel_mama, sexo, Double.parseDouble(idadeatual), alertaMenarca)) {
                                            Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                            hist_consulta.putExtra("nome_paciente", nome_paciente);
                                            hist_consulta.putExtra("id_medico", id_medico);
                                            hist_consulta.putExtra("sexo", sexo);
                                            hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                            hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                                            hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                                            hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                                            hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                                            hist_consulta.putExtra("idade_menarca", idade_menarca);
                                            startActivity(hist_consulta);
                                            finish();
                                        }

                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "JÁ EXISTE UMA CONSULTA CADASTRADA NESSA DATA", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                //Toast.makeText(getApplicationContext(), "NÃO EXISTE CONSULTA CADASTRADA NESSA DATA", Toast.LENGTH_LONG).show();
                                //                        String idade_menarca = "";
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                        //CALCULA IDADE ATUAL ANOS E MESES
                                int hdia, ndia;
                                int hmes, nmes;
                                int hano, nano;
                                int dia, mes, ano;
                                int diferenca;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                double peso = Double.parseDouble(edit_Peso.getText().toString().replace(",", "."));
                                double estatura = Double.parseDouble(edit_Estatura.getText().toString().replace(",", "."));
                                String[] text_data_consulta = data_consulta.getText().toString().split("/");
                                double data_consulta_ano = Double.parseDouble(text_data_consulta[2]);
                                double teste = Double.parseDouble(text_data_consulta[1]);


                                hdia = Integer.parseInt(text_data_consulta[0]);  /* dia de hoje                                               */
                                ndia = dia_nascimento[0];  /* dia de nascimento                                         */
                                hmes = Integer.parseInt(text_data_consulta[1]);  /* mes de hoje                                               */
                                nmes = mes_nascimento[0];  /* mes de nascimento                                         */
                                hano = Integer.parseInt(text_data_consulta[2]);  /* ano de hoje                                               */
                                nano = ano_nascimento[0];  /* ano de nascimento                                         */

                                /* PROCESSAMENTO DE  DADOS                                                */
                                diferenca = 365 * hano + 30 * hmes + hdia
                                        - 365 * nano - 30 * nmes - ndia;

                                ano = diferenca / 365;
                                diferenca = diferenca % 365;

                                mes = diferenca / 30;
                                diferenca = diferenca % 30;

                                String idadeatual = (String) (ano + "." + mes);

                                //Toast.makeText(getApplicationContext(),"ano: " + idadeatual, Toast.LENGTH_LONG).show();
                                if (!edit_MenarcaConsul.getText().toString().equals("") && !sexo.equals("M")) {
                                    String[] dt_menarca = edit_MenarcaConsul.getText().toString().split("/");
                                    int dia_menarca = Integer.parseInt(dt_menarca[0]);
                                    int mes_menarca = Integer.parseInt(dt_menarca[1]);
                                    int ano_menarca = Integer.parseInt(dt_menarca[2]);

                                    int hdiaM, ndiaM;
                                    int hmesM, nmesM;
                                    int hanoM, nanoM;
                                    int diaM, mesM, anoM;
                                    int diferencaM;

                                    hdiaM = dia_menarca;  /* dia menarca                                               */
                                    ndiaM = dia_nascimento[0];  /* dia de nascimento                                         */
                                    hmesM = mes_menarca;  /* mes menarca                                               */
                                    nmesM = mes_nascimento[0];  /* mes de nascimento                                         */
                                    hanoM = ano_menarca;  /* ano menarca                                               */
                                    nanoM = ano_nascimento[0];  /* ano de nascimento                                         */

                                    /* PROCESSAMENTO DE  DADOS                                                */
                                    diferencaM = 365 * hanoM + 30 * hmesM + hdiaM
                                            - 365 * nanoM - 30 * nmesM - ndiaM;

                                    anoM = diferencaM / 365;
                                    diferencaM = diferencaM % 365;

                                    mesM = diferencaM / 30;
                                    diferenca = diferencaM % 30;

                                    idade_menarca = (String) (anoM + "." + mesM);


                                    if (Double.parseDouble(idade_menarca) < 11.5 || Double.parseDouble(idade_menarca) > 15.6) {
                                        alertaMenarca = "Idade menarca fora do normal!";
                                    }
                                }
                                else{
                                    idade_menarca = "";
                                }

                                if (nivel_pelo == 0 && nivel_mama == 0) {
                                    //Toast.makeText(getApplicationContext(),"ENTREI NO IF", Toast.LENGTH_LONG).show();
                                    myRefPacientes = FirebaseDatabase.getInstance().getReference();
                                    uuid_consulta = myRefPacientes.child("consulta").push().getKey();
                                    Consulta con = new Consulta(uuid_consulta, estatura, peso, nome_paciente, data_consulta.getText().toString(), Double.parseDouble(idadeatual), idade_menarca, uuid_paciente, dtIniTratamento.getText().toString(), dtFimTratamento.getText().toString());
                                    cadastroConsulta(con);
                                    if (!alertaTanner(nivel_pelo, nivel_mama, sexo, Double.parseDouble(idadeatual), alertaMenarca)) {
                                        Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                        hist_consulta.putExtra("nome_paciente", nome_paciente);
                                        hist_consulta.putExtra("id_medico", id_medico);
                                        hist_consulta.putExtra("sexo", sexo);
                                        hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                        hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                                        hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                                        hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                                        hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                                        hist_consulta.putExtra("idade_menarca", idade_menarca);
                                        startActivity(hist_consulta);
                                        finish();
                                    }

                                } else {
                                    //Toast.makeText(getApplicationContext(),"ENTREI NO ELSE", Toast.LENGTH_LONG).show();
                                    myRefPacientes = FirebaseDatabase.getInstance().getReference();
                                    uuid_consulta = myRefPacientes.child("consulta").push().getKey();
                                    Consulta con = new Consulta(uuid_consulta, nivel_mama, nivel_pelo, estatura, peso, nome_paciente, data_consulta.getText().toString(), Double.parseDouble(idadeatual), idade_menarca, uuid_paciente, dtIniTratamento.getText().toString(), dtFimTratamento.getText().toString());
                                    cadastroConsulta(con);
                                    if (!alertaTanner(nivel_pelo, nivel_mama, sexo, Double.parseDouble(idadeatual), alertaMenarca)) {
                                        Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                        hist_consulta.putExtra("nome_paciente", nome_paciente);
                                        hist_consulta.putExtra("id_medico", id_medico);
                                        hist_consulta.putExtra("sexo", sexo);
                                        hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                        hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                                        hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                                        hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                                        hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                                        hist_consulta.putExtra("idade_menarca", idade_menarca);
                                        startActivity(hist_consulta);
                                        finish();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Se ocorrer um erro
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "" + msgErro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> Adapter, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void cadastroConsulta(Consulta con) {
        myRefPacientes = FirebaseDatabase.getInstance().getReference();
        myRefPacientes.child("consulta").child(uuid_consulta).setValue(con).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Consulta cadastrada", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao cadastrar consulta", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean alertaTanner(int npelo, int nmama, final String sexo, Double idade_atual, String msgMenarca) {
        String msg_mama = "";
        String msg_pelo = "";
        boolean existeMsg;
        boolean existeMsgM;

        if (sexo.equals("F")) {
            switch (nmama) {
                case 1:
                    if (idade_atual >= 8) {
                        msg_mama = "Mama fora do padrão!";
                    }
                    break;
                case 2:
                    if (idade_atual < 8 || idade_atual > 13) {
                        msg_mama = "Mama fora do padrão!";
                    }
                    break;
                case 3:
                    if (idade_atual < 10 || idade_atual > 14) {
                        msg_mama = "Mama fora do padrão!";
                    }
                    break;
                case 4:
                    if (idade_atual < 11 || idade_atual > 15) {
                        msg_mama = "Mama fora do padrão!";
                    }
                    break;
                case 5:
                    if (idade_atual < 13) {
                        msg_mama = "Mama fora do padrão!";
                    }
                    break;
                default:
                    break;
            }

            switch (npelo) {
                case 1:
                    if (idade_atual >= 9) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 2:
                    if (idade_atual < 9 || idade_atual > 14) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 3:
                    if (idade_atual < 10 || idade_atual > 14.5) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 4:
                    if (idade_atual < 11 || idade_atual > 15) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 5:
                    if (idade_atual < 12) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                default:
                    break;
            }

            if (!msg_mama.equals("") || !msg_pelo.equals("") || !msgMenarca.equals("")) {
                existeMsg = true;
                AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new AlertDialog.Builder(dados_consulta.this);
                alertDialogBuilder.setTitle("Atenção!");
                alertDialogBuilder.setIcon(R.drawable.alerta_icon_24dp);
                alertDialogBuilder
                        .setMessage(msg_mama + "\n" + msg_pelo + "\n" + msgMenarca)
                        .setCancelable(false)
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                hist_consulta.putExtra("nome_paciente", nome_paciente);
                                hist_consulta.putExtra("id_medico", id_medico);
                                hist_consulta.putExtra("sexo", sexo);
                                hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                                hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                                hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                                hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                                hist_consulta.putExtra("idade_menarca", idade_menarca);
                                startActivity(hist_consulta);
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                existeMsg = false;
            }
        } else {
            switch (nmama) {
                case 1:
                    if (idade_atual > 9.5) {
                        msg_mama = "Gônada fora do padrão!";
                    }
                    break;
                case 2:
                    if (idade_atual < 9.5 || idade_atual > 13.5) {
                        msg_mama = "Gônada fora do padrão!";
                    }
                    break;
                case 3:
                    if (idade_atual < 10.5 || idade_atual > 15) {
                        msg_mama = "Gônada fora do padrão!";
                    }
                    break;
                case 4:
                    if (idade_atual < 11.5 || idade_atual > 16) {
                        msg_mama = "Gônada fora do padrão!";
                    }
                    break;
                case 5:
                    if (idade_atual < 12.5) {
                        msg_mama = "Gônada fora do padrão!";
                    }
                    break;
                default:
                    break;
            }

            switch (npelo) {
                case 1:
                    if (idade_atual > 11) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 2:
                    if (idade_atual < 11 || idade_atual > 15.5) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 3:
                    if (idade_atual < 11.9 || idade_atual > 16) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 4:
                    if (idade_atual < 12 || idade_atual > 16.5) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                case 5:
                    if (idade_atual < 13) {
                        msg_pelo = "Pelos fora do padrão!";
                    }
                    break;
                default:
                    break;
            }

            if (!msg_mama.equals("") || !msg_pelo.equals("")) {
                existeMsg = true;
                AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new AlertDialog.Builder(dados_consulta.this);
                alertDialogBuilder.setTitle("Atenção: Desvio da escala de Tanner");
                alertDialogBuilder.setIcon(R.drawable.alerta_icon_24dp);
                alertDialogBuilder
                        .setMessage(msg_mama + "\n" + msg_pelo)
                        .setCancelable(false)
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                hist_consulta.putExtra("nome_paciente", nome_paciente);
                                hist_consulta.putExtra("id_medico", id_medico);
                                hist_consulta.putExtra("sexo", sexo);
                                hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
                                hist_consulta.putExtra("idadePconsulta", idadePconsulta);
                                hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                                hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                                hist_consulta.putExtra("idade_menarca", idade_menarca);
                                startActivity(hist_consulta);
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                existeMsg = false;
            }
        }

        return existeMsg;
    }


    //MÉTODOS PARA ENCERRAR SESSÃO

    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.acao_sair) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

