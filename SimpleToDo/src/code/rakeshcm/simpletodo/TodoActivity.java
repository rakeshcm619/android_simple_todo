package code.rakeshcm.simpletodo;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView itemsList;
	private final int EDIT_REQUEST_CODE = 33;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        itemsList = (ListView) findViewById(R.id.listTodo);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(itemsAdapter);
        setupListViewListener();
        setupEditListViewListener();
        
    }

    private void setupListViewListener() {
    	itemsList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
    }
    
    private void setupEditListViewListener() {
    	itemsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
				// could have passed the select item string and pass back edited string with position in onActicityResult()
				// But it works only for string. If we need bigger things, than read and write file in new activity. 
				i.putExtra("itemPosition", position);
				startActivityForResult(i, EDIT_REQUEST_CODE);
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// EDIT_REQUEST_CODE is defined above
    	if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
    		readItems();
    		itemsAdapter.notifyDataSetChanged();
    	}
    } 
    
	private void readItems() {
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir, "todo.txt");
    	try {
    		if(items == null) {
    			items = new ArrayList<String>(FileUtils.readLines(todoFile));
    		}
    		else {
    			items.clear();
    			items.addAll(new ArrayList<String>(FileUtils.readLines(todoFile)));
    		}
    	}
    	catch (IOException ex) {
    		if(items == null) {
        		items = new ArrayList<String>();
    		}
    		else {
    			items.clear();
    		}
    		ex.printStackTrace();
    	}
    }
    
    private void saveItems() {
    	File fileDir = getFilesDir();
    	File todoFile = new File(fileDir, "todo.txt");
    	try {
    		FileUtils.writeLines(todoFile, items);
    	}
    	catch (IOException ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void addTodoItem(View v) {
    	EditText newItem = (EditText) findViewById(R.id.newItem);
    	itemsAdapter.add(newItem.getText().toString());
    	newItem.setText("");
    	saveItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
