package com.example.com.tannerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import bottomnav.hitherejoe.com.tannerapp.R;

public class atualiza_consulta extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayAdapter<String> dataMama, dataPelo;
    int nivel_mama = 0;
    int nivel_pelo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_consulta);


        TextView txtEditTanner = (TextView) findViewById(R.id.txtEscalaTannerAtuali);
        final Spinner spinnerMama = (Spinner) findViewById(R.id.spinnerMamaAtuali);
        final Spinner spinnerPelo = (Spinner) findViewById(R.id.spinnerPeloAtuali);
        final CheckBox chEscTanner = (CheckBox) findViewById(R.id.chEscTannerAtuali);
        // Spinner click listener
        spinnerMama.setOnItemSelectedListener(this);
        spinnerPelo.setOnItemSelectedListener(this);
        //Spinner Drop down elements MAMA
        final List<String>[] escTanner = new List[]{new ArrayList<String>()};
        //if (sexoAtuali.equals("F")) {
            txtEditTanner.setText("Mama");
            escTanner[0].add("");
            escTanner[0].add("M1");
            escTanner[0].add("M2");
            escTanner[0].add("M3");
            escTanner[0].add("M4");
            escTanner[0].add("M5");
        //} else {
//            txtEditTanner.setText("Gônada");
//            escTanner[0].add("");
//            escTanner[0].add("G1");
//            escTanner[0].add("G2");
//            escTanner[0].add("G3");
//            escTanner[0].add("G4");
//            escTanner[0].add("G5");
        //}

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

        spinnerMama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerPelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    @Override
    public void onItemSelected(AdapterView<?> Adapter, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}