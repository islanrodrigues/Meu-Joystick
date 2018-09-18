package br.com.topicosintegradores.meujoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton buttonTop, buttonBottom, buttonLeft, buttonRight;
    private ImageButton buttonTopRight, buttonTopLeft, buttonBottomRight, buttonBottomLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Ocultando a status bar da tela
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        findViewsById();

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
    }

}
