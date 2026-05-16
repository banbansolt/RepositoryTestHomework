package kr.ac.kopo.sang.myapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<TodoItem> resultList;
    private CustomAdapter adapter;
    private Button btnDeleteMode;
    private boolean isDeleteMode = false;
    private int targetPosition = -1;

    private final ActivityResultLauncher<Intent> timerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedTime = result.getData().getStringExtra("selectedTime");
                    if (selectedTime != null && targetPosition != -1) {
                        resultList.get(targetPosition).setTime(selectedTime);
                        adapter.notifyDataSetChanged();
                        saveData();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultList = (ArrayList<TodoItem>) getIntent().getSerializableExtra("todoData");
        if (resultList == null) resultList = new ArrayList<>();

        ListView lvResult = findViewById(R.id.lv_result);
        btnDeleteMode = findViewById(R.id.btn_delete_mode);
        Button btnBack = findViewById(R.id.btn_back);

        adapter = new CustomAdapter();
        lvResult.setAdapter(adapter);

        btnDeleteMode.setOnClickListener(v -> {
            if (!isDeleteMode) {
                isDeleteMode = true;
                btnDeleteMode.setText("확인 삭제");
            } else {
                deleteItems();
                isDeleteMode = false;
                btnDeleteMode.setText("삭제");
            }
            adapter.notifyDataSetChanged();
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("updatedData", resultList);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    private void deleteItems() {
        ArrayList<TodoItem> toRemove = new ArrayList<>();
        for (TodoItem item : resultList) if (item.isChecked()) toRemove.add(item);
        resultList.removeAll(toRemove);
        saveData();
        Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show();
    }

    // ResultActivity의 순정 저장 로직
    private void saveData() {
        SharedPreferences sp = getSharedPreferences("todo_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        JSONArray jsonArray = new JSONArray();
        try {
            for (TodoItem item : resultList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", item.getTitle());
                jsonObject.put("time", item.getTime());
                jsonArray.put(jsonObject);
            }
            editor.putString("task_list", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class CustomAdapter extends BaseAdapter {
        @Override public int getCount() { return resultList.size(); }
        @Override public Object getItem(int p) { return resultList.get(p); }
        @Override public long getItemId(int p) { return p; }

        @Override
        public View getView(int p, View v, ViewGroup parent) {
            if (v == null) v = getLayoutInflater().inflate(R.layout.activity_todo, parent, false);
            TodoItem item = resultList.get(p);

            CheckBox cb = v.findViewById(R.id.cb_delete);
            TextView title = v.findViewById(R.id.tv_todo_text);
            TextView time = v.findViewById(R.id.tv_todo_time);
            Button btn = v.findViewById(R.id.btn_set_timer);

            title.setText(item.getTitle());
            time.setText(item.getTime());
            cb.setChecked(item.isChecked());
            cb.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
            btn.setVisibility(isDeleteMode ? View.GONE : View.VISIBLE);

            v.setOnClickListener(view -> {
                if(isDeleteMode) {
                    item.setChecked(!item.isChecked());
                    notifyDataSetChanged();
                }
            });

            btn.setOnClickListener(view -> {
                targetPosition = p;
                timerLauncher.launch(new Intent(ResultActivity.this, TimerActivity.class));
            });
            return v;
        }
    }
}