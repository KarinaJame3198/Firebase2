package com.example.laujame.firebase2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import modelodedatos.model;

public class MyRecyclerViewHolder extends RecyclerView.Adapter<MyRecyclerViewHolder.ViewHolder>
        implements View.OnClickListener {

    //Estructura de datos para llenar los elementos graficos
    private ArrayList<model> modelList;

    //Agregamos un escuchador
    private View.OnClickListener listener;

    //Crear un constructor para inicializar la lista de modelos,
    //con los datos que mandan firebase y poder usarlos
    public MyRecyclerViewHolder(ArrayList<model> modelList) {
        this.modelList = modelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflar layout(representarlo de manera grafica)
        View view = LayoutInflater.from(
                parent.getContext()).
                inflate(R.layout.model_item_bd,
                        parent, false);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        model model = modelList.get(position);
        holder.lblId.setText(model.getId());
        holder.lblGroup.setText(model.getGroup());
        holder.lblLecture.setText(model.getLecture());
        holder.lblActivity.setText(model.getActivity());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setOnCLickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        //Aqui vamos a inicializar los componentes gráficos del xml
        //con el texto quet trae la lista de objetos que manda el
        //usuario al acceder a la base de datos de firebase.

        private TextView lblId, lblGroup, lblLecture, lblActivity;
        public View  view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            this.lblId = view.findViewById(R.id.lblIdModelItem);
            this.lblGroup = view.findViewById(R.id.lblGrupoModelItem2);
            this.lblLecture = view.findViewById(R.id.lblMateriaModelItem3);
            this.lblActivity = view.findViewById(R.id.lblActividadModelItem4);
        }
    }

}