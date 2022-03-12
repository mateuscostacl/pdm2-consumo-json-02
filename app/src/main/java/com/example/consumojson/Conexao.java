package com.example.consumojson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Conexao {

    public InputStream obterRespostaHTTP(String endereco){

        try {
            URL url = new URL(endereco);

            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(conexao.getInputStream());

            return inputStream;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }//method
}