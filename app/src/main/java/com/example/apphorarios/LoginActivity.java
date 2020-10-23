package com.example.apphorarios;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphorarios.RetroFit.IMyService;
import com.example.apphorarios.RetroFit.RetroFitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {

    Button btn_signup, btn_login;
    EditText edt_username, edt_password;
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
        setContentView(R.layout.activity_login);

        //Iniciamos servicios
        Retrofit retroFitClient = RetroFitClient.getInstance();
        iMyService = retroFitClient.create(IMyService.class);

        btn_signup = findViewById(R.id.btn_login_signup);
        btn_login = findViewById(R.id.btn_login_login);
        edt_username = findViewById(R.id.edt_login_username);
        edt_password = findViewById(R.id.edt_login_password);

        //Recuperamos el token de FB
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    private static final String TAG = "FCM";

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        tokenFCM = task.getResult();
                        Log.d("FCM", tokenFCM);

                        btn_signup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                intent.putExtra("fcm", tokenFCM);
                                startActivity(intent);
                            }
                        });

                        btn_login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginUser(edt_username.getText().toString(),
                                        edt_password.getText().toString());
                            }
                        });
                    }
                });
    }

    private void loginUser(final String username, String pass) {
        if(TextUtils.isEmpty(username)) {
            mostrarAlerta("Debes introducir un nombre de usuario");
            return;
        }
        if(TextUtils.isEmpty(pass)) {
            mostrarAlerta("Debes introducir una contraseña");
            return;
        }

        compositeDisposable.add(iMyService.loginUser(username, pass)
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

        final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

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

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
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

    private void getFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    private static final String TAG = "FCM";

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        tokenFCM = task.getResult();
                    }
                });
    }
}