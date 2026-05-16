package kr.ac.kopo.sang.myapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TodoItem> todoList;
    private CustomMainAdapter adapter;
    private EditText etTodo;

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<TodoItem> updatedList = (ArrayList<TodoItem>) result.getData().getSerializableExtra("updatedData");
                    if (updatedList != null) {
                        todoList.clear();
                        todoList.addAll(updatedList);
                        adapter.notifyDataSetChanged();
                        saveData();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTodo = findViewById(R.id.et_todo);
        ListView lvTodo = findViewById(R.id.lv_todo);
        Button btnAdd = findViewById(R.id.btn_add);
        Button btnComplete = findViewById(R.id.btn_complete);

        loadData();

        adapter = new CustomMainAdapter(this, todoList);
        lvTodo.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String text = etTodo.getText().toString().trim();
            if (!text.isEmpty()) {
                todoList.add(new TodoItem(text));
                adapter.notifyDataSetChanged();
                etTodo.setText("");
                saveData();
            }
        });

        btnComplete.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("todoData", todoList);
            resultLauncher.launch(intent);
        });
    }

    // Gson 없이 순정 JSONArray로 저장하는 로직
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("todo_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();
        try {
            for (TodoItem item : todoList) {
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

    // Gson 없이 순정 JSONArray로 불러오는 로직
    private void loadData() {
        todoList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("todo_prefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("task_list", null);

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String time = jsonObject.getString("time");

                    TodoItem item = new TodoItem(title);
                    item.setTime(time);
                    todoList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}