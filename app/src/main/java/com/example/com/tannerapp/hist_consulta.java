package com.example.com.tannerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bottomnav.hitherejoe.com.tannerapp.R;

public class hist_consulta extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefPacientes;
    /************************************************************/
    private Button btn_nova_consulta;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private String sexoAtuali;
    ArrayAdapter<String> dataMama, dataPelo;
    int nivel_mama = 0;
    int nivel_pelo = 0;

    int nivel_mamaAtu = 0;
    int nivel_peloAtu = 0;
    /*********************************************************************************************************************/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String uuid_consulta;
    public double escala_mama;
    public double escala_pelo;
    public double altura;
    public double peso;
    public String nome;
    public String data_consulta;
    public double idade_atual;
    public String idade_menarcaF;
    public String uuid_pac;
    public String dtInitratamento;
    public String dtFimtratamento;
    public boolean alertaMama = false;
    public boolean alertaPelo = false;
    public boolean alertaMamaMSG = false;
    public boolean alertaPeloMSG = false;
    public boolean alertaGonada = false;
    public boolean alertaPeloM = false;
    public boolean alertaGonadaMSG = false;
    public boolean alertaPeloMSGM = false;
    public boolean alertaMenarca = false;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*********************************************************************************************************************/
    String nome_paciente;
    String id_medico;
    String sexo;
    String uuid_paciente;
    String data_nascimento;
    String idadePconsulta;
    String idadeUconsulta;
    int qtdConsulta;
    String idade_menarca;
    String[] anoNascimento = new String[1];
    String[] mesNascimento = new String[1];
    String[] anoMenarca = new String[1];
    String[] mesMenarca = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist_consulta);
/*********************************************************************************************************************/


        getSupportActionBar().setTitle("Endocrin App");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(hist_consulta.this, telainicial.class);
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

        btn_nova_consulta = findViewById(R.id.nova_consulta);

/***************************************************************************************************************/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Intent i = getIntent();
        nome_paciente = i.getStringExtra("nome_paciente");
        id_medico = i.getStringExtra("id_medico");
        sexo = i.getStringExtra("sexo");
        uuid_paciente = i.getStringExtra("uuid_paciente");
        data_nascimento = i.getStringExtra("data_nascimento");
        idadePconsulta = i.getStringExtra("idadePconsulta");
        idadeUconsulta = i.getStringExtra("idadeUconsulta");
        qtdConsulta = i.getIntExtra("qtdConsulta", 0);
        idade_menarca = i.getStringExtra("idade_menarca");
        sexoAtuali = sexo;

        //Toast.makeText(getApplicationContext(),"IDADE: " + sexo, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"IDADE ÚLTIMA CONSULTA: " + idadeUconsulta, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"Data nascimento: " + data_nascimento, Toast.LENGTH_LONG).show();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_lista_consulta);
        // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_menu:
                        Intent home = new Intent(hist_consulta.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.action_cadastro_pacientes:
                        Intent cadastro_pacientes = new Intent(hist_consulta.this, dados_paciente.class);
                        cadastro_pacientes.putExtra("nome_paciente", nome_paciente);
                        cadastro_pacientes.putExtra("id_medico", id_medico);
                        cadastro_pacientes.putExtra("sexo", sexo);
                        cadastro_pacientes.putExtra("uuid_paciente", uuid_paciente);
                        cadastro_pacientes.putExtra("data_nascimento", data_nascimento);
                        cadastro_pacientes.putExtra("idadePconsulta", idadePconsulta);
                        cadastro_pacientes.putExtra("idadeUconsulta", idadeUconsulta);
                        cadastro_pacientes.putExtra("qtdConsulta", qtdConsulta);
                        cadastro_pacientes.putExtra("idade_menarca", idade_menarca);
                        startActivity(cadastro_pacientes);
                        break;
                    case R.id.action_cadastro_consulta:
                        Intent cadastro_consulta = new Intent(hist_consulta.this, dados_consulta.class);
                        cadastro_consulta.putExtra("nome_paciente", nome_paciente);
                        cadastro_consulta.putExtra("id_medico", id_medico);
                        cadastro_consulta.putExtra("sexo", sexo);
                        cadastro_consulta.putExtra("uuid_paciente", uuid_paciente);
                        cadastro_consulta.putExtra("data_nascimento", data_nascimento);
                        cadastro_consulta.putExtra("idadePconsulta", idadePconsulta);
                        cadastro_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                        cadastro_consulta.putExtra("qtdConsulta", qtdConsulta);
                        cadastro_consulta.putExtra("idade_menarca", idade_menarca);
                        startActivity(cadastro_consulta);
                        break;
                    case R.id.action_hist_consultas:

                        break;
                    case R.id.action_grafico:
                        Intent grafico = new Intent(hist_consulta.this, grafico.class);
                        grafico.putExtra("nome_paciente", nome_paciente);
                        grafico.putExtra("id_medico", id_medico);
                        grafico.putExtra("sexo", sexo);
                        grafico.putExtra("uuid_paciente", uuid_paciente);
                        grafico.putExtra("data_nascimento", data_nascimento);
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


///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**************************************************************************************************************/

        // button = findViewById(R.id.teste);
        recyclerView = findViewById(R.id.list);

        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch(uuid_paciente);


        btn_nova_consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastro_consulta = new Intent(hist_consulta.this, dados_consulta.class);
                cadastro_consulta.putExtra("nome_paciente", nome_paciente);
                cadastro_consulta.putExtra("id_medico", id_medico);
                cadastro_consulta.putExtra("sexo", sexo);
                cadastro_consulta.putExtra("uuid_paciente", uuid_paciente);
                cadastro_consulta.putExtra("data_nascimento", data_nascimento);
                cadastro_consulta.putExtra("idadePconsulta", idadePconsulta);
                cadastro_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                cadastro_consulta.putExtra("qtdConsulta", qtdConsulta);
                cadastro_consulta.putExtra("idade_menarca", idade_menarca);
                startActivity(cadastro_consulta);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout root;
        public final TextView txtTitle;
        public TextView txtDesc;
        public ImageView alerta;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.list_data);
            alerta = itemView.findViewById(R.id.alerta);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }
    }

    public void fetch(final String uuid) {
        myRefPacientes = database.getReference("consulta");
        FirebaseRecyclerOptions<Consulta> options = null;
        Query queryConsulta = myRefPacientes.orderByChild("idade_atual");
        // if(uuid.equals("-LpdesRwHMcwpI3OJeJT")){
        options = new FirebaseRecyclerOptions.Builder<Consulta>().setQuery(queryConsulta, new SnapshotParser<Consulta>() {
            @NonNull
            @Override
            public final Consulta parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new Consulta(
                        snapshot.child("uuid_consulta").getValue().toString(),
                        Double.parseDouble(snapshot.child("escala_mama").getValue().toString()),
                        Double.parseDouble(snapshot.child("escala_pelo").getValue().toString()),
                        Double.parseDouble(snapshot.child("altura").getValue().toString()),
                        Double.parseDouble(snapshot.child("peso").getValue().toString()),
                        snapshot.child("nome_paciente").getValue().toString(),
                        snapshot.child("data_consulta").getValue().toString(),
                        Double.parseDouble(snapshot.child("idade_atual").getValue().toString()),
                        snapshot.child("idade_menarca").getValue().toString(),
                        snapshot.child("uuid_paciente").getValue().toString(),
                        snapshot.child("dtIniTratamento").getValue().toString(),
                        snapshot.child("dtFimTratamento").getValue().toString()
                );
            }
        }).build();
        //}
        adapter = new FirebaseRecyclerAdapter<Consulta, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Consulta consul = new Consulta(uuid_consulta, escala_mama, escala_pelo, altura, peso, nome, data_consulta, idade_atual, idade_menarca, uuid_pac, dtInitratamento, dtFimtratamento);
                //if(uuid_pac.equals(uuid)){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_consulta, parent, false);
                return new ViewHolder(view);
            }

//            @Override
//            public int getItemCount() {
//                return
//            }


            @Override
            protected void onBindViewHolder(final ViewHolder holder, final int position, final Consulta consul) {
                final int escala_mama;
                int escala_pelo;
                alertaMama = false;
                alertaPelo = false;
                alertaGonada = false;
                alertaPeloM = false;
                alertaMenarca = false;
                alertaMamaMSG = false;
                alertaPeloMSG = false;
                alertaGonadaMSG = false;
                alertaPeloMSGM = false;

                if (consul.getUuid_paciente().equals(uuid)) {
                    //holder.alerta.setVisibility(View.GONE);
                    //holder.itemView.bringToFront();
                    if (sexoAtuali.equals("F")) {
                        escala_mama = (int) consul.getEscala_mama();
                        escala_pelo = (int) consul.getEscala_pelo();

                        if (!consul.getIdade_menarca().equals("")) {
                            if (Double.parseDouble(consul.getIdade_menarca()) < 11.5 || Double.parseDouble(consul.getIdade_menarca()) > 15.6) {
                                alertaMenarca = true;
                            }
                        }

                        switch (escala_mama) {
                            case 1:
                                if (consul.getIdade_atual() >= 8) {
                                    alertaMama = true;
                                    alertaMamaMSG = true;
                                }
                                break;
                            case 2:
                                if (consul.getIdade_atual() < 8 || consul.getIdade_atual() > 13) {
                                    alertaMama = true;
                                    alertaMamaMSG = true;
                                }
                                break;
                            case 3:
                                if (consul.getIdade_atual() < 10 || consul.getIdade_atual() > 14) {
                                    alertaMama = true;
                                    alertaMamaMSG = true;
                                }
                                break;
                            case 4:
                                if (consul.getIdade_atual() < 11 || consul.getIdade_atual() > 15) {
                                    alertaMama = true;
                                    alertaMamaMSG = true;
                                }
                                break;
                            case 5:
                                if (consul.getIdade_atual() < 13) {
                                    alertaMama = true;
                                    alertaMamaMSG = true;
                                }
                                break;
                            default:
                                break;
                        }

                        switch (escala_pelo) {
                            case 1:
                                if (consul.getIdade_atual() >= 9) {
                                    alertaPelo = true;
                                    alertaPeloMSG = true;
                                }
                                break;
                            case 2:
                                if (consul.getIdade_atual() < 9 || consul.getIdade_atual() > 14) {
                                    alertaPelo = true;
                                    alertaPeloMSG = true;
                                }
                                break;
                            case 3:
                                if (consul.getIdade_atual() < 10 || consul.getIdade_atual() > 14.5) {
                                    alertaPelo = true;
                                    alertaPeloMSG = true;
                                }
                                break;
                            case 4:
                                if (consul.getIdade_atual() < 11 || consul.getIdade_atual() > 15) {
                                    alertaPelo = true;
                                    alertaPeloMSG = true;
                                }
                                break;
                            case 5:
                                if (consul.getIdade_atual() < 12) {
                                    alertaPelo = true;
                                    alertaPeloMSG = true;
                                }
                                break;
                            default:
                                break;
                        }
//
                        if (alertaMama || alertaPelo || alertaMenarca) {
                            holder.alerta.setVisibility(View.VISIBLE);
                        }
                    } else {
                        int escala_gonada = (int) consul.getEscala_mama();
                        int escala_peloM = (int) consul.getEscala_pelo();

                        switch (escala_gonada) {
                            case 1:
                                if (consul.getIdade_atual() > 9.5) {
                                    alertaGonada = true;
                                    alertaGonadaMSG = true;
                                }
                                break;
                            case 2:
                                if (consul.getIdade_atual() < 9.5 || consul.getIdade_atual() > 13.5) {
                                    alertaGonada = true;
                                    alertaGonadaMSG = true;
                                }
                                break;
                            case 3:
                                if (consul.getIdade_atual() < 10.5 || consul.getIdade_atual() > 15) {
                                    alertaGonada = true;
                                    alertaGonadaMSG = true;
                                }
                                break;
                            case 4:
                                if (consul.getIdade_atual() < 11.5 || consul.getIdade_atual() > 16) {
                                    alertaGonada = true;
                                    alertaGonadaMSG = true;
                                }
                                break;
                            case 5:
                                if (consul.getIdade_atual() < 12.5) {
                                    alertaGonada = true;
                                    alertaGonadaMSG = true;
                                }
                                break;
                            default:
                                break;
                        }

                        switch (escala_peloM) {
                            case 1:
                                if (consul.getIdade_atual() > 11) {
                                    alertaPeloM = true;
                                    alertaPeloMSGM = true;
                                }
                                break;
                            case 2:
                                if (consul.getIdade_atual() < 11 || consul.getIdade_atual() > 15.5) {
                                    alertaPeloM = true;
                                    alertaPeloMSGM = true;
                                }
                                break;
                            case 3:
                                if (consul.getIdade_atual() < 11.9 || consul.getIdade_atual() > 16) {
                                    alertaPeloM = true;
                                    alertaPeloMSGM = true;
                                }
                                break;
                            case 4:
                                if (consul.getIdade_atual() < 12 || consul.getIdade_atual() > 16.5) {
                                    alertaPeloM = true;
                                    alertaPeloMSGM = true;
                                }
                                break;
                            case 5:
                                if (consul.getIdade_atual() < 13) {
                                    alertaPeloM = true;
                                    alertaPeloMSGM = true;
                                }
                                break;
                            default:
                                break;
                        }

                        if (alertaGonada || alertaPeloM) {
                            holder.alerta.setVisibility(View.VISIBLE);
                        }
                    }

                    /**MOSTRAR DADOS DA CONSULTA AQUI**/
                    final AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new AlertDialog.Builder(hist_consulta.this);

                    /**************************************************************************/
                    String msg = "";
                    String[] dados = consul.getIdade_menarca().split("\\.");
                    String[] dadosConsulta = String.valueOf(consul.getIdade_atual()).split("\\.");
                    String[] GonadaMama = String.valueOf(consul.escala_mama).split("\\.");
                    String[] Pelo = String.valueOf(consul.getEscala_pelo()).split("\\.");
                    String mes;
//                            Toast.makeText(getApplicationContext(),"GONADAMAMA: " + GonadaMama[0], Toast.LENGTH_LONG).show();
//                            Toast.makeText(getApplicationContext(),"PELO: " + Pelo[0], Toast.LENGTH_LONG).show();

                    anoNascimento[0] = dadosConsulta[0];
                    mesNascimento[0] = dadosConsulta[1];

                    if(mesNascimento[0].equals("1")){
                        mes = "mês";
                    }
                    else{
                        mes = "meses";
                    }

                    if(dados.length == 2){
                        anoMenarca[0] = dados[0];
                        mesMenarca[0] = dados[1];
                    }
                    else{
                        anoMenarca[0] = dados[0];
                        mesMenarca[0] = "";

                    }

                    if (sexoAtuali.equals("F")) {
                        String msgmenarca = "\nIdade menarca: " + anoMenarca[0] + " anos e " + mesMenarca[0] + " meses \n";
                        String pelo = "\nEscala pelo: " + Pelo[0];
                        String mama = "\nEscala mama: " + GonadaMama[0];
                        String mensagem = "Nome paciente: " + consul.getNome_paciente() + "\nAltura(cm): " + consul.getAltura() + "\nPeso(kg): " + consul.getPeso() + "\nIdade: " + anoNascimento[0] + " anos " + mesNascimento[0] + " " + mes + "\nInício tratamento: " + consul.getDtIniTratamento() + "\nFim tratamento: " + consul.getDtFimTratamento();
                        SpannableStringBuilder msgFinal = new SpannableStringBuilder(mensagem);
                        SpannableStringBuilder msgMenarca = new SpannableStringBuilder(msgmenarca);
                        SpannableStringBuilder msgMama = new SpannableStringBuilder(mama);
                        SpannableStringBuilder msgPelo = new SpannableStringBuilder(pelo);

                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.BLACK);
                        msgFinal.setSpan(foregroundColorSpan2, 0, mensagem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        msgPelo.setSpan(foregroundColorSpan2, 0, pelo.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        if (!consul.getIdade_menarca().equals("")){
                            if (Double.parseDouble(consul.getIdade_menarca()) < 11.5 || Double.parseDouble(consul.getIdade_menarca()) > 15.6){
                                msgMenarca.setSpan(foregroundColorSpan, 0, msgmenarca.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                        else{
                            msgmenarca = "";
                            msgMenarca = new SpannableStringBuilder(msgmenarca);
                        }

                        if(alertaMamaMSG && alertaPeloMSG){
                            msgMama.setSpan(foregroundColorSpan, 0, mama.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                            msgPelo.setSpan(foregroundColorSpan, 0, pelo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                        else if(alertaMamaMSG){
                            msgMama.setSpan(foregroundColorSpan, 0, mama.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        else if(alertaPeloMSG){
                            msgPelo.setSpan(foregroundColorSpan, 0, pelo.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        else{
                            msgMama.setSpan(foregroundColorSpan2, 0, mama.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                            msgPelo.setSpan(foregroundColorSpan2, 0, pelo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        }

                        msgFinal.append(msgMama.append(msgPelo));
                        msgFinal.append(msgMenarca);
                        alertDialogBuilder.setMessage(msgFinal);

                    } else {
                        String pelo = "\nEscala pelo: " + Pelo[0];
                        String mama = "\nEscala gônada: " + GonadaMama[0];
                        String mensagem = "Nome paciente: " + consul.getNome_paciente() + "\nAltura(cm): " + consul.getAltura() + "\nPeso(kg): " + consul.getPeso() + "\nIdade: " + anoNascimento[0] + " anos " + mesNascimento[0] + " " + mes + "\nInício tratamento: " + consul.getDtIniTratamento() + "\nFim tratamento: " + consul.getDtFimTratamento();
                        SpannableStringBuilder msgFinal = new SpannableStringBuilder(mensagem);
                        SpannableStringBuilder msgMama = new SpannableStringBuilder(mama);
                        SpannableStringBuilder msgPelo = new SpannableStringBuilder(pelo);

                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.BLACK);
                        msgFinal.setSpan(foregroundColorSpan2, 0, mensagem.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                        if(alertaGonadaMSG && alertaPeloMSGM){
                            msgMama.setSpan(foregroundColorSpan, 0, mama.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                            msgPelo.setSpan(foregroundColorSpan, 0, pelo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                        else if(alertaGonadaMSG){
                            msgMama.setSpan(foregroundColorSpan, 0, mama.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        else if(alertaPeloMSGM){
                            msgPelo.setSpan(foregroundColorSpan, 0, pelo.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        else{
                            msgMama.setSpan(foregroundColorSpan2, 0, mama.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                            msgPelo.setSpan(foregroundColorSpan2, 0, pelo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        }

                        alertDialogBuilder.setMessage(msgFinal.append(msgMama.append(msgPelo)));
                    }

                    holder.setTxtTitle("Dados da consulta: " + String.valueOf(consul.getData_consulta()));
                    holder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialogBuilder.setTitle("");
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                        /*******************************************************************************************************************************/
                            alertDialogBuilder.setNegativeButton("Editar dados consulta", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(hist_consulta.this);
                                    dialogBuilder.setTitle("Editar dados consulta");
                                    LayoutInflater inflater = (hist_consulta.this).getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.activity_atualiza_consulta, null);
                                    dialogBuilder.setView(dialogView);

                                    final EditText editData = (EditText) dialogView.findViewById(R.id.editDataCon);
                                    final EditText editNome = (EditText) dialogView.findViewById(R.id.editNpaciente);
                                    final EditText editIdade = (EditText) dialogView.findViewById(R.id.editNidade);
                                    final EditText editAltura = (EditText) dialogView.findViewById(R.id.editAlt);
                                    final EditText editPeso = (EditText) dialogView.findViewById(R.id.editPesoPac);
                                    final EditText editIdadeMenarcaAt = (EditText) dialogView.findViewById(R.id.editMenarcaAtu);
                                    final EditText editIiniTratamentoAt = (EditText) dialogView.findViewById(R.id.editIniTratamentoAtu);
                                    final EditText editFimTratamentoAt = (EditText) dialogView.findViewById(R.id.editFimTratamentoAtu);
                                    editIiniTratamentoAt.addTextChangedListener(MaskEditUtil.mask(editIiniTratamentoAt, MaskEditUtil.FORMAT_DATE));
                                    editFimTratamentoAt.addTextChangedListener(MaskEditUtil.mask(editFimTratamentoAt, MaskEditUtil.FORMAT_DATE));
                                    /******************************************************************************/
                                    TextView txtEditTanner = (TextView) dialogView.findViewById(R.id.txtEscalaTannerAtuali);
                                    final Spinner spinnerMama = (Spinner) dialogView.findViewById(R.id.spinnerMamaAtuali);
                                    final Spinner spinnerPelo = (Spinner) dialogView.findViewById(R.id.spinnerPeloAtuali);
                                    final CheckBox chEscTanner = (CheckBox) dialogView.findViewById(R.id.chEscTannerAtuali);
                                    spinnerMama.setOnItemSelectedListener(hist_consulta.this);
                                    spinnerPelo.setOnItemSelectedListener(hist_consulta.this);
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
                                    dataMama = new ArrayAdapter<String>(hist_consulta.this, android.R.layout.simple_spinner_item, escTanner[0]);
                                    dataPelo = new ArrayAdapter<String>(hist_consulta.this, android.R.layout.simple_spinner_item, Pelo[0]);

                                    // Drop down layout style - list view with radio button
                                    dataMama.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerMama.setAdapter(dataMama);
                                    spinnerMama.setOnItemSelectedListener(hist_consulta.this);

                                    dataPelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerPelo.setAdapter(dataPelo);
                                    spinnerPelo.setOnItemSelectedListener(hist_consulta.this);

                                    spinnerMama.setOnItemSelectedListener(new OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            switch (adapterView.getItemAtPosition(i).toString()) {
                                                case "M1":
                                                    nivel_mamaAtu = 1;
                                                    break;
                                                case "M2":
                                                    nivel_mamaAtu = 2;
                                                    break;
                                                case "M3":
                                                    nivel_mamaAtu = 3;
                                                    break;
                                                case "M4":
                                                    nivel_mamaAtu = 4;
                                                    break;
                                                case "M5":
                                                    nivel_mamaAtu = 5;
                                                    break;
                                                case "G1":
                                                    nivel_mamaAtu = 1;
                                                    break;
                                                case "G2":
                                                    nivel_mamaAtu = 2;
                                                    break;
                                                case "G3":
                                                    nivel_mamaAtu = 3;
                                                    break;
                                                case "G4":
                                                    nivel_mamaAtu = 4;
                                                    break;
                                                case "G5":
                                                    nivel_mamaAtu = 5;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });


                                    spinnerPelo.setOnItemSelectedListener(new OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            switch (adapterView.getItemAtPosition(i).toString()) {
                                                case "P1":
                                                    nivel_peloAtu = 1;
                                                    break;
                                                case "P2":
                                                    nivel_peloAtu = 2;
                                                    break;
                                                case "P3":
                                                    nivel_peloAtu = 3;
                                                    break;
                                                case "P4":
                                                    nivel_peloAtu = 4;
                                                    break;
                                                case "P5":
                                                    nivel_peloAtu = 5;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    /*******************************************************************************/
                                    editData.setText(consul.getData_consulta());
                                    editData.setEnabled(false);
                                    editNome.setText(consul.getNome_paciente());
                                    editNome.setEnabled(false);
                                    editIdade.setText(String.valueOf(consul.getIdade_atual()));
                                    editIdade.setEnabled(false);
                                    editAltura.setText(String.valueOf(consul.getAltura()));
                                    editPeso.setText(String.valueOf(consul.getPeso()));

                                    editIdadeMenarcaAt.setText(consul.getIdade_menarca());
                                    if(consul.getIdade_menarca().equals("")){
                                        editIdadeMenarcaAt.setEnabled(false);
                                    }
                                    editIiniTratamentoAt.setText(consul.getDtIniTratamento());
                                    if(consul.getDtIniTratamento().equals("")){
                                        editIiniTratamentoAt.setEnabled(false);
                                    }
                                    editFimTratamentoAt.setText(consul.getDtFimTratamento());
                                    if(consul.getDtIniTratamento().equals("")){
                                        editFimTratamentoAt.setEnabled(false);
                                    }
                                    final String uuid_consul = consul.get_uuid_consulta();
                                    //final Button button1 = (Button) dialogView.findViewById(R.id.btn_atualiza_consulta);

                                    /*****************************************************************************************************/
                                    /*****************************************************************************************************/
                                    /*****************************************************************************************************/
                                    /*****************************************************************************************************/
//                                    button1.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    });
                                    dialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    dialogBuilder.setNeutralButton("Atualizar dados", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String nome = editNome.getText().toString();
                                            double idade = Double.parseDouble(editIdade.getText().toString());
                                            String peso = editPeso.getText().toString();
                                            double altura = Double.parseDouble(editAltura.getText().toString());
                                            String idadeMenarc = editIdadeMenarcaAt.getText().toString();
                                            String dtIniTrat = editIiniTratamentoAt.getText().toString();
                                            String dtFimTrat = editFimTratamentoAt.getText().toString();

                                            myRefPacientes = FirebaseDatabase.getInstance().getReference().child("consulta").child(uuid_consul);

                                            /************************************************************************************************************************************/
                                            /************************************************************************************************************************************/
                                            String msg_mamaAtu = "";
                                            String msg_peloAtu = "";

                                            if (sexo.equals("F")) {
                                                switch (nivel_mamaAtu) {
                                                    case 1:
                                                        if (idade >= 8) {
                                                            msg_mamaAtu = "Mama fora do padrão!";
                                                        }
                                                        break;
                                                    case 2:
                                                        if (idade < 8 || idade_atual > 13) {
                                                            msg_mamaAtu = "Mama fora do padrão!";
                                                        }
                                                        break;
                                                    case 3:
                                                        if (idade < 10 || idade_atual > 14) {
                                                            msg_mamaAtu = "Mama fora do padrão!";
                                                        }
                                                        break;
                                                    case 4:
                                                        if (idade < 11 || idade_atual > 15) {
                                                            msg_mamaAtu = "Mama fora do padrão!";
                                                        }
                                                        break;
                                                    case 5:
                                                        if (idade < 13) {
                                                            msg_mamaAtu = "Mama fora do padrão!";
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }

                                                switch (nivel_peloAtu) {
                                                    case 1:
                                                        if (idade >= 9) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 2:
                                                        if (idade < 9 || idade_atual > 14) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 3:
                                                        if (idade < 10 || idade_atual > 14.5) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 4:
                                                        if (idade < 11 || idade_atual > 15) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 5:
                                                        if (idade < 12) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }

                                                if (!msg_mamaAtu.equals("") || !msg_peloAtu.equals("")) {
                                                    AlertDialog.Builder alertDialogBuilder;
                                                    alertDialogBuilder = new AlertDialog.Builder(dialogView.getContext());
                                                    alertDialogBuilder.setTitle("Atenção!");
                                                    alertDialogBuilder.setIcon(R.drawable.alerta_icon_24dp);
                                                    alertDialogBuilder
                                                            .setMessage(msg_mamaAtu + "\n" + msg_peloAtu)
                                                            .setCancelable(false)
                                                            .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                                                    hist_consulta.putExtra("nome_paciente", nome_paciente);
                                                                    hist_consulta.putExtra("id_medico", id_medico);
                                                                    hist_consulta.putExtra("sexo", sexo);
                                                                    hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                                                    hist_consulta.putExtra("data_nascimento", data_nascimento);
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
                                                }
                                            }
                                            else
                                            {
                                                switch (nivel_mamaAtu) {
                                                    case 1:
                                                        if (idade_atual > 9.5) {
                                                            msg_mamaAtu = "Gônada fora do padrão!";
                                                        }
                                                        break;
                                                    case 2:
                                                        if (idade_atual < 9.5 || idade_atual > 13.5) {
                                                            msg_mamaAtu = "Gônada fora do padrão!";
                                                        }
                                                        break;
                                                    case 3:
                                                        if (idade_atual < 10.5 || idade_atual > 15) {
                                                            msg_mamaAtu = "Gônada fora do padrão!";
                                                        }
                                                        break;
                                                    case 4:
                                                        if (idade_atual < 11.5 || idade_atual > 16) {
                                                            msg_mamaAtu = "Gônada fora do padrão!";
                                                        }
                                                        break;
                                                    case 5:
                                                        if (idade_atual < 12.5) {
                                                            msg_mamaAtu = "Gônada fora do padrão!";
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }

                                                switch (nivel_peloAtu) {
                                                    case 1:
                                                        if (idade_atual > 11) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 2:
                                                        if (idade_atual < 11 || idade_atual > 15.5) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 3:
                                                        if (idade_atual < 11.9 || idade_atual > 16) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 4:
                                                        if (idade_atual < 12 || idade_atual > 16.5) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    case 5:
                                                        if (idade_atual < 13) {
                                                            msg_peloAtu = "Pelos fora do padrão!";
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }

                                                if (!msg_mamaAtu.equals("") || !msg_peloAtu.equals("")) {
                                                    AlertDialog.Builder alertDialogBuilder;
                                                    alertDialogBuilder = new AlertDialog.Builder(dialogView.getContext());
                                                    alertDialogBuilder.setTitle("Atenção!");
                                                    alertDialogBuilder.setIcon(R.drawable.alerta_icon_24dp);
                                                    alertDialogBuilder
                                                            .setMessage(msg_mamaAtu + "\n" + msg_peloAtu)
                                                            .setCancelable(false)
                                                            .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
                                                                    hist_consulta.putExtra("nome_paciente", nome_paciente);
                                                                    hist_consulta.putExtra("id_medico", id_medico);
                                                                    hist_consulta.putExtra("sexo", sexo);
                                                                    hist_consulta.putExtra("uuid_paciente", uuid_paciente);
                                                                    hist_consulta.putExtra("data_nascimento", data_nascimento);
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
                                                }
                                            }
                                            /************************************************************************************************************************************/
                                            /************************************************************************************************************************************/

                                            Consulta c = new Consulta(uuid_consul, nivel_mamaAtu, nivel_peloAtu, altura, Double.parseDouble(peso), nome, consul.getData_consulta(), idade, idadeMenarc, consul.getUuid_paciente(), dtIniTrat, dtFimTrat);
                                            myRefPacientes.setValue(c, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError != null) {
                                                        System.out.println("Data could not be saved " + databaseError.getMessage());
                                                    } else {
                                                        System.out.println("Data saved successfully.");
                                                    }
                                                }
                                            });

                                            dialogInterface.cancel();
                                        }
                                    });
                                    /**************************************************/
                                    /*************************************************/
                                    AlertDialog alertDialog = dialogBuilder.create();
                                    alertDialog.show();
                        /*******************************************************************************************************************************/
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }
        };

        recyclerView.setAdapter(adapter);
    }


//    public void atualizaConsulta(String uuid_consul, String nome, double idade, double altura, double peso, int nmama, int npelo){
//
//    }
//
//
//    public boolean alertaTanner(int npelo, int nmama, final String sexo, Double idade_atual, String msgMenarca) {
//        String msg_mama = "";
//        String msg_pelo = "";
//        boolean existeMsg;
//        boolean existeMsgM;
//
//        if (sexo.equals("F")) {
//            switch (nmama) {
//                case 1:
//                    if (idade_atual >= 8) {
//                        msg_mama = "Mama fora do padrão!";
//                    }
//                    break;
//                case 2:
//                    if (idade_atual < 8 || idade_atual > 13) {
//                        msg_mama = "Mama fora do padrão!";
//                    }
//                    break;
//                case 3:
//                    if (idade_atual < 10 || idade_atual > 14) {
//                        msg_mama = "Mama fora do padrão!";
//                    }
//                    break;
//                case 4:
//                    if (idade_atual < 11 || idade_atual > 15) {
//                        msg_mama = "Mama fora do padrão!";
//                    }
//                    break;
//                case 5:
//                    if (idade_atual < 13) {
//                        msg_mama = "Mama fora do padrão!";
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            switch (npelo) {
//                case 1:
//                    if (idade_atual >= 9) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 2:
//                    if (idade_atual < 9 || idade_atual > 14) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 3:
//                    if (idade_atual < 10 || idade_atual > 14.5) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 4:
//                    if (idade_atual < 11 || idade_atual > 15) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 5:
//                    if (idade_atual < 12) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            if (!msg_mama.equals("") || !msg_pelo.equals("") || !msgMenarca.equals("")) {
//                existeMsg = true;
//                AlertDialog.Builder alertDialogBuilder;
//                alertDialogBuilder = new AlertDialog.Builder(dados_consulta.this);
//                alertDialogBuilder.setTitle("Atenção!");
//                alertDialogBuilder.setIcon(R.drawable.alerta_icon_24dp);
//                alertDialogBuilder
//                        .setMessage(msg_mama + "\n" + msg_pelo + "\n" + msgMenarca)
//                        .setCancelable(false)
//                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
//                                hist_consulta.putExtra("nome_paciente", nome_paciente);
//                                hist_consulta.putExtra("id_medico", id_medico);
//                                hist_consulta.putExtra("sexo", sexo);
//                                hist_consulta.putExtra("uuid_paciente", uuid_paciente);
//                                hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
//                                hist_consulta.putExtra("idadePconsulta", idadePconsulta);
//                                hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
//                                hist_consulta.putExtra("qtdConsulta", qtdConsulta);
//                                hist_consulta.putExtra("idade_menarca", idade_menarca);
//                                startActivity(hist_consulta);
//                                finish();
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            } else {
//                existeMsg = false;
//            }
//        } else {
//            switch (nmama) {
//                case 1:
//                    if (idade_atual > 9.5) {
//                        msg_mama = "Gônada fora do padrão!";
//                    }
//                    break;
//                case 2:
//                    if (idade_atual < 9.5 || idade_atual > 13.5) {
//                        msg_mama = "Gônada fora do padrão!";
//                    }
//                    break;
//                case 3:
//                    if (idade_atual < 10.5 || idade_atual > 15) {
//                        msg_mama = "Gônada fora do padrão!";
//                    }
//                    break;
//                case 4:
//                    if (idade_atual < 11.5 || idade_atual > 16) {
//                        msg_mama = "Gônada fora do padrão!";
//                    }
//                    break;
//                case 5:
//                    if (idade_atual < 12.5) {
//                        msg_mama = "Gônada fora do padrão!";
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            switch (npelo) {
//                case 1:
//                    if (idade_atual > 11) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 2:
//                    if (idade_atual < 11 || idade_atual > 15.5) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 3:
//                    if (idade_atual < 11.9 || idade_atual > 16) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 4:
//                    if (idade_atual < 12 || idade_atual > 16.5) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                case 5:
//                    if (idade_atual < 13) {
//                        msg_pelo = "Pelos fora do padrão!";
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            if (!msg_mama.equals("") || !msg_pelo.equals("")) {
//                existeMsg = true;
//                AlertDialog.Builder alertDialogBuilder;
//                alertDialogBuilder = new AlertDialog.Builder(dados_consulta.this);
//                alertDialogBuilder.setTitle("Atenção: Desvio da escala de Tanner");
//                alertDialogBuilder.setIcon(R.drawable.alerta_icon_24dp);
//                alertDialogBuilder
//                        .setMessage(msg_mama + "\n" + msg_pelo)
//                        .setCancelable(false)
//                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                Intent hist_consulta = new Intent(getApplicationContext(), hist_consulta.class);
//                                hist_consulta.putExtra("nome_paciente", nome_paciente);
//                                hist_consulta.putExtra("id_medico", id_medico);
//                                hist_consulta.putExtra("sexo", sexo);
//                                hist_consulta.putExtra("uuid_paciente", uuid_paciente);
//                                hist_consulta.putExtra("data_nascimento", data_nascimento_consul);
//                                hist_consulta.putExtra("idadePconsulta", idadePconsulta);
//                                hist_consulta.putExtra("idadeUconsulta", idadeUconsulta);
//                                hist_consulta.putExtra("qtdConsulta", qtdConsulta);
//                                hist_consulta.putExtra("idade_menarca", idade_menarca);
//                                startActivity(hist_consulta);
//                                finish();
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            } else {
//                existeMsg = false;
//            }
//        }
//
//        return existeMsg;
//    }


    @Override
    public void onBackPressed(){

    }

    @Override
    public void onItemSelected(AdapterView<?> Adapter, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
