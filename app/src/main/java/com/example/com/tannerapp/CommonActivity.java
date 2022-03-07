package com.example.com.tannerapp;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;


abstract public class CommonActivity extends AppCompatActivity {

    protected EditText email;
    protected EditText password;
    protected ProgressBar progressBar;


    protected void showSnackbar( String message ){
        Snackbar.make(progressBar,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    protected void showToast( String message ){
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    protected void openProgressBar(){
        progressBar.setVisibility( View.VISIBLE );
    }

    protected void closeProgressBar(){
        progressBar.setVisibility( View.GONE );
    }

    abstract protected void inicializarViews();

    abstract protected void inicializarUsuario();

    abstract void onConnectionFailed(ConnectionResult connectionResult);
}