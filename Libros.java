package com.example.alumne.changebook;



import java.sql.Blob;

/**
 * Created by alumne on 20/04/16.
 */
public class Libros {
    public String id;
    public String name;
    public String genero;
    public String editorial;
    public String precio;
    public String image;
    public String coduser;



    public Libros(String name,String editorial, String genero,String precio,String image) {
        this.name = name;
        this.genero = genero;
        this.editorial = editorial;
        this.precio = precio;
        this.image=image;


    }
    public Libros(String id,String name,String editorial, String genero,String precio,String image) {
        this.name = name;
        this.genero = genero;
        this.editorial = editorial;
        this.precio = precio;
        this.image=image;
        this.id=id;


    }
    public Libros(String id,String name,String editorial, String genero,String precio,String image,String coduser) {
        this.name = name;
        this.genero = genero;
        this.editorial = editorial;
        this.precio = precio;
        this.image=image;
        this.id=id;
        this.coduser=coduser;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoduser() {
        return coduser;
    }

    public void setCoduser(String coduser) {
        this.coduser = coduser;
    }

}
