package dm.pivofinder.models;

import java.io.Serializable;

public class Beer implements Serializable
{

    public static int autoid = 1;
    public int beerId;
    public String name;
    public String bar;
    public double price;
    public double lat;
    public double lng;
    public String address;



    public Beer() {}

    public Beer(String name, String bar, double price, String add, double lat, double lng)
    {
        this.beerId = autoid++;
        this.name = name;
        this.bar = bar;
        this.price = price;
        this.address = add;
        this.lat = lat;
        this.lng = lng;

    }

    @Override
    public String toString() {
        return "bar [name=" + name
                + ", shop =" + bar + ", rating="  + ", lat=" + lat  + ", lng=" + lng + ", price=" + price
                 + "]";
    }

    public static int getAutoid() {
        return autoid;
    }

    public static void setAutoid(int autoid) {
        Beer.autoid = autoid;
    }

    public int getBeerId() {
        return beerId;
    }

    public void setBeerId(int beerId) {
        this.beerId = beerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
