package ie.wit.gareth.foodreviewapp.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Gareth on 10/04/2015.
 */
public class Restaurant {


    private String id;
    private String name;
    private String food;
    private String address;
    private LatLng latLng;

    public Restaurant(){

    }

    public Restaurant(String name, String food, String address) {
        this.name = name;
        this.food = food;
        this.address = address;
    }

    public Restaurant(String id, String name, String food, String address, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.food = food;
        this.address = address;
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatLng(LatLng latLng){
        this.latLng = latLng;
    }



}
