package br.com.topicosintegradores.meujoystick;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button btnBluetooth;
    private ImageButton buttonTop, buttonBottom, buttonLeft, buttonRight;
    private ImageButton buttonTopRight, buttonTopLeft, buttonBottomRight, buttonBottomLeft;
    public BluetoothAdapter myBluetoothAdapter = null;
    public BluetoothDevice myDevice = null;
    public BluetoothSocket mySocket = null;
    public static final int SOLICITA_ATIVACAO = 1;
    public static final int SOLICITA_CONEXAO = 2;

    ConnectedThread CThread;

    public String dadosParaEnvio = "S";

    boolean conexao = false;
    private static String MAC = null;

    UUID my_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Ocultando a status bar da tela
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        findViewsById();

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(myBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Seu dispositivo não possui Bluetooth", Toast.LENGTH_LONG).show();
        }else if(!myBluetoothAdapter.isEnabled()){
            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBt, SOLICITA_ATIVACAO);
        }

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(conexao){
                    //desconectar
                    try{
                        mySocket.close();
                        conexao = false;
                        Toast.makeText(getApplicationContext(), "O Bluetooth foi desconectado", Toast.LENGTH_LONG).show();
                    }catch (IOException erro){
                        Toast.makeText(getApplicationContext(), "Ocorreu um Erro" + erro, Toast.LENGTH_LONG).show();
                    }
                }else{
                    //conectar
                    Intent abreLista = new Intent(MainActivity.this, ListaDispositivos.class);
                    startActivityForResult(abreLista,SOLICITA_CONEXAO);
                }
            }
        });

        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conexao){
                    CThread.enviar("F");
                }else{
                    Toast.makeText(getApplicationContext(), "Ocorreu um Erro", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void findViewsById () {
        buttonTop = (ImageButton) findViewById(R.id.btn_top);
        buttonBottom = (ImageButton) findViewById(R.id.btn_bottom);
        buttonLeft = (ImageButton) findViewById(R.id.btn_left);
        buttonRight = (ImageButton) findViewById(R.id.btn_right);
        buttonTopRight = (ImageButton) findViewById(R.id.btn_top_right);
        buttonTopLeft = (ImageButton) findViewById(R.id.btn_top_left);
        buttonTopRight = (ImageButton) findViewById(R.id.btn_top_right);
        buttonBottomRight = (ImageButton) findViewById(R.id.btn_bottom_right);
        buttonBottomLeft = (ImageButton) findViewById(R.id.btn_bottom_left);
        btnBluetooth = (Button) findViewById(R.id.btn_bluetooth);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SOLICITA_ATIVACAO:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "O Bluetooth foi ativado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "O Bluetooth não foi ativado", Toast.LENGTH_LONG).show();
                    finish();
                }

            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    myDevice = myBluetoothAdapter.getRemoteDevice(MAC);

                    try{
                        mySocket = myDevice.createRfcommSocketToServiceRecord(my_UUID);

                        mySocket.connect();

                        conexao = true;

                        CThread = new ConnectedThread(mySocket);
                        CThread.start();

                        Toast.makeText(getApplicationContext(), "Conectado com: " + MAC, Toast.LENGTH_LONG).show();
                    }catch (IOException erro){

                        conexao = false;
                        Toast.makeText(getApplicationContext(), "Erro ao conectar" + erro + MAC, Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Falha ao obter o MAC", Toast.LENGTH_LONG).show();
                }
        }

    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            /*
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                      //      .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }*/
        }

        public void enviar(String dadosEnviar) {
            byte[] msgBuffer = dadosEnviar.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mySocket.close();
            } catch (IOException e) { }
        }
    }
}
