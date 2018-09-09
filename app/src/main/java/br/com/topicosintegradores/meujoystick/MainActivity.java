package br.com.topicosintegradores.meujoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton buttonTop, buttonBottom, buttonLeft, buttonRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

    }

    private void findViewsById () {
        buttonTop = (ImageButton) findViewById(R.id.btn_top);
        buttonBottom = (ImageButton) findViewById(R.id.btn_bottom);
        buttonLeft = (ImageButton) findViewById(R.id.btn_left);
        buttonRight = (ImageButton) findViewById(R.id.btn_right);
    }

}
