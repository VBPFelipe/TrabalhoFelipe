package com.vbpfelipe.SalaryCalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final double ALIQUOTA_INSS_PARAMETRO_1 = 1751.81;
    private static final double ALIQUOTA_INSS_PARAMETRO_2 = 2919.72;

    private static final double ALIQUOTA_INSS_1 = 0.08;
    private static final double ALIQUOTA_INSS_2 = 0.09;
    private static final double ALIQUOTA_INSS_3 = 0.11;

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

    FloatingActionButton fab;

    TextView txtAliquotaINSS, txtAliqINSSVal, txtBaseINSS, txtBaseINSSVal,
            txtValorINSS, txtValorINSSVal, txtBaseIRPF, txtBaseIRPFVal,
            txtAliquotaIRPF, txtAliquotaIRPFVal, txtDeducaoIRPF, txtDeducaoIRPFVal,
            txtValorIRPF, txtValorIRPFVal;

    TextView txtSalLiq, txtSalLiqVal;

    double salarioLiquido = 0;
    double valorIRPF = 0;
    double valorINSS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParameters();

        //Botão designado para o cálculo quando clicado
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


        //botão flutuante que inicializa os procedimentos para o gráfico quando clicado
        fab = (FloatingActionButton) findViewById(R.id.fabGrafico);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    plotGrafico();
                    Toast.makeText(getApplicationContext(), "Gerando Gráfico...", Toast.LENGTH_LONG).
                            show();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro: "+ e.getMessage(), Toast.LENGTH_LONG).
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

    /**
     * Método irá calcular o Salário Líquido do usuário,
     * de acordo com o salário bruto e quantida de dependentes
     * fornecidos na entrada de dados do app.
     *
     * O cálculo tem em base à partir dos descontos dos valores de INSS e IRPF
     * em cima do Salário Bruto.
     */
    private void calculoSalario(){

        double valorSalarioBruto = Double.parseDouble(edtSalarioBruto.getText().toString());
        double dependentes = Double.parseDouble(edtNDependentes.getText().toString());

        double aliquotaINSS = calculoAliquotaINSS(valorSalarioBruto);

        double baseINSS = calculoBaseINSS(valorSalarioBruto);

        valorINSS = aliquotaINSS * baseINSS;

        double baseIRPF = valorSalarioBruto - valorINSS - (dependentes * BASE_IRPF_PARAMETRO);

        double aliquotaIRPF = calculoAliquotaIRPF(baseIRPF);
        double deducaoIRPF = calculoDeducaoIRPF(baseIRPF);

        valorIRPF = (baseIRPF * aliquotaIRPF) - deducaoIRPF;

        salarioLiquido = valorSalarioBruto - valorINSS - valorIRPF;

        txtAliqINSSVal.setText( (aliquotaINSS * 100.00) + "%");
        txtBaseINSSVal.setText(String.format("%.02f", baseINSS));
        txtValorINSSVal.setText(String.format("%.02f", valorINSS));
        txtBaseIRPFVal.setText(String.format("%.02f", baseIRPF));
        txtAliquotaIRPFVal.setText( (aliquotaIRPF * 100.00) + "%");
        txtDeducaoIRPFVal.setText(String.format("%.02f", deducaoIRPF));
        txtValorIRPFVal.setText(String.format("%.02f", valorIRPF));
        txtSalLiqVal.setText(String.format("%.02f", salarioLiquido));
    }

    /**
     * Retorna Alíquota INSS
     * @param valorSalarioBruto
     * @return - Valor em tipo double, seguindo o padrão
     */
    private double calculoAliquotaINSS(double valorSalarioBruto){

        if(valorSalarioBruto <= ALIQUOTA_INSS_PARAMETRO_1)
            return ALIQUOTA_INSS_1;
        else{
            if(valorSalarioBruto <= ALIQUOTA_INSS_PARAMETRO_2)
                return ALIQUOTA_INSS_2;
            else
                return ALIQUOTA_INSS_3;
        }
    }

    /**
     * Retorna valor Base do INSS
     * @param valorSalarioBruto
     * @return - valor em tipo double, de acordo com o padrão
     */
    private double calculoBaseINSS(double valorSalarioBruto){
        if(valorSalarioBruto <= BASE_INSS_PARAMETRO)
            return valorSalarioBruto;
        else
            return BASE_INSS_PARAMETRO;
    }

    /**
     * Retorna o valor da Alíquota do IRPF
     * @param baseIRPF
     * @return - valor de acordo padrão, em tipo double
     */
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

    /**
     * Retorna o valor da Dedução do IRPF
     * @param baseIRPF
     * @return - valores da tabela de acordo com padrões, em tipo double
     */
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

    /**
     * plotGráfico
     * Povoa parâmetros com respectivos valores para repassar à Activity SalarioBRutoPieChart
     * que gerará o gráfico em disco
     *
     * Em seguida, inicia a activity.
     */
    private void plotGrafico(){
        Intent telaGrafico = new Intent(MainActivity.this, SalarioBrutoPieChart.class);

        //Passando os valores para a outra Activity - SalarioBrutoPieChart
        telaGrafico.putExtra("SalarioLiquido", Float.parseFloat(String.valueOf(salarioLiquido)));
        telaGrafico.putExtra("ValorINSS", Float.parseFloat(String.valueOf(valorINSS)));
        telaGrafico.putExtra("ValorIRPF", Float.parseFloat(String.valueOf(valorIRPF)));

        //chamando a outra activity
        startActivity(telaGrafico);
    }
}
