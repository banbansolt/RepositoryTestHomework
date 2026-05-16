package kr.ac.kopo.sang.myapplicationtest;

import java.io.Serializable;

public class TodoItem implements Serializable {

    private String title;

    private String time;

    private boolean isChecked;

    public TodoItem(String title) {

        this.title = title;

        this.time = "시간 미설정";

        this.isChecked = false;
    }

    public String getTitle() {

        return title;
    }

    // 추가된 부분
    public void setTitle(String title) {

        this.title = title;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }

    public boolean isChecked() {

        return isChecked;
    }

    public void setChecked(boolean checked) {

        isChecked = checked;
    }
}