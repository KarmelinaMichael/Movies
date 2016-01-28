package com.km.movies.app;

/**
 * Created by lina on 12/12/2015.
 */
public class MovieGetJson {
    private String overview;
    private String rDate;
    private String vAvg;
    private String img;
    private String title;
    private String id;


    public MovieGetJson() {
    }

    public MovieGetJson(String title,String img,String overview, String rDate, String vAvg ,String id) {

        this.title=title;
        this.img = img;
        this.overview = overview;
        this.rDate = rDate;
        this.vAvg = vAvg;
        this.id=id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public String getrDate() {
        return rDate;
    }



    public String getvAvg() {
        return vAvg;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public void setvAvg(String vAvg) {
        this.vAvg = vAvg;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
