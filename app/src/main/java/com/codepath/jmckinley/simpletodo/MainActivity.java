package com.codepath.jmckinley.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapater itemsAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        //etItem.setText("I'm doing this from Java!");

        loadItems();

        final ItemsAdapater.OnLongClickListener onLongClickListener = new ItemsAdapater.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position){
                //Delete the item from the model
                items.remove(position);
                //Notify the adapater
                itemsAdapater.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapater = new ItemsAdapater(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapater);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //Add new item to the model
                items.add(todoItem);
                //notify adapater that a item was inserted
                itemsAdapater.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems(){
       try {
           items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
       }catch(IOException e){
           Log.e("MainActivity", "Error reading items", e);
           items = new ArrayList<>();
       }
    }

    //This function saves items by writing them into the data file
    private void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        }catch(IOException e){
            Log.e("MainActivity", "Error writting items", e);
        }
    }
}
