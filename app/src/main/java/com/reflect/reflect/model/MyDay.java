package com.reflect.reflect.model;

public class MyDay {


    private String uid;
    private String id;
    private Long date;
    private String emoji;

    public MyDay(String uid, String id, Long date, String emoji, String spotifySongName, String spotifySongCover, String spotifySongArtist, String grateFulForToday, String journalToday) {
        this.uid = uid;
        this.id = id;
        this.date = date;
        this.emoji = emoji;
        this.spotifySongName = spotifySongName;
        this.spotifySongCover = spotifySongCover;
        this.spotifySongArtist = spotifySongArtist;
        this.grateFulForToday = grateFulForToday;
        this.journalToday = journalToday;
    }
    public MyDay(){}

    private String spotifySongName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getSpotifySongName() {
        return spotifySongName;
    }

    public void setSpotifySongName(String spotifySongName) {
        this.spotifySongName = spotifySongName;
    }

    public String getSpotifySongCover() {
        return spotifySongCover;
    }

    public void setSpotifySongCover(String spotifySongCover) {
        this.spotifySongCover = spotifySongCover;
    }

    public String getSpotifySongArtist() {
        return spotifySongArtist;
    }

    public void setSpotifySongArtist(String spotifySongArtist) {
        this.spotifySongArtist = spotifySongArtist;
    }

    public String getGrateFulForToday() {
        return grateFulForToday;
    }

    public void setGrateFulForToday(String grateFulForToday) {
        this.grateFulForToday = grateFulForToday;
    }

    public String getJournalToday() {
        return journalToday;
    }

    public void setJournalToday(String journalToday) {
        this.journalToday = journalToday;
    }

    private String spotifySongCover;
    private String spotifySongArtist;
    private String grateFulForToday;
    private String journalToday;

}
