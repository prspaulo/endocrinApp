package com.example.com.tannerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import bottomnav.hitherejoe.com.tannerapp.R;

public class RegistrationActivity extends CommonActivity implements DatabaseReference.CompletionListener, View.OnClickListener {

    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Medico medico;
    private EditText name;
    private Button FabCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cadastro");

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    return;
                }
                //medico.setId(firebaseUser.getUid());
                //medico.saveDB(RegistrationActivity.this);
            }
        };

        inicializarViews();

        FabCadastrar = (Button) findViewById(R.id.register);
        FabCadastrar.setOnClickListener(this);
    }


//    private void sendVerificationEmail()
//    {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        user.sendEmailVerification()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // email sent
//
//
//                            // after email is sent just logout the user and finish this activity
//                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(RegistrationActivity.this, telainicial.class));
//                            finish();
//                        }
//                        else
//                        {
//                            // email not sent, so display message and restart the activity or do whatever you wish to do
//
//                            //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//
//                        }
//                    }
//                });
//    }

    protected void inicializarViews() {
        name = (EditText) findViewById(R.id.nome);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    protected void inicializarUsuario() {
        medico = new Medico();
        medico.setName(name.getText().toString());
        medico.setEmail(email.getText().toString());
        medico.setPassword(password.getText().toString());
    }

    @Override
    public void onClick(View v) {
        inicializarUsuario();

        String NOME = name.getText().toString();
        String EMAIL = email.getText().toString();
        String SENHA = password.getText().toString();

        boolean ok = true;

        if (NOME.isEmpty()) {
            name.setError("O campo nome não pode ser vazio");
            ok = false;
        }

        if (EMAIL.isEmpty()) {
            email.setError("O campo email não pode ser vazio");
            ok = false;
        }

        if (SENHA.isEmpty()) {
            password.setError("Por favor, informe uma senha!");
            ok = false;
        }

        if (ok) {
            FabCadastrar.setEnabled(false);
            progressBar.setFocusable(true);

            openProgressBar();
            salvarUsuario();
        } else {
            closeProgressBar();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void salvarUsuario() {

        mAuth.createUserWithEmailAndPassword(medico.getEmail(), medico.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    closeProgressBar();
                    showSnackbar("Erro ao efetuar cadastro!!!");
                }
                else{
                    mAuth.signOut();
                    closeProgressBar();
                    AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                    alertDialogBuilder.setTitle("Endocrin App");
                    alertDialogBuilder
                            .setMessage("Conta criada com sucesso!")
                            .setCancelable(false)
                            .setPositiveButton("Voltar",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //showSnackbar(e.getMessage());
                if(e.getMessage().endsWith("]")){
                    showSnackbar("Senha deve possuir no mínimo 6 caracteres!!!");
                }
                else if(e.getMessage().endsWith("t.")){
                    showSnackbar("O email informado já está cadastrado!!!");
                }
                FabCadastrar.setEnabled(true);
            }
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
    }

    @Override
    void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}