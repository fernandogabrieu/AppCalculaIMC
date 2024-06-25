package br.edu.utfpr.calculaimc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.Objects;

/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity {

    private TextInputEditText etPeso;
    private TextInputEditText etAltura;
    private TextView tvResultado;
    private Button btCalcular;
    private Button btLimpar;
    private HalfGauge halfGauge;
    private TextView tvResultadoEscrito;
    private TextView tvDiferencaPesoIdeal;
    private LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResultado = findViewById(R.id.tvResultado);
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        btCalcular = findViewById(R.id.btCalcular);
        btLimpar = findViewById(R.id.btLimpar);
        halfGauge = findViewById(R.id.halfGauge);
        tvResultadoEscrito = findViewById(R.id.tvResultadoEscrito);
        tvDiferencaPesoIdeal = findViewById(R.id.tvDiferencaPesoIdeal);
        main = findViewById(R.id.main);

        btLimpar.setOnClickListener(v -> btLimparOnClick());

        // Configura as faixas do HalfGauge
        configureHalfGauge();

        // Adiciona OnEditorActionListener para capturar a tecla Enter e verificar os campos
        etPeso.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                checkInputsAndFocus();
                return true;
            }
            return false;
        });

        etAltura.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                checkInputsAndFocus();
                return true;
            }
            return false;
        });

        // Configura o botão Calcular para esconder o teclado e calcular o IMC
        btCalcular.setOnClickListener(v -> {
            if (validateInputs()) {
                hideKeyboard();
                btCalcularOnClick(v);
            }
        });
    }

    // Método para esconder o teclado
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Método para verificar se os campos estão preenchidos e mover o foco para o campo vazio, se houver
    private void checkInputsAndFocus() {
        if (Objects.requireNonNull(etPeso.getText()).toString().isEmpty()) {
            etPeso.requestFocus();
        } else if (Objects.requireNonNull(etAltura.getText()).toString().isEmpty()) {
            etAltura.requestFocus();
        } else {
            hideKeyboard();
        }
    }

    // Método para validar os campos de entrada com mensagens de erro
    private boolean validateInputs() {
        boolean valid = true;
        if (Objects.requireNonNull(etPeso.getText()).toString().isEmpty()) {
            etPeso.setError("Campo peso deve ser preenchido");
            etPeso.requestFocus();
            valid = false;
        } else if (Objects.requireNonNull(etAltura.getText()).toString().isEmpty()) {
            etAltura.setError("Campo altura deve ser preenchido");
            etAltura.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void configureHalfGauge() {
        Range range1 = new Range();
        range1.setColor(getColor(R.color.blueA700));//Color.parseColor(//"#2c5dfe"));
        range1.setFrom(16);
        range1.setTo(17);

        Range range2 = new Range();
        range2.setColor(getColor(R.color.cyanA400));//Color.parseColor("#4edafe"));
        range2.setFrom(17);
        range2.setTo(18.5);

        Range range3 = new Range();
        range3.setColor(getColor(R.color.green500));//Color.parseColor("#5ebf4b"));
        range3.setFrom(18.5);
        range3.setTo(25);

        Range range4 = new Range();
        range4.setColor(getColor(R.color.yellowA700));//Color.parseColor("#f3e141"));
        range4.setFrom(25.0);
        range4.setTo(30);

        Range range5 = new Range();
        range5.setColor(getColor(R.color.orangeA700));//Color.parseColor("#feac00"));
        range5.setFrom(30.0);
        range5.setTo(35);

        Range range6 = new Range();
        range6.setColor(getColor(R.color.deepOrangeA400));//Color.parseColor("#ff782a"));
        range6.setFrom(35.0);
        range6.setTo(40);

        Range range7 = new Range();
        range7.setColor(getColor(R.color.redA700));//Color.parseColor("#ff2d25"));
        range7.setFrom(40.0);
        range7.setTo(42.0);

        halfGauge.addRange(range1);
        halfGauge.addRange(range2);
        halfGauge.addRange(range3);
        halfGauge.addRange(range4);
        halfGauge.addRange(range5);
        halfGauge.addRange(range6);
        halfGauge.addRange(range7);

        halfGauge.setValueColor(Color.WHITE);
        halfGauge.setMaxValueTextColor(Color.WHITE);
        halfGauge.setMinValueTextColor(Color.WHITE);
        halfGauge.setMaxValue(42);
        halfGauge.setMinValue(16);
    }

    @SuppressLint("SetTextI18n")
    public void btCalcularOnClick(View v) {
        if (!validateInputs()) {
            return;
        }

        double peso = Double.parseDouble(Objects.requireNonNull(etPeso.getText()).toString());
        double altura = Double.parseDouble(Objects.requireNonNull(etAltura.getText()).toString());

        double resultado = peso / Math.pow(altura, 2); //imc = peso / (altura*altura)

        double diferencaPesoIdeal = 0;
        if (resultado < 18.5) {
            diferencaPesoIdeal = peso - 18.5 * Math.pow(altura, 2);
        } else if (resultado > 24.9) {
            diferencaPesoIdeal = peso - 24.9 * Math.pow(altura, 2);
        }

        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df2 = new DecimalFormat("0.0");
        tvResultado.setText(df2.format(resultado));

        halfGauge.setValue(Double.parseDouble(df.format(resultado).replace(',', '.')));
        if (resultado < 17) {
            tvDiferencaPesoIdeal.setText(df.format(diferencaPesoIdeal) + " Kg");
            tvResultadoEscrito.setText("Muito abaixo");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.blueA700));
            main.setBackgroundColor(getColor(R.color.blueA700));
            btCalcular.setBackgroundColor(getColor(R.color.blue900));
            btLimpar.setBackgroundColor(getColor(R.color.blue900));
        } else if (resultado < 18.5) {
            tvDiferencaPesoIdeal.setText(df.format(diferencaPesoIdeal) + " Kg");
            tvResultadoEscrito.setText("Abaixo do peso");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.cyan600));
            main.setBackgroundColor(getColor(R.color.cyan600));
            btCalcular.setBackgroundColor(getColor(R.color.cyan800));
            btLimpar.setBackgroundColor(getColor(R.color.cyan800));
        } else if (resultado < 25) {
            tvDiferencaPesoIdeal.setText("Ótimo!");
            tvResultadoEscrito.setText("Normal");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.green500));
            main.setBackgroundColor(getColor(R.color.green500));
            btCalcular.setBackgroundColor(getColor(R.color.green900));
            btLimpar.setBackgroundColor(getColor(R.color.green900));
        } else if (resultado < 30) {
            tvDiferencaPesoIdeal.setText("+" + df.format(diferencaPesoIdeal) + " Kg");
            tvResultadoEscrito.setText("Sobrepeso");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.yellow700));
            main.setBackgroundColor(getColor(R.color.Goldenrod));
            btCalcular.setBackgroundColor(getColor(R.color.DarkGoldenrod));
            btLimpar.setBackgroundColor(getColor(R.color.DarkGoldenrod));
        } else if (resultado < 35) {
            tvDiferencaPesoIdeal.setText("+" + df.format(diferencaPesoIdeal) + " Kg");
            tvResultadoEscrito.setText("Obesidade (I)");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.orangeA700));
            main.setBackgroundColor(getColor(R.color.orangeA700));//Color.parseColor("#feac00"));
            btCalcular.setBackgroundColor(getColor(R.color.deepOrange800));
            btLimpar.setBackgroundColor(getColor(R.color.deepOrange800));
        } else if (resultado < 40) {
            tvDiferencaPesoIdeal.setText("+" + df.format(diferencaPesoIdeal) + " Kg");
            tvResultadoEscrito.setText("Obesidade (II)");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.deepOrangeA400));
            main.setBackgroundColor(getColor(R.color.deepOrangeA400));//Color.parseColor("#ff782a"));
            btCalcular.setBackgroundColor(getColor(R.color.deepOrange900));
            btLimpar.setBackgroundColor(getColor(R.color.deepOrange900));
        } else if (resultado >= 40) {
            tvDiferencaPesoIdeal.setText("+" + df.format(diferencaPesoIdeal) + " Kg!");
            tvResultadoEscrito.setText("Obesidade (III)");
            tvDiferencaPesoIdeal.setTextColor(getColor(R.color.redA700));
            main.setBackgroundColor(getColor(R.color.redA700));//Color.parseColor("#ff2d25"));
            btCalcular.setBackgroundColor(getColor(R.color.red900));
            btLimpar.setBackgroundColor(getColor(R.color.red900));
        }
    }

    private void btLimparOnClick() {
        etPeso.setText("");
        etAltura.setText("");
        tvResultado.setText("");
        tvResultadoEscrito.setText("");
        tvResultadoEscrito.setTextColor(Color.BLACK);
        tvDiferencaPesoIdeal.setText("");
        main.setBackgroundColor(getColor(R.color.blue700));
        btCalcular.setBackgroundColor(getColor(R.color.DarkBlue));
        btLimpar.setBackgroundColor(getColor(R.color.DarkBlue));
        halfGauge.setValue(0.0);
        etPeso.requestFocus();
    }
}