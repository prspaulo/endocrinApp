package com.example.com.tannerapp;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bottomnav.hitherejoe.com.tannerapp.R;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String nome_paciente = "", uuid = "", sexo = "", s = "", data_nascimento = "", dt, menarcaPac = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefPacientes;
    /*********************/
    private Button btnNovoPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btnTeste = (Button) findViewById(R.id.teste);
/*********************************************************************************************************************/
        getSupportActionBar().setTitle("Endocrin App");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, telainicial.class);
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
        user  = firebaseAuth.getCurrentUser();

        final Spinner spinner_pacientes = (Spinner) findViewById(R.id.spPacientesPorMedico);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        final List<String> sexo_paciente = new ArrayList<String>();
        final List<String> uuid_paciente = new ArrayList<String>();
        final List<String> dtnascimento = new ArrayList<>();
        final List<String> uuid_paciente_consulta = new ArrayList<String>();
        final List<String> idade_consulta = new ArrayList<String>();
        final List<String> idade_consulta_final = new ArrayList<String>();
        final List<String> idade_menarca = new ArrayList<String>();

        spinner_pacientes.setEnabled(false);
        bottomNavigationView.setEnabled(false);

        myRefPacientes = database.getReference("pacientes");
        Query queryConsulta = myRefPacientes.orderByChild("nome");
        ValueEventListener valueEventListener = queryConsulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot respostaSnapshot) {
                Iterable<DataSnapshot> respostaChildren = respostaSnapshot.getChildren();
                final List<String> nomeConsulta = new ArrayList<String>();
                nomeConsulta.add("Clique para selecionar");
                sexo_paciente.add("");
                uuid_paciente.add("");
                dtnascimento.add("");

                for (DataSnapshot resposta : respostaSnapshot.getChildren()) {
                    if(resposta.exists()){
                        if(resposta.child("id_medico").getValue().toString().equals(user.getUid())){
                            String consultaName = resposta.child("nome").getValue().toString();
                            String uuid = resposta.child("uuid").getValue().toString();
                            data_nascimento = resposta.child("data_nascimento").getValue().toString();
                            sexo = resposta.child("sexo").getValue().toString();
                            nomeConsulta.add(consultaName);
                            sexo_paciente.add(sexo);
                            uuid_paciente.add(uuid);
                            dtnascimento.add(data_nascimento);
                        }
                    }
                }
                if(nomeConsulta.size() != 0){
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_pacientes.setAdapter(arrayAdapter);
                    spinner_pacientes.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        myRefPacientes = database.getReference("consulta");
        Query query = myRefPacientes.orderByChild("idade_atual");
        ValueEventListener valueEventListenerCon = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot respostaSnapshot) {
                for (DataSnapshot resposta : respostaSnapshot.getChildren()) {
                    if(resposta.exists()){
                        String uuid = resposta.child("uuid_paciente").getValue().toString();
                        String idade = resposta.child("idade_atual").getValue().toString();
                        String menarca = resposta.child("idade_menarca").getValue().toString();
                        uuid_paciente_consulta.add(uuid);
                        idade_consulta.add(idade);
                        idade_menarca.add(menarca);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinner_pacientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    nome_paciente = adapterView.getItemAtPosition(i).toString();

                    if(!nome_paciente.equals("") && !nome_paciente.equals("Clique para selecionar")){
                        s = sexo_paciente.get(i);
                        uuid = uuid_paciente.get(i);
                        dt = dtnascimento.get(i);

                        for(int i2=0; i2 < uuid_paciente_consulta.size();i2++){
                            if(uuid_paciente_consulta.get(i2).equals(uuid)) {
                                idade_consulta_final.add(idade_consulta.get(i2));
                                if(!idade_menarca.get(i2).equals("")){
                                    menarcaPac = idade_menarca.get(i2);
                                }
                            }
                        }
                        //Toast.makeText(getApplicationContext(),"IDADE PRIMEIRA CONSULTA: " + idade_consulta_final.get(0), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),"IDADE ÚLTIMA CONSULTA: " + idade_consulta_final.get(idade_consulta_final.size()-1), Toast.LENGTH_LONG).show();
                        if(idade_consulta_final.size()!= 0 && idade_consulta_final.size() !=0){
                            Intent hist_consulta = new Intent(MainActivity.this, hist_consulta.class);
                            hist_consulta.putExtra("nome_paciente", nome_paciente);
                            hist_consulta.putExtra("id_medico", user.getUid());
                            hist_consulta.putExtra("sexo", s);
                            hist_consulta.putExtra("uuid_paciente", uuid);
                            hist_consulta.putExtra("data_nascimento", dt);
                            hist_consulta.putExtra("idadePconsulta", idade_consulta_final.get(0));
                            hist_consulta.putExtra("idadeUconsulta", idade_consulta_final.get(idade_consulta_final.size()-1));
                            hist_consulta.putExtra("qtdConsulta", idade_consulta_final.size());
                            hist_consulta.putExtra("idade_menarca", menarcaPac);
                            startActivity(hist_consulta);
                        }
                        else{
                            int qtdConsulta = idade_consulta_final.size();
                            Intent hist_consulta = new Intent(MainActivity.this, hist_consulta.class);
                            hist_consulta.putExtra("nome_paciente", nome_paciente);
                            hist_consulta.putExtra("id_medico", user.getUid());
                            hist_consulta.putExtra("sexo", s);
                            hist_consulta.putExtra("uuid_paciente", uuid);
                            hist_consulta.putExtra("data_nascimento", dt);
                            hist_consulta.putExtra("idadePconsulta", "0.0");
                            hist_consulta.putExtra("idadeUconsulta", "0.0");
                            hist_consulta.putExtra("qtdConsulta", qtdConsulta);
                            hist_consulta.putExtra("idade_menarca", menarcaPac);
                            startActivity(hist_consulta);
                        }

                        //Toast.makeText(getApplicationContext(),"UUID PACIENTE: " + uuid + "\nSEXO: " + s + "\nNOME: " + nome_paciente, Toast.LENGTH_LONG).show();
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNovoPaciente = findViewById(R.id.novo_paciente);

        /*TESTE CHAMA RECYCLE VIEW*/
        btnNovoPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idade_consulta_final.size()!= 0 && idade_consulta_final.size() !=0){
                    Intent cadastro_paciente = new Intent(MainActivity.this, dados_paciente.class);
                    cadastro_paciente.putExtra("nome_paciente", nome_paciente);
                    cadastro_paciente.putExtra("id_medico", user.getUid());
                    cadastro_paciente.putExtra("sexo", s);
                    cadastro_paciente.putExtra("uuid_paciente", uuid);
                    cadastro_paciente.putExtra("data_nascimento", dt);
                    cadastro_paciente.putExtra("idadePconsulta", idade_consulta_final.get(0));
                    cadastro_paciente.putExtra("idadeUconsulta", idade_consulta_final.get(idade_consulta_final.size()-1));
                    cadastro_paciente.putExtra("qtdConsulta", idade_consulta_final.size());
                    cadastro_paciente.putExtra("idade_menarca", menarcaPac);
                    startActivity(cadastro_paciente);
                }
                else{
                    int qtdConsulta = idade_consulta_final.size();
                    Intent cadastro_paciente = new Intent(MainActivity.this, dados_paciente.class);
                    cadastro_paciente.putExtra("nome_paciente", nome_paciente);
                    cadastro_paciente.putExtra("id_medico", user.getUid());
                    cadastro_paciente.putExtra("sexo", s);
                    cadastro_paciente.putExtra("uuid_paciente", uuid);
                    cadastro_paciente.putExtra("data_nascimento", dt);
                    cadastro_paciente.putExtra("idadePconsulta", "0.0");
                    cadastro_paciente.putExtra("idadeUconsulta", "0.0");
                    cadastro_paciente.putExtra("qtdConsulta", qtdConsulta);
                    cadastro_paciente.putExtra("idade_menarca", menarcaPac);
                    startActivity(cadastro_paciente);
                }
            }
        });
/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/

        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_menu:

                                break;
                            case R.id.action_cadastro_pacientes:
                                Intent cadastro_paciente = new Intent(MainActivity.this, dados_paciente.class);
                                cadastro_paciente.putExtra("nome_paciente", nome_paciente);
                                cadastro_paciente.putExtra("id_medico", user.getUid());
                                cadastro_paciente.putExtra("sexo", s);
                                cadastro_paciente.putExtra("uuid_paciente", uuid);
                                startActivity(cadastro_paciente);
                                break;
                            case R.id.action_cadastro_consulta:
                                if(nome_paciente.equals("") || s.equals("")){
                                    Toast.makeText(getApplicationContext(),"Você precisa cadastrar um paciente para acessar essa opção!", Toast.LENGTH_LONG).show();
                                }else{
                                    Intent cadastro_consulta = new Intent(MainActivity.this, dados_consulta.class);
                                    cadastro_consulta.putExtra("nome_paciente", nome_paciente);
                                    cadastro_consulta.putExtra("id_medico", user.getUid());
                                    cadastro_consulta.putExtra("sexo", s);
                                    cadastro_consulta.putExtra("uuid_paciente", uuid);
                                    startActivity(cadastro_consulta);
                                }
                                break;
                            case R.id.action_hist_consultas:
                                if(nome_paciente.equals("") || s.equals("")){
                                    Toast.makeText(getApplicationContext(),"Você precisa cadastrar um paciente para acessar essa opção!", Toast.LENGTH_LONG).show();
                                }else{
                                    Intent hist_consulta = new Intent(MainActivity.this, hist_consulta.class);
                                    hist_consulta.putExtra("nome_paciente", nome_paciente);
                                    hist_consulta.putExtra("id_medico", user.getUid());
                                    hist_consulta.putExtra("sexo", s);
                                    hist_consulta.putExtra("uuid_paciente", uuid);
                                    startActivity(hist_consulta);
                                }
                                break;
                            case R.id.action_grafico:

                                if(nome_paciente.equals("") || s.equals("")){
                                    Toast.makeText(getApplicationContext(),"Você precisa cadastrar um paciente para acessar essa opção!", Toast.LENGTH_LONG).show();
                                }else{
                                    Intent grafico = new Intent(MainActivity.this, grafico.class);
                                    grafico.putExtra("nome_paciente", nome_paciente);
                                    grafico.putExtra("id_medico", user.getUid());
                                    grafico.putExtra("sexo", s);
                                    grafico.putExtra("uuid_paciente", uuid);
                                    startActivity(grafico);
                                }
                                //startActivity(grafico);
                                //Toast.makeText(getApplicationContext(),"Nome paciente selecionado: " + nome_paciente, Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
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
