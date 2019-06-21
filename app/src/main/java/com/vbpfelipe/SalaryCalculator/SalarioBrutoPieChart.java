package com.vbpfelipe.SalaryCalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class SalarioBrutoPieChart extends AppCompatActivity {

    private PieChart myPieChart;

    List<PieEntry> valores = new ArrayList<>();
    PieDataSet dataSet1;
    PieData dados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salario_bruto_pie_chart);

        myPieChart = (PieChart) findViewById(R.id.graficoSalarioB);

        /* Recebendo os valores da Activity que chamou */
        Bundle param= getIntent().getExtras(); //recebe parâmetros
        float SLf = param.getFloat("SalarioLiquido");
        float INSSf = param.getFloat("ValorINSS");
        float IRPFf = param.getFloat("ValorIRPF");

        System.out.println("Valores Recebidos: " + SLf + ", " + INSSf + ", " + IRPFf);


        //Configurando o gráfico
        myPieChart.setUsePercentValues(true);
        myPieChart.getDescription().setEnabled(false);
        myPieChart.setExtraOffsets(5,10,5,5);

        myPieChart.setDragDecelerationFrictionCoef(0.95f);

        //se true, grafico pizza com furo no meio (formato disco)
        myPieChart.setDrawHoleEnabled(true);
        myPieChart.setHoleColor(Color.WHITE);
        myPieChart.setTransparentCircleRadius(31f);
        //tamanho em % do furo no meio
        myPieChart.setHoleRadius(20f);
        //Cor e tamanho da fonte dos nomes
        myPieChart.setEntryLabelColor(Color.DKGRAY);
        myPieChart.setEntryLabelTextSize(12f);

        //Repassando valores dos atributos para o gráfico
        valores.add(new PieEntry( SLf, "% Salário Líquido" ));
        valores.add(new PieEntry( INSSf, "% INSS" ));
        valores.add(new PieEntry( IRPFf, "% IRPF" ));

        /* Animação na montagem da pizza */
        myPieChart.animateY(1400, Easing.EaseInOutCubic);

        dataSet1 = new PieDataSet(valores, "\n Gráfico Percentual no Salário Bruto");
        //largura da separação das partes da pizza
        dataSet1.setSliceSpace(6f);
        dataSet1.setIconsOffset(new MPPointF(0, 40));
        dataSet1.setSelectionShift(5f);
        dataSet1.setColors(ColorTemplate.PASTEL_COLORS);

        dados = new PieData( (dataSet1) );
        /* Cor e tamanho da fonte do percentuais */
        dados.setValueTextSize(14f);
        dados.setValueTextColor(Color.BLACK);

        myPieChart.setData(dados);
    }
}
