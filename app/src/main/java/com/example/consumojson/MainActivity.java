package com.example.consumojson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private static String URL = "https://api.androidhive.info/contacts/";

    private ListAdapter adapter;
    private ListView lvAgenda;
    private List<HashMap<String, String>> itensList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvAgenda = findViewById(R.id.lv_agenda);

        new obterDados().execute();
    }

    private class obterDados extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Downloading: arquivo JSON", Toast.LENGTH_LONG).show();
        }//method

        @Override
        protected Void doInBackground(Void... voids) {

            Conexao conexao = new Conexao();
            InputStream inputStream = conexao.obterRespostaHTTP(URL);
            Auxilia auxilia = new Auxilia();
            String textoJSON = auxilia.converter(inputStream);

            itensList = new ArrayList<>();

            if (textoJSON != null) {
                try {
                    JSONObject jsonObject = new JSONObject(textoJSON);
                    JSONArray contatos = jsonObject.getJSONArray("contacts");
                    for (int i = 0; i < contatos.length(); i++) {

                        JSONObject contato = contatos.getJSONObject(i);
                        String id = contato.getString("id");
                        String nome = contato.getString("name");
                        String email = contato.getString("email");
                        String end = contato.getString("address");
                        String sexo = contato.getString("gender");

                        JSONObject fone = contato.getJSONObject("phone");
                        String movel = fone.getString("mobile");
                        String casa = fone.getString("home");
                        String escritorio = fone.getString("office");

                        HashMap<String, String> cont = new HashMap<>();
                        cont.put("id", id);
                        cont.put("src", "@drawable/ic_launcher_background");
                        cont.put("nome", nome);
                        cont.put("email", email);
                        cont.put("endereco", end);
                        cont.put("sexo", sexo);
                        cont.put("movel", movel + " [celular]");
                        cont.put("casa", casa + " [casa]");
                        cont.put("escritorio", escritorio + " [escritório]");

                        itensList.add(cont);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "JSON erro: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "JSON erro: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            adapter = new SimpleAdapter(MainActivity.this,
                    itensList,
                    R.layout.lista_item,
                    new String[]{
                            "id",
                            "src",
                            "nome",
                            "email",
                            "endereco",
                            "movel",
                            "casa",
                            "escritorio"
                    },
                    new int[]{
                            R.id.tv_id,
                            R.id.iv_id,
                            R.id.tv_nome,
                            R.id.tv_email,
                            R.id.tv_endereco,
                            R.id.tv_tel_movel,
                            R.id.tv_tel_casa,
                            R.id.tv_tel_escritorio
                    });

            lvAgenda.setAdapter(adapter);

        }//method
    }//innerclass
}//class



