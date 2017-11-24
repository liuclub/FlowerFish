package com.bagelplay.gameset.evagame.doman;

/**
 * Created by liubo on 2017/10/31.
 */

public class Food {
    private String chineseName;//中文名字
    private String englishName;//英文名字
    private int chineseSoundResId;//中文发音资源ID
    private int englishSoundResId;//英文发音资源ID
    private int imageResId;//图片资源ID

    public Food(String chineseName, String englishName, int chineseSoundResId, int englishSoundResId, int imageResId) {
        this.chineseSoundResId = chineseSoundResId;
        this.englishSoundResId = englishSoundResId;
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.imageResId = imageResId;
    }

    public Food() {
    }


    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getChineseSoundResId() {
        return chineseSoundResId;
    }

    public void setChineseSoundResId(int chineseSoundResId) {
        this.chineseSoundResId = chineseSoundResId;
    }

    public int getEnglishSoundResId() {
        return englishSoundResId;
    }

    public void setEnglishSoundResId(int englishSoundResId) {
        this.englishSoundResId = englishSoundResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public String toString() {
        return "Food{" +
                "chineseName='" + chineseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", chineseSoundResId=" + chineseSoundResId +
                ", englishSoundResId=" + englishSoundResId +
                ", imageResId=" + imageResId +
                '}';
    }
}
