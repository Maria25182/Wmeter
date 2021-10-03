package com.example.wmeter;


import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wmeter.models.mediciones;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MensajeAdapter  extends RecyclerView.Adapter<MensajeAdapter.viewHolder>{
    Context context;
    ArrayList<mediciones> list;

    public MensajeAdapter(Context context, ArrayList<mediciones> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items,parent,false);
        return  new viewHolder(v);
    }

    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    public void onBindViewHolder(@NonNull MensajeAdapter.viewHolder holder, int position) {
        mediciones data = list.get(position);
        long timestampString = Long.parseLong(data.getTime());
        String value = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"). format(new Date(timestampString * 1000));

        holder.txtFecha.setText("FECHA = " + value);
        holder.txtSensor.setText("VOLUMEN L*M = " + String.valueOf(data.getSensor())+"L");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtFecha, txtSensor;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.textfecha);
            txtSensor = itemView.findViewById(R.id.textsensor);
        }
    }
}
