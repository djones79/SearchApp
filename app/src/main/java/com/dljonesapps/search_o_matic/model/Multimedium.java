
package com.dljonesapps.search_o_matic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import java.io.Serializable;

public class Multimedium implements Serializable {

    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("legacy")
    @Expose
    private Legacy legacy;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getUrl() {
        return !TextUtils.isEmpty(url)
                ? "http://www.nytimes.com/" + url
                : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Legacy getLegacy() {
        return legacy;
    }

    public void setLegacy(Legacy legacy) {
        this.legacy = legacy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
