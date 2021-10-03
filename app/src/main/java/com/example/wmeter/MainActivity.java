package com.example.wmeter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Button lista;
    DatabaseReference mDatabase;
    GraphView graph;
    Long fechasL;
    //Calendar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        graph= (GraphView) findViewById(R.id.graph);
        lista = (Button) findViewById(R.id.button);

        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lista = new Intent(MainActivity.this, MainList.class);
                startActivity(lista);
            }
        });
        Data();
    }

    public void Data(){
        mDatabase.child("mediciones").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("asdsad", "onComplete: " + task.getException());
                }else{
                    SimpleDateFormat formato = new SimpleDateFormat("E MMM dd yyyy hh:mm:ss");

                    for (DataSnapshot data : task.getResult().getChildren()){
                        String fecha = data.child("time").getValue().toString();
                        String sensor = data.child("sensor").getValue().toString();
                        Float sensores= Float.parseFloat(sensor);
                        Long fechaF = Long. parseLong(fecha);

                        series.appendData(new DataPoint(fechaF,sensores),true,100);
                        Log.d("FECHA", "onComplete: " + fecha);

                        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                            @Override

                            public String formatLabel(double value, boolean isValueX) {

                                if(isValueX){
                                    Format format = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
                                    return format.format(value*1000);


                                }
                                return super.formatLabel(value,isValueX);

                            }
                        });


                    }
                    graph.addSeries(series);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(10);
                    graph.getGridLabelRenderer().setNumVerticalLabels(10);
                    graph.getViewport().setScrollable(true);
                    graph.getViewport().setScalableY(true);
                    //graph.getViewport().setYAxisBoundsManual(true);
                    graph.getGridLabelRenderer().setHorizontalLabelsAngle(60);
                    graph.getGridLabelRenderer().setTextSize(25);



                }
                graph.removeAllSeries();graph.addSeries(series);
            }

        });

    }




}