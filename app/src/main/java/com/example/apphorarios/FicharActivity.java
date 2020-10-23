package com.example.apphorarios;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphorarios.RetroFit.IMyService;
import com.example.apphorarios.RetroFit.RetroFitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FicharActivity extends AppCompatActivity {

    String id, token;
    EditText edt_horaEntrada, edt_horaSalida, edt_descripcion;
    Button btn_registrar;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichar);

        //Iniciamos servicios
        Retrofit retroFitClient = RetroFitClient.getInstance();
        iMyService = retroFitClient.create(IMyService.class);

        edt_horaEntrada = findViewById(R.id.edt_fichar_horaEntrada);
        edt_horaSalida = findViewById(R.id.edt_fichar_horaSalida);
        edt_descripcion = findViewById(R.id.edt_fichar_descripcion);
        btn_registrar = findViewById(R.id.btn_fichar_registrar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                token = null;
                id = null;
            }
            else {
                token = extras.getString("token");
                id = extras.getString("id");
            }
        }
        else {
            token = (String) savedInstanceState.getSerializable("token");
            id = (String) savedInstanceState.getSerializable("id");
        }

        getByDate();
    }

    private void getByDate() {

        compositeDisposable.add(iMyService.getByDate(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonreponse = new JSONObject(response);
                        final JSONArray data = jsonreponse.getJSONArray("datos");

                        if (data.length() == 0) {
                            btn_registrar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    registerData(edt_horaEntrada.getText().toString(),
                                            edt_horaSalida.getText().toString(),
                                            edt_descripcion.getText().toString());
                                }
                            });
                        }
                        else {
                            edt_horaEntrada.setText(data.getJSONObject(0).getString("horaEntrada"));
                            edt_horaSalida.setText(data.getJSONObject(0).getString("horaSalida"));
                            edt_descripcion.setText(data.getJSONObject(0).getString("descripcion"));

                            btn_registrar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        updateData(edt_horaEntrada.getText().toString(),
                                                edt_horaSalida.getText().toString(),
                                                edt_descripcion.getText().toString(),
                                                data.getJSONObject(0).getString("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }));
    }

    private void registerData(String horaEntrada, String horaSalida, String descripcion) {
        if (!validateData(horaEntrada, horaSalida, descripcion)) {
            return;
        }

        compositeDisposable.add(iMyService.registerData(token, horaEntrada, horaSalida, id, descripcion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        mostrarAlerta("Registro guardado con éxito.");

                    }
                }));
    }

    private void updateData(String horaEntrada, String horaSalida, String descripcion, String registroId) {
        if (!validateData(horaEntrada, horaSalida, descripcion)) {
            return;
        }

        compositeDisposable.add(iMyService.updateData(token, registroId, horaEntrada, horaSalida, id, descripcion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        mostrarAlerta("Registro actualizado con éxito.");
                    }
                }));
    }

    private boolean validateData(String horaEntrada, String horaSalida, String descripcion) {
        //TODO VALIDATE FIELDS
        return true;
    }

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(FicharActivity.this);
        alerta.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FicharActivity.this, HomeActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        AlertDialog exito = alerta.create();
        exito.show();
    }
}