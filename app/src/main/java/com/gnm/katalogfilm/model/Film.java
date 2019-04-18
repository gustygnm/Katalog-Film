package com.gnm.katalogfilm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Film implements Parcelable {

    @SerializedName("page")
    private Integer mPage;

    @SerializedName("results")
    private List<DetailFilm> mFilmResults;

    @SerializedName("total_pages")
    private Integer mTotalPages;

    @SerializedName("total_results")
    private Integer mTotalResults;

    public Integer getmPage() {
        return mPage;
    }

    public void setmPage(Integer mPage) {
        this.mPage = mPage;
    }

    public List<DetailFilm> getmFilmResults() {
        return mFilmResults;
    }

    public void setmFilmResults(List<DetailFilm> mFilmResults) {
        this.mFilmResults = mFilmResults;
    }

    public Integer getmTotalPages() {
        return mTotalPages;
    }

    public void setmTotalPages(Integer mTotalPages) {
        this.mTotalPages = mTotalPages;
    }

    public Integer getmTotalResults() {
        return mTotalResults;
    }

    public void setmTotalResults(Integer mTotalResults) {
        this.mTotalResults = mTotalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mPage);
        dest.writeTypedList(this.mFilmResults);
        dest.writeValue(this.mTotalPages);
        dest.writeValue(this.mTotalResults);
    }

    public Film() {
    }

    protected Film(Parcel in) {
        this.mPage = (Integer) in.readValue(Long.class.getClassLoader());
        this.mFilmResults = in.createTypedArrayList(DetailFilm.CREATOR);
        this.mTotalPages = (Integer) in.readValue(Long.class.getClassLoader());
        this.mTotalResults = (Integer) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}
