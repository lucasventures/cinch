package com.virtualspaces.cinch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by LUCASVENTURES on 5/26/2016.
 */
public class PieChartHelper implements OnChartValueSelectedListener{
    private String font = "fonts/pt-sans.ttf";


    private PieChart mPieChart;
    private Context mContext;
    private String[] mCategories;
    private Typeface typeFace;
    //values are entered in order of
    private ArrayList<Double> mDataValues;

    PieChartHelper(PieChart pieChart, Context context, String[] categoryNames, ArrayList<Double> dataValues){
        mPieChart = pieChart;
        mContext = context;
        mCategories = categoryNames;
        mDataValues = dataValues;
    }

    public void setUpPieChart(){

        //set up look of pie chart
        typeFace = Typeface.createFromAsset(mContext.getAssets(),font);

        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription(" Describes budget as configured in menu.");
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterTextTypeface(typeFace);
        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(40f);
        mPieChart.setTransparentCircleRadius(48f);

        // mChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        //mChart.setUnit(" $");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this);
        //set data (params = (number or categories, range, chart object);
        setData(mCategories.length-1, mPieChart);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
       // mPieChart.spin(2000, 0, 360, Easing.EasingOption.EaseInBounce);
/*

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
 */
    }

    private void setData(int count, PieChart mChart) {

        ArrayList<Entry> yVals1 = new ArrayList<>();

        /*
        IMPORTANT: In a PieChart, no values (Entry) should have the same
        xIndex (even if from different DataSets), since no values can be
        drawn above each other.
         */

        for (int i = 0; i < count +1; i++) {
            yVals1.add(new Entry((float) ((double)mDataValues.get(i)), i));
        }

        ArrayList<String> xVals = new ArrayList<>();
        //creates names for categories
        for (int i = 0; i < count + 1; i++){
            xVals.add(mCategories[i % mCategories.length]);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add colors
        dataSet.setColors(new int[]{
                R.color.pie_red,
                R.color.pie_blue,
                R.color.pie_green,
                R.color.pie_gray

        } ,mChart.getContext());
        dataSet.setSelectionShift(3f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(typeFace);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Categorized\nSpending\nAnalysis");
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, s.length(), 0);
        return s;
    }

}
