package com.example.shubhampandey.ilovezappos.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.example.shubhampandey.ilovezappos.R;
import com.example.shubhampandey.ilovezappos.services.NotificationTriggerService;
import com.example.shubhampandey.ilovezappos.utils.CommonUtils;
import com.example.shubhampandey.ilovezappos.utils.HourAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.shubhampandey.ilovezappos.utils.CommonUtils.isNetworkAvailable;

public class GraphActivity extends BaseActivity {
    private LineChart mChart;
    private long mReferenceTime;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final long DELAY_IN_MILLIS = 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        Intent intent1 = new Intent(getApplicationContext(), NotificationTriggerService.class);
        PendingIntent pending_intent = PendingIntent.getService(getApplicationContext(), 0, intent1, 0);
        AlarmManager alarm_mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm_mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), DELAY_IN_MILLIS, pending_intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initListeners();
    }

    private void initViews() {
        mChart = (LineChart) findViewById(R.id.chart);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    private void initListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pingBTCUSDApi();
            }
        });
        pingBTCUSDApi();
    }

    private void pingBTCUSDApi() {
        if (isNetworkAvailable(getApplicationContext())) {
            new BTCTrendTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    private List<Entry> getGraphParamsDollar() {
        JSONArray jsonArray = CommonUtils.getJSON(getString(R.string.graph_url), null);
        List<Entry> entryList = new ArrayList<>();
        if (jsonArray != null) {
            try {
                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                    JSONObject referenceObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                    mReferenceTime = referenceObject.getLong(getString(R.string.date));
                    JSONObject object = jsonArray.getJSONObject(i);
                    double price = object.getDouble(getString(R.string.price)) * object.getDouble(getString(R.string.amount));
                    long date = object.getLong(getString(R.string.date)) - mReferenceTime;
                    Entry btcEntry = new Entry(date, (float) price);
                    entryList.add(btcEntry);
                }
                return entryList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected int getContentPageLayoutId() {
        return R.layout.activity_main;
    }

    class BTCTrendTask extends AsyncTask<String, Void, List<Entry>> {
        @Override
        protected List<Entry> doInBackground(String... strings) {
            return getGraphParamsDollar();
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            if (entries != null && !entries.isEmpty()) {
                LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.value_vs_time));
                lineDataSet.setColor(Color.BLUE);
                lineDataSet.setLineWidth(3f);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillColor(Color.RED);
                lineDataSet.setValueTextSize(5f);
                lineDataSet.setValueTextColor(Color.RED);
                LineData lineData = new LineData(lineDataSet);
                lineData.getXMin();
                lineData.getYMin();
                mChart.setData(lineData);
                mChart.moveViewTo((lineData.getXMin() + lineData.getXMax()) / 2, lineData.getYMax(), YAxis.AxisDependency.LEFT);
                XAxis xAxis = mChart.getXAxis();
                IAxisValueFormatter iAxisValueFormatter = new HourAxisValueFormatter(mReferenceTime);
                xAxis.setValueFormatter(iAxisValueFormatter);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                mChart.getAxisLeft().setEnabled(false);
                mSwipeRefreshLayout.setRefreshing(false);
                mChart.invalidate();
            }
        }
    }
}
