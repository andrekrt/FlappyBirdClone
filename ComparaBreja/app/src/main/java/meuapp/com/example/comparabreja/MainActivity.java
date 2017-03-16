package meuapp.com.example.comparabreja;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ml1;
    private EditText preco1;
    private EditText ml2;
    private EditText preco2;
    private Button verificar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            ml1 = (EditText)findViewById(R.id.ml1);
            preco1=(EditText)findViewById(R.id.preco1);
            ml2=(EditText) findViewById(R.id.ml2);
            preco2=(EditText) findViewById(R.id.preco2);
            verificar=(Button) findViewById(R.id.verificar);

        verificar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String msg = determinaResultado();
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

    private String determinaResultado() {
        String resultado="";
        String campo1ml=ml1.getText().toString();
        String campo1preco=preco1.getText().toString();
        String campo2ml=ml2.getText().toString();
        String campo2preco=preco2.getText().toString();
        Double valor1ml=Double.parseDouble(campo1ml);
        Double valor1preco=Double.parseDouble(campo1preco);
        Double valor2ml=Double.parseDouble(campo2ml);
        Double valor2preco=Double.parseDouble(campo2preco);

        Double divisao1= valor1preco/valor1ml;
        Double divisao2=valor2preco/valor1ml;
        if (divisao1<divisao2) {
            resultado = "A primeira opção é melhor";
        }if (divisao2<divisao1){
            resultado="A segunda opção é melhor";
        }
        return resultado;
    }
}
