package com.example.com.tannerapp;

import androidx.annotation.NonNull;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.UniqueLegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.Series;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import bottomnav.hitherejoe.com.tannerapp.R;


public class grafico extends AppCompatActivity {
    PointsGraphSeries<DataPoint> series;

    private DatabaseReference databaseReference;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabaseInstance;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    int valor_mama = 0;
    double mena_idade, pgInicial, pgFinal;
    String[] nascimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*********************************************************************************************************************/
        getSupportActionBar().setTitle("Endocrin App");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(grafico.this, telainicial.class);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafico);
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        final int[] teste = new int[1];
        final String[] legenda = new String[5];
        final int[] pgraficoinicial = {0};
        final int[] pgraficofinal = {0};
        final String[] sex = new String[1];
/**************************************************************************************************************/
    //graph.setLayoutParams(new AbsoluteLayout.LayoutParams(300, 200));
//        graph.setScaleX(1);
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

        nascimento = data_nascimento.split("/");
        //Toast.makeText(getApplicationContext(),"IDADE MENARCA: " + idade_menarca, Toast.LENGTH_LONG).show();
        if(sexo.equals("F") && !idade_menarca.equals("")){
            pgInicial = (Double.parseDouble(idadePconsulta) - Double.parseDouble(idade_menarca));
//            Toast.makeText(getApplicationContext(),"PONTO INICIAL: " + pgInicial, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(),"IDADE PRIMEIRA CONSULTA: " + idadePconsulta, Toast.LENGTH_LONG).show();
        }
        else {
            pgInicial = Double.parseDouble(idadePconsulta);
        }

        if(idadeUconsulta.equals("")){
            pgFinal = 15;
        }
        else{
            pgFinal = Double.parseDouble(idadeUconsulta);
        }
        //limite_final = Double.parseDouble(qtdConsulta);
        //Toast.makeText(getApplicationContext(),"QUANTIDADE DE CONSULTAWS: " + qtdConsulta, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"IDADE ÚLTIMA CONSULTA: " + idadeUconsulta, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"Data nascimento: " + data_nascimento, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"Sexo PACIENTE: " + sexo, Toast.LENGTH_LONG).show();
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**************************************************************************************************************/
        mFirebaseDatabaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabaseInstance.getReference("consulta");
        Query queryConsulta = mDatabaseReference.orderByChild("idade_atual");

        final LineGraphSeries<DataPoint> nivel_mama = new LineGraphSeries<>();
        final PointsGraphSeries<DataPoint> nivel_mama_points = new PointsGraphSeries<>();
        final LineGraphSeries<DataPoint> nivel_pelo = new LineGraphSeries<>();
        final PointsGraphSeries<DataPoint> nivel_pelo_points = new PointsGraphSeries<>();
        final LineGraphSeries<DataPoint> menarca = new LineGraphSeries<>();
        final LineGraphSeries<DataPoint> TratIni = new LineGraphSeries<>();
        final PointsGraphSeries<DataPoint> nivel_pelo_points_alerta = new PointsGraphSeries<>();
        final PointsGraphSeries<DataPoint> nivel_mama_points_alerta = new PointsGraphSeries<>();

        queryConsulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Toast.makeText(getApplicationContext(),"Nome paciente selecionado: " + nome_paciente, Toast.LENGTH_LONG).show();
                if(dataSnapshot.exists()){
                    Iterable<DataSnapshot> respostaChildren = dataSnapshot.getChildren();
                    double aux = 0;
                    for (DataSnapshot resposta : respostaChildren) {
                        String uuid = resposta.child("uuid_paciente").getValue().toString();
                        if(uuid.equals(uuid_paciente)){
                            double escala_mama = Double.parseDouble(resposta.child("escala_mama").getValue().toString());
                            double escala_pelo = Double.parseDouble(resposta.child("escala_pelo").getValue().toString());
                            double idade = Double.parseDouble(resposta.child("idade_atual").getValue().toString());
                            //limite_final = idade;
                            final String nome = resposta.child("nome_paciente").getValue().toString();
                            double peso = Double.parseDouble(resposta.child("peso").getValue().toString());
//
                            if(escala_mama != 0 && escala_pelo != 0){
                                try {
                                    if (sexo.equals("F")) {
                                        switch ((int) escala_mama) {
                                            case 1:
                                                if (idade >= 8) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 2:
                                                if (idade < 8 || idade > 13) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 3:
                                                if (idade < 10 || idade > 14) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 4:
                                                if (idade < 11 || idade > 15) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 5:
                                                if (idade < 13) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                        switch ((int) escala_pelo) {
                                            case 1:
                                                if (idade >= 9) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 2:
                                                if (idade < 9 || idade > 14) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 3:
                                                if (idade < 10 || idade > 14.5) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 4:
                                                if (idade < 11 || idade > 15) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 5:
                                                if (idade < 12) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                    } else {
                                        switch ((int) escala_mama) {
                                            case 1:
                                                if (idade > 9.5) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 2:
                                                if (idade < 9.5 || idade > 13.5) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 3:
                                                if (idade < 10.5 || idade > 15) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 4:
                                                if (idade < 11.5 || idade > 16) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            case 5:
                                                if (idade < 12.5) {
                                                    nivel_mama_points_alerta.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                else{
                                                    nivel_mama.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                    nivel_mama_points.appendData(new DataPoint(idade, escala_mama), true, 100);
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                        switch ((int) escala_pelo) {
                                            case 1:
                                                if (idade > 11) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 2:
                                                if (idade < 11 || idade > 15.5) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 3:
                                                if (idade < 11.9 || idade > 16) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 4:
                                                if (idade < 12 || idade > 16.5) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            case 5:
                                                if (idade < 13) {
                                                    nivel_pelo_points_alerta.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                else{
                                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }

//                                    nivel_pelo.appendData(new DataPoint(idade, escala_pelo), true, 100);
//                                    nivel_pelo_points.appendData(new DataPoint(idade, escala_pelo), true, 100);
                                }
                                catch (Exception e){

                                }
                            }

                            if(!resposta.child("dtIniTratamento").getValue().toString().equals("") && !resposta.child("dtFimTratamento").getValue().toString().equals("") && resposta.child("uuid_paciente").getValue().toString().equals(uuid_paciente)) {
                                String[] TratamentoIni = resposta.child("dtIniTratamento").getValue().toString().split("/");
                                String[] TratamentoFim = resposta.child("dtFimTratamento").getValue().toString().split("/");


                                int dia_ini = Integer.parseInt(TratamentoIni[0]);
                                int mes_ini = Integer.parseInt(TratamentoIni[1]);
                                int ano_ini = Integer.parseInt(TratamentoIni[2]);

                                int dia_fim = Integer.parseInt(TratamentoFim[0]);
                                int mes_fim = Integer.parseInt(TratamentoFim[1]);
                                int ano_fim = Integer.parseInt(TratamentoFim[2]);

                                int dia_nascimento = Integer.parseInt(nascimento[0]);
                                int mes_nascimento = Integer.parseInt(nascimento[1]);
                                int ano_nascimento = Integer.parseInt(nascimento[2]);


                                int diaIni, diaFim, ndiaM;
                                int mesIni, mesFim, nmesM;
                                int anoIni, anoFim, nanoM;
                                int dia, mes, ano, mesF, anoF;
                                int diferenca, diferencaF;

                                diaIni = dia_ini;  /* dia menarca                                               */
                                ndiaM = dia_nascimento;  /* dia de nascimento                                         */
                                mesIni = mes_ini;  /* mes menarca                                               */
                                nmesM = mes_nascimento;  /* mes de nascimento                                         */
                                anoIni = ano_ini;  /* ano menarca                                               */
                                nanoM = ano_nascimento;  /* ano de nascimento                                         */

                                /* PROCESSAMENTO DE  DADOS                                                */
                                diferenca =   365*anoIni + 30*mesIni + diaIni
                                        - 365*nanoM - 30*nmesM - ndiaM;

                                ano = diferenca/365;
                                diferenca = diferenca%365;

                                mes = diferenca/30;
                                diferenca = diferenca%30;

                                String idade_Ini_Tratamento = (String) (ano + "." + mes);
                                /***************************************************************************/
                                diaFim = dia_fim;  /* dia menarca                                               */
                                ndiaM = dia_nascimento;  /* dia de nascimento                                         */
                                mesFim = mes_fim;  /* mes menarca                                               */
                                nmesM = mes_nascimento;  /* mes de nascimento                                         */
                                anoFim = ano_fim;  /* ano menarca                                               */
                                nanoM = ano_nascimento;  /* ano de nascimento                                         */

                                /* PROCESSAMENTO DE  DADOS                                                */
                                diferencaF =   365*anoFim + 30*mesFim + diaFim
                                        - 365*nanoM - 30*nmesM - ndiaM;

                                anoF = diferencaF/365;
                                diferencaF = diferencaF%365;

                                mesF = diferencaF/30;
                                diferencaF = diferencaF%30;

                                String idade_Fim_Tratamento = (String) (anoF + "." + mesF);

                                //Toast.makeText(getApplicationContext(),"IDADE INÍCIO TRATAMENTO: " + Double.parseDouble(idade_Ini_Tratamento), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(),"IDADE FIM TRATAMENTO: " + Double.parseDouble(idade_Fim_Tratamento), Toast.LENGTH_LONG).show();

                                try {
                                    TratIni.appendData(new DataPoint(Double.parseDouble(idade_Ini_Tratamento), 5), true, 5);
                                    TratIni.appendData(new DataPoint(Double.parseDouble(idade_Fim_Tratamento), 5), true, 5);
                                    TratIni.appendData(new DataPoint(Double.parseDouble(idade_Fim_Tratamento), 1), true, 5);
                                    TratIni.appendData(new DataPoint(Double.parseDouble(idade_Ini_Tratamento), 1), true, 5);
                                }
                                catch (Exception e){

                                }
                                TratIni.setTitle("Tratamento");
                                TratIni.setDrawBackground(true);
                                TratIni.setBackgroundColor(Color.argb(80, 220, 220, 220));
                                TratIni.setColor(Color.argb(80, 220, 220, 220));
                                graph.addSeries(TratIni);
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Não existem dados cadastrados.", Toast.LENGTH_LONG).show();
                }


                graph.addSeries(nivel_pelo_points);
                graph.addSeries(nivel_pelo);
                graph.addSeries(nivel_mama_points);
                graph.addSeries(nivel_mama);

                graph.addSeries(nivel_pelo_points_alerta);
                graph.addSeries(nivel_mama_points_alerta);

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.parseColor("#264255"));
                paint.setStrokeWidth(3);
                paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
                nivel_mama.setDrawAsPath(true);
                nivel_mama.setCustomPaint(paint);
                nivel_mama.setAnimated(true);
                nivel_mama_points.setSize(8);
                nivel_mama_points.setColor(Color.parseColor("#264255"));
                nivel_mama.setColor(Color.parseColor("#264255"));

                nivel_pelo.setAnimated(true);
                nivel_pelo_points.setSize(12);//#254154
                nivel_pelo_points.setColor(Color.parseColor("#82b3b0"));
                nivel_pelo.setColor(Color.parseColor("#82b3b0"));
                nivel_pelo.setTitle("Pelo");

                nivel_mama_points_alerta.setSize(8);
                nivel_mama_points_alerta.setColor(Color.RED);
                nivel_pelo_points_alerta.setSize(12);//#254154
                nivel_pelo_points_alerta.setColor(Color.RED);
                nivel_mama_points_alerta.setTitle("Alerta desvio");
                nivel_pelo_points_alerta.setTitle("Alerta desvio");

                if(sexo.equals("F")){
                    nivel_mama_points.setTitle("Mama");
                    nivel_mama.setTitle("Mama");
                }
                else{
                    nivel_mama_points.setTitle("Gônada");
                    nivel_mama.setTitle("Gônada");
                }

                nivel_pelo_points.setTitle("Pelo");
                graph.setLegendRenderer(new UniqueLegendRenderer(graph));
                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.MIDDLE);
                graph.getLegendRenderer().setFixedPosition(1, 5);
                graph.getLegendRenderer().setVisible(true);
                graph.getLegendRenderer().setTextSize(15);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_grafico);
        // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_menu:
                        Intent home = new Intent(grafico.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.action_cadastro_pacientes:
                        Intent cadastro_pacientes = new Intent(grafico.this, dados_paciente.class);
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
                        Intent cadastro_consulta = new Intent(grafico.this, dados_consulta.class);
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
                        Intent hist_consulta = new Intent(grafico.this, hist_consulta.class);
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
                        break;
                    case R.id.action_grafico:

                        break;
                }
                return false;
            }
        });
/*********************************************************************************************************************************************/
/********************************************************************************************************************************************/
/*******************************************************************************************************************************************/
        mDatabaseReference = mFirebaseDatabaseInstance.getReference("consulta");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LineGraphSeries<DataPoint> menarca = new LineGraphSeries<>();

                if(dataSnapshot.exists()){
                    Iterable<DataSnapshot> respostaChildren = dataSnapshot.getChildren();

                    for (DataSnapshot resposta : respostaChildren) {
                        String nome = resposta.child("nome_paciente").getValue().toString();
                        sex[0] = sexo;
                        if (sexo.equals("F") && nome.equals(nome_paciente)){
                            double idade_menarca = 0;
                            if(!resposta.child("idade_menarca").getValue().toString().equals("")){
                                idade_menarca = Double.parseDouble(resposta.child("idade_menarca").getValue().toString());
                                mena_idade = idade_menarca;
                            }

                            if (idade_menarca != 0){
                                menarca.appendData(new DataPoint(idade_menarca, 1), true, 2);
                                menarca.appendData(new DataPoint(idade_menarca, 5), true, 2);
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Não existem dados cadastrados", Toast.LENGTH_LONG).show();
                }
                if(mena_idade != 0){
                    graph.addSeries(menarca);
                    menarca.setColor(Color.parseColor("#ff6f9c"));
                    menarca.setTitle("Menarca");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/************************************************************************************************************/
                        /******PERÍODO DE TRATAMENTO*******/
/************************************************************************************************************/
/************************************************************************************************************/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
                /****ESCALA APRESENTADA DO EIXO Y DE ACORDO COM O SEXO*****/

        if(sexo.equals("M")){
            legenda[0] = "G1";
            legenda[1] = "G2";
            legenda[2] = "G3";
            legenda[3] = "G4";
            legenda[4] = "G5";
        }
        else{
            legenda[0] = "M1";
            legenda[1] = "M2";
            legenda[2] = "M3";
            legenda[3] = "M4";
            legenda[4] = "M5";
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**************************************************************************************************************/
                    /*******CONFIGURAÇÕES DOS PONTOS E LINHAS DO GRÁFICO*********/
        //Toast.makeText(getApplicationContext(),"PONTO INICIAL GRÁFICO: " + pgInicial, Toast.LENGTH_LONG).show();

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(1);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(pgInicial);
        graph.getViewport().setMaxX(pgFinal);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setMinimalViewport(2, (pgFinal + 5), 3, 3);
        //graph.getViewport().setDrawBorder(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(15);
        graph.getSecondScale().setMinY(1);
        graph.getSecondScale().setMaxY(5);
        GridLabelRenderer renderer = graph.getGridLabelRenderer();
        renderer.setHorizontalLabelsAngle(90);
        renderer.setHorizontalAxisTitle("Idade");
        renderer.setLabelHorizontalHeight(1);
        renderer.setVerticalLabelsAlign(Paint.Align.LEFT);
        graph.setTitle("Escala de Tanner");
        //graph.computeScroll();
        //graph.getGridLabelRenderer().setLabelsSpace(2);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setVerticalLabels(legenda);

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        StaticLabelsFormatter pelo = new StaticLabelsFormatter(graph);
        pelo.setVerticalLabels(new String[] {"P1", "P2", "P3", "P4", "P5"});
        //pelo.setHorizontalLabels(eixoX);
        graph.getSecondScale().setLabelFormatter(pelo);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);




/**********************************************************************************************************************/
//

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
