package ie.wit.gareth.foodreviewapp.models;



/**
 * Created by Gareth on 13/04/2015.
 */

public class Review {

    private String id;
    private String title;
    private String date;
    private String details;
    private String comeback;
    private String restaurant_id;
    private String user_id;

    public Review(){

    }

    public Review(String id, String title, String date, String details, String comeback) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.details = details;
        this.comeback = comeback;
    }

    public Review(String title, String date, String details, String comeback, String restaurant_id, String user_id) {
        this.title = title;
        this.date = date;
        this.details = details;
        this.comeback = comeback;
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getComeback() {
        return comeback;
    }

    public void setComeback(String comeback) {
        this.comeback = comeback;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
