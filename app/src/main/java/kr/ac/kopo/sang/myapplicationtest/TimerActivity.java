package kr.ac.kopo.sang.myapplicationtest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {
    private TextView tvDate, tvTime;
    private int y, m, d, h, mi;
    private boolean dSel = false, tSel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        tvDate = findViewById(R.id.tv_selected_date);
        tvTime = findViewById(R.id.tv_selected_time);

        Calendar c = Calendar.getInstance();
        y = c.get(Calendar.YEAR); m = c.get(Calendar.MONTH); d = c.get(Calendar.DAY_OF_MONTH);
        h = c.get(Calendar.HOUR_OF_DAY); mi = c.get(Calendar.MINUTE);

        findViewById(R.id.btn_date).setOnClickListener(v -> new DatePickerDialog(this, (view, year, month, day) -> {
            tvDate.setText(year + "년 " + (month + 1) + "월 " + day + "일");
            dSel = true;
        }, y, m, d).show());

        findViewById(R.id.btn_time).setOnClickListener(v -> new TimePickerDialog(this, (view, hour, min) -> {
            tvTime.setText(hour + "시 " + min + "분");
            tSel = true;
        }, h, mi, false).show());

        findViewById(R.id.btn_timer_back).setOnClickListener(v -> {
            if (dSel && tSel) {
                Intent intent = new Intent();
                intent.putExtra("selectedTime", tvDate.getText().toString() + " " + tvTime.getText().toString());
                setResult(RESULT_OK, intent);
            }
            finish();
        });
    }
}