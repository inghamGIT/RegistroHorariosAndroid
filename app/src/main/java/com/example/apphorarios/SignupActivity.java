package com.example.apphorarios;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphorarios.RetroFit.IMyService;
import com.example.apphorarios.RetroFit.RetroFitClient;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity {

    Button btn_register;
    EditText edt_nomEmp, edt_apeEmp, edt_username, edt_password, edt_confirm, edt_email;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String tokenFCM;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Iniciamos servicios
        Retrofit retroFitClient = RetroFitClient.getInstance();
        iMyService = retroFitClient.create(IMyService.class);

        btn_register = findViewById(R.id.btn_signup_register);
        edt_nomEmp = findViewById(R.id.edt_signup_nomEmp);
        edt_apeEmp = findViewById(R.id.edt_signup_apeEmp);
        edt_username = findViewById(R.id.edt_signup_username);
        edt_password = findViewById(R.id.edt_signup_password);
        edt_confirm = findViewById(R.id.edt_signup_confirm);
        edt_email = findViewById(R.id.edt_signup_email);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                tokenFCM = null;
            }
            else {
                tokenFCM = extras.getString("fcm");
            }
        }
        else {
            tokenFCM = (String) savedInstanceState.getSerializable("fcm");
        }
        Log.d("SIGNUP", tokenFCM);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(edt_nomEmp.getText().toString(),
                        edt_apeEmp.getText().toString(),
                        edt_username.getText().toString(),
                        edt_password.getText().toString(),
                        edt_confirm.getText().toString(),
                        edt_email.getText().toString());
            }
        });
    }

    private void registerUser(String nomEmp, String apeEmp, final String username, String pass, String confirm, String email) {

        if (!validateUser(nomEmp, apeEmp, username, pass, confirm, email)) {
            return;
        }

        compositeDisposable.add(iMyService.registerUser(nomEmp, apeEmp, username, pass, confirm, email, tokenFCM)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsontoken = new JSONObject(response);
                        getUser("Bearer " + jsontoken.getString("token"), username);
                    }
                }));
    }

    private void getUser(final String token, final String username) {

        final Intent intent = new Intent(SignupActivity.this, HomeActivity.class);

        compositeDisposable.add(iMyService.getUser(token, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        try {
                            JSONObject jsonreponse = new JSONObject(response);
                            JSONObject jsondata = jsonreponse.getJSONObject("datos");
                            intent.putExtra("token", token);
                            intent.putExtra("id", jsondata.getString("_id"));
                            startActivity(intent);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    private boolean validateUser(String nomEmp, String apeEmp, String username, String pass, String confirm, String email) {
        if(TextUtils.isEmpty(nomEmp)) {
            mostrarAlerta("Debe introducir un nombre.");
            return false;
        }
        if(nomEmp.length() < 3) {
            mostrarAlerta("El nombre debe tener al menos tres caracteres.");
            return false;
        }
        if(nomEmp.length() > 30) {
            mostrarAlerta("El nombre debe tener un máximo de treinta caracteres.");
            return false;
        }
        if(TextUtils.isEmpty(apeEmp)) {
            mostrarAlerta("Debe introducir un apellido.");
            return false;
        }
        if(apeEmp.length() < 3) {
            mostrarAlerta("Los apellidos deben tener al menos tres caracteres.");
            return false;
        }
        if(apeEmp.length() > 30) {
            mostrarAlerta("Los apellidos deben tener un máximo de treinta caracteres.");
            return false;
        }
        if(TextUtils.isEmpty(username)) {
            mostrarAlerta("Debe introducir un nombre de usuario.");
            return false;
        }
        if(username.length() < 3) {
            mostrarAlerta("El nombre de usuario debe tener al menos tres caracteres.");
            return false;
        }
        if(username.length() > 30) {
            mostrarAlerta("El nombre de usuario debe tener un máximo de treinta caracteres.");
            return false;
        }
        if(TextUtils.isEmpty(pass)) {
            mostrarAlerta("Debe introducir una contraseña.");
            return false;
        }
        if(pass.length() < 4) {
            mostrarAlerta("La contraseña debe tener al menos cuatro caracteres.");
            return false;
        }
        if(pass.length() > 30) {
            mostrarAlerta("La contraseña debe tener un máximo de treinta caracteres.");
            return false;
        }
        if(TextUtils.isEmpty(confirm)) {
            mostrarAlerta("Debe confirmar la contraseña.");
            return false;
        }
        if(!pass.equals(confirm)) {
            mostrarAlerta("La contraseña y su confirmación no coinciden.");
            return false;
        }
        if(TextUtils.isEmpty(email)) {
            mostrarAlerta("Debe introducir una dirección de correo.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(SignupActivity.this);
        alerta.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog exito = alerta.create();
        exito.setTitle("Error en la entrada de datos:");
        exito.show();
    }
}