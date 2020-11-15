package com.ebs.banglalinkbangladhol.bean;

/**
 * Created by Rajuwan on 30-Apr-17.
 */

public class Promo {

    public int show;
    public String promourl;
    public String promotexturl;

    public Promo(int show, String promourl, String promotexturl) {
        this.show = show;
        this.promourl = promourl;
        this.promotexturl = promotexturl;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getPromourl() {
        return promourl;
    }

    public void setPromourl(String promourl) {
        this.promourl = promourl;
    }

    public String getPromotexturl() {
        return promotexturl;
    }

    public void setPromotexturl(String promotexturl) {
        this.promotexturl = promotexturl;
    }

}
