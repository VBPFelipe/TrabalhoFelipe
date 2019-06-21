package com.vbpfelipe.SalaryCalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final double ALIQUOTA_INSS_PARAMETRO_1 = 1751.81;
    private static final double ALIQUOTA_INSS_PARAMETRO_2 = 2919.72;

    private static final double BASE_INSS_PARAMETRO = 5839.45;

    private static final double BASE_IRPF_PARAMETRO = 189.59;

    private static final double ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_1 = 1903.98;
    private static final double ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_2 = 2826.05;
    private static final double ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_3 = 3751.05;
    private static final double ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_4 = 4664.08;

    private static final double ALIQUOTA_IRPF_PARAMETRO_1 = 0.00;
    private static final double ALIQUOTA_IRPF_PARAMETRO_2 = 0.075;
    private static final double ALIQUOTA_IRPF_PARAMETRO_3 = 0.15;
    private static final double ALIQUOTA_IRPF_PARAMETRO_4 = 0.225;
    private static final double ALIQUOTA_IRPF_PARAMETRO_5 = 0.275;

    private static final double DEDUCAO_IRPF_PARAMETRO_1 = 0.00;
    private static final double DEDUCAO_IRPF_PARAMETRO_2 = 142.80;
    private static final double DEDUCAO_IRPF_PARAMETRO_3 = 354.80;
    private static final double DEDUCAO_IRPF_PARAMETRO_4 = 636.13;
    private static final double DEDUCAO_IRPF_PARAMETRO_5 = 869.36;

    TextView txtSalarioBruto, txtNDependentes;
    EditText edtSalarioBruto, edtNDependentes;

    Button btnCalcular;

    TextView txtAliquotaINSS, txtAliqINSSVal, txtBaseINSS, txtBaseINSSVal,
            txtValorINSS, txtValorINSSVal, txtBaseIRPF, txtBaseIRPFVal,
            txtAliquotaIRPF, txtAliquotaIRPFVal, txtDeducaoIRPF, txtDeducaoIRPFVal,
            txtValorIRPF, txtValorIRPFVal;

    TextView txtSalLiq, txtSalLiqVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParameters();
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    calculoSalario();
                    Toast.makeText(getApplicationContext(),"Calculando...", Toast.LENGTH_LONG).
                            show();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).
                            show();
                }
            }
        });
    }


    /**
     * Inicializar os parâmetros
     */

    private void initParameters(){
        txtSalarioBruto = (TextView) findViewById(R.id.txtSalarioBruto);
        edtSalarioBruto = (EditText) findViewById(R.id.edtSalarioBruto);
        txtNDependentes = (TextView) findViewById(R.id.txtNDependentes);
        edtNDependentes = (EditText) findViewById(R.id.edtNDependentes);

        txtAliquotaINSS = (TextView) findViewById(R.id.txtAliquotaINSS);
        txtAliqINSSVal = (TextView) findViewById(R.id.txtAliqINSSVal);
        txtBaseINSS = (TextView) findViewById(R.id.txtBaseINSS);
        txtBaseINSSVal = (TextView) findViewById(R.id.txtBaseINSSVal);

        txtValorINSS = (TextView) findViewById(R.id.txtValorINSS);
        txtValorINSSVal = (TextView) findViewById(R.id.txtValorINSSVal);
        txtBaseIRPF = (TextView) findViewById(R.id.txtBaseIRPF);
        txtBaseIRPFVal = (TextView) findViewById(R.id.txtBaseIRPFVal);

        txtAliquotaIRPF = (TextView) findViewById(R.id.txtAliquotaIRPF);
        txtAliquotaIRPFVal = (TextView) findViewById(R.id.txtAliquotaIRPFVal);
        txtDeducaoIRPF = (TextView) findViewById(R.id.txtDeducaoIRPF);
        txtDeducaoIRPFVal = (TextView) findViewById(R.id.txtDeducaoIRPFVal);

        txtValorIRPF = (TextView) findViewById(R.id.txtValorIRPF);
        txtValorIRPFVal = (TextView) findViewById(R.id.txtValorIRPFVal);

        txtSalLiq = (TextView) findViewById(R.id.txtSalLiq);
        txtSalLiqVal = (TextView) findViewById(R.id.txtSalLiqVal);
    }

    private void calculoSalario(){

        double valorSalarioBruto = Double.parseDouble(edtSalarioBruto.getText().toString());
        double dependentes = Double.parseDouble(edtNDependentes.getText().toString());

        double aliquotaINSS = calculoAliquotaINSS(valorSalarioBruto);

        double baseINSS = calculoBaseINSS(valorSalarioBruto);

        double valorINSS = aliquotaINSS * baseINSS;

        double baseIRPF = valorSalarioBruto - valorINSS - (dependentes * BASE_IRPF_PARAMETRO);

        double aliquotaIRPF = calculoAliquotaIRPF(baseIRPF);
        double deducaoIRPF = calculoDeducaoIRPF(baseIRPF);

        double valorIRPF = (baseIRPF * aliquotaIRPF) - deducaoIRPF;

        double salarioLiquido = valorSalarioBruto - valorINSS - valorIRPF;

        txtAliqINSSVal.setText( (aliquotaINSS * 100.00) + "%");
        txtBaseINSSVal.setText(String.format("%.02f", baseINSS));
        txtValorINSSVal.setText(String.format("%.02f", valorINSS));
        txtBaseIRPFVal.setText(String.format("%.02f", baseIRPF));
        txtAliquotaIRPFVal.setText( (aliquotaIRPF * 100.00) + "%");
        txtDeducaoIRPFVal.setText(String.format("%.02f", deducaoIRPF));
        txtValorIRPFVal.setText(String.format("%.02f", valorIRPF));
        txtSalLiqVal.setText(String.format("%.02f", salarioLiquido));
    }

    private double calculoAliquotaINSS(double valorSalarioBruto){

        if(valorSalarioBruto <= ALIQUOTA_INSS_PARAMETRO_1)
            return 0.08;
        else{
            if(valorSalarioBruto <= ALIQUOTA_INSS_PARAMETRO_2)
                return 0.09;
            else
                return 0.11;
        }
    }

    private double calculoBaseINSS(double valorSalarioBruto){
        if(valorSalarioBruto <= BASE_INSS_PARAMETRO)
            return valorSalarioBruto;
        else
            return BASE_INSS_PARAMETRO;
    }

    private double calculoAliquotaIRPF(Double baseIRPF){
        if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_4)
            return ALIQUOTA_IRPF_PARAMETRO_5;
        else{
            if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_3)
                return ALIQUOTA_IRPF_PARAMETRO_4;
            else{
                if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_2)
                    return ALIQUOTA_IRPF_PARAMETRO_3;
                else{
                    if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_1)
                        return ALIQUOTA_IRPF_PARAMETRO_2;
                    else
                        return ALIQUOTA_IRPF_PARAMETRO_1;
                }
            }
        }
    }

    private double calculoDeducaoIRPF(Double baseIRPF){
        if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_4)
            return DEDUCAO_IRPF_PARAMETRO_5;
        else{
            if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_3)
                return DEDUCAO_IRPF_PARAMETRO_4;
            else{
                if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_2)
                    return DEDUCAO_IRPF_PARAMETRO_3;
                else{
                    if(baseIRPF > ALIQUOTA_DEDUCAO_IRPF_PARAMETRO_1)
                        return DEDUCAO_IRPF_PARAMETRO_2;
                    else
                        return DEDUCAO_IRPF_PARAMETRO_1;
                }
            }
        }
    }
}
