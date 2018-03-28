package com.example.poojadeole.homework9final;

/**
 * Created by poojadeole on 11/23/17.
 */

public class FavInfo {
    String favsym;
    String favprice;
    String favchange;

    FavInfo(String str) {
        this.favsym = str;
        this.favprice = "44";
        this.favchange = "+1.2";
    }

    FavInfo(String str, String price, String change) {
        this.favsym = str;
        this.favprice = price;
        this.favchange = change;
    }


    FavInfo() {

    }
}
