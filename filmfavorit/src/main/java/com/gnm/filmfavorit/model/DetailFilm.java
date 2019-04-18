package com.gnm.filmfavorit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailFilm implements Parcelable {

    @SerializedName("adult")
    private Boolean mAdult;

    @SerializedName("backdrop_path")
    private String mBackdropPath;

    @SerializedName("genre_ids")
    private List<Long> mGenreIds;

    @SerializedName("homepage")
    private String mHomepage;

    @SerializedName("id")
    private Integer mId;

    @SerializedName("original_language")
    private String mOriginalLanguage;

    @SerializedName("original_title")
    private String mOriginalTitle;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("popularity")
    private Double mPopularity;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("runtime")
    private Integer mRuntime;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("tagline")
    private String mTagline;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("video")
    private Boolean mVideo;

    @SerializedName("vote_average")
    private Double mVoteAverage;

    @SerializedName("vote_count")
    private Long mVoteCount;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBackdropPath);
        dest.writeValue(this.mId);
        dest.writeValue(this.mRuntime);
        dest.writeString(this.mOriginalLanguage);
        dest.writeString(this.mOriginalTitle);
        dest.writeString(this.mOverview);
        dest.writeString(this.mTagline);
        dest.writeValue(this.mPopularity);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mTitle);
        dest.writeList(this.mGenreIds);
        dest.writeValue(this.mAdult);
        dest.writeValue(this.mVideo);
        dest.writeValue(this.mVoteAverage);
        dest.writeValue(this.mVoteCount);
    }

    public DetailFilm(Parcel in) {
        this.mBackdropPath = in.readString();
        this.mId = (Integer) in.readValue(Long.class.getClassLoader());
        this.mRuntime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mOriginalLanguage = in.readString();
        this.mOriginalTitle = in.readString();
        this.mOverview = in.readString();
        this.mTagline = in.readString();
        this.mPopularity = (Double) in.readValue(Double.class.getClassLoader());
        this.mPosterPath = in.readString();
        this.mReleaseDate = in.readString();
        this.mTitle = in.readString();
        this.mGenreIds = new ArrayList<Long>();
        in.readList(this.mGenreIds, Long.class.getClassLoader());
        this.mAdult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mVideo = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mVoteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.mVoteCount = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<DetailFilm> CREATOR = new Creator<DetailFilm>() {
        @Override
        public DetailFilm createFromParcel(Parcel in) {
            return new DetailFilm(in);
        }

        @Override
        public DetailFilm[] newArray(int size) {
            return new DetailFilm[size];
        }
    };

    public DetailFilm() {

    }

    public Boolean getmAdult() {
        return mAdult;
    }

    public void setmAdult(Boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public List<Long> getmGenreIds() {
        return mGenreIds;
    }

    public void setmGenreIds(List<Long> mGenreIds) {
        this.mGenreIds = mGenreIds;
    }

    public String getmHomepage() {
        return mHomepage;
    }

    public void setmHomepage(String mHomepage) {
        this.mHomepage = mHomepage;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setmOriginalLanguage(String mOriginalLanguage) {
        this.mOriginalLanguage = mOriginalLanguage;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public Double getmPopularity() {
        return mPopularity;
    }

    public void setmPopularity(Double mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public Integer getmRuntime() {
        return mRuntime;
    }

    public void setmRuntime(Integer mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmTagline() {
        return mTagline;
    }

    public void setmTagline(String mTagline) {
        this.mTagline = mTagline;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Boolean getmVideo() {
        return mVideo;
    }

    public void setmVideo(Boolean mVideo) {
        this.mVideo = mVideo;
    }

    public Double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(Double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public Long getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(Long mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
