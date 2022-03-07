package com.example.com.tannerapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bottomnav.hitherejoe.com.tannerapp.R;

public class telainicial extends CommonActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Medico medico;

    private TextView cadastrar;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = getFirebaseAuthResultHandler();

        inicializarViews();

        btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(this);
    }

    protected void inicializarViews() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        cadastrar = (TextView) findViewById(R.id.login);
    }

    protected void inicializarUsuario() {
        medico = new Medico();
        medico.setEmail(email.getText().toString());
        medico.setPassword(password.getText().toString());
    }

    @Override
    public void onClick(View v) {

        inicializarUsuario();

        int id = v.getId();
        if (id == R.id.login) {

            String EMAIL = email.getText().toString();
            String SENHA = password.getText().toString();

            boolean ok = true;

            if (EMAIL.isEmpty()) {
                email.setError("E-mail não informado!");

                ok = false;
            }

            if (SENHA.isEmpty()) {
                password.setError("Por favor digite uma senha!");

                ok = false;
            }

            if (ok) {
                btnLogin.setEnabled(false);
                cadastrar.setEnabled(false);
                progressBar.setFocusable(true);

                openProgressBar();
                verifyLogin();
            } else {
                closeProgressBar();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
    }

    private void verifyLogged() {

        if (firebaseAuth.getCurrentUser() != null) {
            chamarMainActivity();
        } else {
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

                if (userFirebase == null) {
                    return;
                }

                if (medico.getId() == null && isNameOk(medico, userFirebase)) {

                    medico.setId(userFirebase.getUid());
                    medico.setNameIfNull(userFirebase.getDisplayName());
                    medico.setEmailIfNull(userFirebase.getEmail());
                    medico.saveDB();
                }
            }
        };
        return (callback);
    }

    private void verifyLogin() {
        firebaseAuth.signInWithEmailAndPassword(
                medico.getEmail(),
                medico.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            closeProgressBar();
                            btnLogin.setEnabled(true);
                            cadastrar.setEnabled(true);

                            showSnackbar("Usuário ou senha inválidos!!!");

                            return;
                        }
                        else{
                            chamarMainActivity();
                        }
                    }
                });
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showToast(connectionResult.getErrorMessage());
    }

    private boolean isNameOk(Medico medico, FirebaseUser firebaseUser) {
        return (medico.getName() != null || firebaseUser.getDisplayName() != null);
    }

    private void chamarMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void chamarCadastro(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void chamarReset(View view) {
        Intent intent = new Intent(this, resetarSenha.class);
        startActivity(intent);
    }
}
