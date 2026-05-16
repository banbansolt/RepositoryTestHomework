package kr.ac.kopo.sang.myapplicationtest;

import java.io.Serializable;

public class TodoItem implements Serializable {
    private String title;
    private String time;
    private boolean isChecked; // 삭제 모드 체크용

    public TodoItem(String title) {
        this.title = title;
        this.time = "시간 미설정";
        this.isChecked = false;
    }

    public String getTitle() { return title; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}