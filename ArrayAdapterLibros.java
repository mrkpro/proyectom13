package com.example.alumne.changebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alumne on 20/04/16.
 */
public class ArrayAdapterLibros extends BaseAdapter {
    ArrayList<Libros> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    public ArrayAdapterLibros(Context context, ArrayList<Libros> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Libros getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Libros currentListData = getItem(position);

        mViewHolder.tvName.setText(currentListData.getName());
        mViewHolder.tvEditorial.setText(currentListData.getEditorial());
        mViewHolder.tvGenero.setText(currentListData.getGenero());
        mViewHolder.tvPrecio.setText(currentListData.getPrecio());
        mViewHolder.ivImage.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(currentListData.getImage(),Base64.DEFAULT),0,Base64.decode(currentListData.getImage(),Base64.DEFAULT).length));

        return convertView;
    }

    private class MyViewHolder {
        TextView tvName, tvEditorial,tvGenero,tvPrecio;
        ImageView ivImage;

        public MyViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.textViewName);
            tvEditorial = (TextView) item.findViewById(R.id.textViewEditorial);
            tvGenero = (TextView) item.findViewById(R.id.textViewGenero);
            tvPrecio = (TextView) item.findViewById(R.id.textViewPrecio);
            ivImage = (ImageView) item.findViewById(R.id.ivimage);
        }
    }
}
