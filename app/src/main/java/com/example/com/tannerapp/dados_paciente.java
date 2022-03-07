package com.example.com.tannerapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import bottomnav.hitherejoe.com.tannerapp.R;

public class dados_paciente extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabaseInstance;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference databaseReference;


    Button btn_teste;
    String s, responsavel;
    boolean verificaPacient = false;
    int cont = 0;
    final String[] key = new String[1];

    String nome_pacienteCad = "";
    String id_medicoCad = "";
    String sexoCad = "";
    String uuid_pacienteCad = "";
    String data_nascimento;
    String idadePconsulta;
    String idadeUconsulta;
    String qtdConsulta;
    String idade_menarca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_paciente);

        /*********************************************************************************************************************/
        getSupportActionBar().setTitle("Endocrin App");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(dados_paciente.this, telainicial.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseReference = LibraryClass.getFirebase();
        databaseReference.getRef();
        final FirebaseUser usuario;
/*********************************************************************************************************************************************/

/***************************************************************************************************************/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Intent i = getIntent();
        final String nome_paciente = i.getStringExtra("nome_paciente");
        final String id_medico = i.getStringExtra("id_medico");
        final String sexo = i.getStringExtra("sexo");
        final String uuid_paciente = i.getStringExtra("uuid_paciente");
        final String data_nascimento = i.getStringExtra("data_nascimento");
        final String idadePconsulta = i.getStringExtra("idadePconsulta");
        final String idadeUconsulta = i.getStringExtra("idadeUconsulta");
        final int qtdConsulta = i.getIntExtra("qtdConsulta", 0);
        final String idade_menarca = i.getStringExtra("idade_menarca");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**************************************************************************************************************/
        final EditText nome = (EditText) findViewById(R.id.editNome);
        final EditText editDataNascimento = (EditText) findViewById(R.id.editDataNascimento);
        final EditText editResonsavel = (EditText) findViewById(R.id.editResponsavel);
        final RadioButton rdMasculino = (RadioButton) findViewById(R.id.rdMasculino);
        final RadioButton rdFeminino = (RadioButton) findViewById(R.id.rdFeminino);
        final EditText email = (EditText) findViewById(R.id.editEmail);

        editDataNascimento.addTextChangedListener(MaskEditUtil.mask(editDataNascimento, MaskEditUtil.FORMAT_DATE));

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_paciente);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_menu:
                        Intent home = new Intent(dados_paciente.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.action_cadastro_pacientes:

                        break;
                    case R.id.action_cadastro_consulta:
                        if (nome_paciente.equals("") || sexo.equals("")) {
                            Toast.makeText(getApplicationContext(), "Você precisa cadastrar um paciente para acessar essa opção!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent cadastro_consulta = new Intent(dados_paciente.this, dados_consulta.class);
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
                        break;
                    case R.id.action_hist_consultas:
                        if (nome_paciente.equals("") || sexo.equals("")) {
                            Toast.makeText(getApplicationContext(), "Você precisa cadastrar um paciente para acessar essa opção!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent hist_consulta = new Intent(dados_paciente.this, hist_consulta.class);
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
                        }
                        break;
                    case R.id.action_grafico:
                        if (nome_paciente.equals("") || sexo.equals("")) {
                            Toast.makeText(getApplicationContext(), "Você precisa cadastrar um paciente para acessar essa opção!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent grafico = new Intent(dados_paciente.this, grafico.class);
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
                        }
                        break;
                }
                return false;
            }
        });
/********************************************************************************************************************************************/
/********************************************************************************************************************************************/
        rdMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdMasculino.isChecked()) {
                    rdFeminino.setChecked(false);
                    s = rdMasculino.getText().toString();
                }
            }
        });

        rdFeminino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdFeminino.isChecked()) {
                    rdMasculino.setChecked(false);
                    //editDataMenarca.setEnabled(true);
                    s = rdFeminino.getText().toString();
                }
            }
        });

        btn_teste = findViewById(R.id.btn_cad_paciente);
        btn_teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean erro = true;
                String msgErro = "";

                if (TextUtils.isEmpty(nome.getText())) {
                    msgErro += "Preencha o campo nome\n";
                    erro = false;
                }
                if (TextUtils.isEmpty(editDataNascimento.getText())) {
                    msgErro += "Preencha o campo data nascimento\n";
                    erro = false;
                }
                else{
                    String[] text_data_consulta = editDataNascimento.getText().toString().split("/");
                    //Toast.makeText(getApplicationContext(), "DIA: " + text_data_consulta[0] + "\nMÊS: " + text_data_consulta[1] + "\nANO: " + text_data_consulta[2], Toast.LENGTH_LONG).show();
                    Date data = new Date();
                    SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                    String dataFormatada = formataData.format(data);
                    String[] dataatual = dataFormatada.split("/");
                    //Toast.makeText(getApplicationContext(), "Data: " + data, Toast.LENGTH_LONG).show();
                    if((Integer.parseInt(text_data_consulta[0]) < 1) || (Integer.parseInt(text_data_consulta[0]) > 31) || (Integer.parseInt(text_data_consulta[1]) > 12) || (Integer.parseInt(text_data_consulta[1]) < 1) || (Integer.parseInt(text_data_consulta[2]) > Integer.parseInt(dataatual[2])) || (Integer.parseInt(text_data_consulta[2]) < 1950)){
                        msgErro += "Data nascimento inválida\n";
                        erro = false;
                    }

                }
                if (!rdFeminino.isChecked() && !rdMasculino.isChecked()) {
                    msgErro += "Selecione o sexo do paciente\n";
                    erro = false;
                }
                if (TextUtils.isEmpty(editResonsavel.getText())){
                    msgErro += "Preencha o campo Responsável\n";
                    erro = false;
                }
                if (erro){
                    //Toast.makeText(getApplicationContext(), "" + verificaPaciente(email.getText().toString()), Toast.LENGTH_LONG).show();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    key[0] = mDatabaseReference.child("pacientes").push().getKey();
                    nome_pacienteCad = nome.getText().toString();
                    id_medicoCad = id_medico;
                    sexoCad = s;
                    uuid_pacienteCad = key[0];
                    Cad_paciente cad = new Cad_paciente(nome.getText().toString(), editDataNascimento.getText().toString(), s, id_medico, email.getText().toString(), editResonsavel.getText().toString(), key[0]);
                    verificaPaciente(email.getText().toString(), cad);
                }
                else{
                    Toast.makeText(getApplicationContext(), "" + msgErro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**********************************************************************************************************************************************/
    public void verificaPaciente(final String emailP, final Cad_paciente pac){
        final String[] msg = {""};
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefPacientes;
        myRefPacientes = database.getReference("pacientes");
        ValueEventListener valueEventListener = myRefPacientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot respostaSnapshotPac) {
                for (DataSnapshot respostaPac : respostaSnapshotPac.getChildren()) {
                    String email = respostaPac.child("email").getValue().toString();
                    if(emailP.equals(email)){
                        msg[0] = "Paciente já existe";
                        verificaPacient = true;
                        break;
                    }
                }

                if(!verificaPacient){
                    cadastroPacientes(pac);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cadastroPacientes(final Cad_paciente pac) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("pacientes").child(key[0]).setValue(pac).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Paciente cadastrado!", Toast.LENGTH_LONG).show();
                Intent cadastro_consulta = new Intent(getApplicationContext(), dados_consulta.class);
                cadastro_consulta.putExtra("nome_paciente", nome_pacienteCad);
                cadastro_consulta.putExtra("id_medico", id_medicoCad);
                cadastro_consulta.putExtra("sexo", sexoCad);
                cadastro_consulta.putExtra("uuid_paciente", uuid_pacienteCad);
                cadastro_consulta.putExtra("data_nascimento", data_nascimento);
                cadastro_consulta.putExtra("idadePconsulta", idadePconsulta);
                cadastro_consulta.putExtra("idadeUconsulta", idadeUconsulta);
                cadastro_consulta.putExtra("qtdConsulta", qtdConsulta);
                cadastro_consulta.putExtra("idade_menarca", idade_menarca);

                startActivity(cadastro_consulta);
                finish();
//                Toast.makeText(getApplicationContext(), "Nome: " + nome_pacienteCad, Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "ID Medico: " + id_medicoCad, Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "Sexo: " + sexoCad, Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "UUID Paciente" + uuid_pacienteCad, Toast.LENGTH_LONG).show();
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro ao efetuar cadastro de paciente!!!", Toast.LENGTH_LONG).show();
                }
            });
    }


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
