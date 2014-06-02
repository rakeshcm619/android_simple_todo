package code.rakeshcm.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	ArrayList<String> items;
	private int itemEditPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		int itemPosition = getIntent().getIntExtra("itemPosition", 0);
		this.itemEditPosition = itemPosition;
		readItems();
		populateView();
	}
	
	public void saveEditedTodoItem(View v) {
		EditText editItem = (EditText) findViewById(R.id.etTodoItem);
    	items.set(this.itemEditPosition, editItem.getText().toString());
		saveItems();
    	setResult(RESULT_OK);
		finish();
    }
	
	public void cancelEditedTodoItem(View v) {
		setResult(RESULT_CANCELED);
		finish();
    }
	
	private void populateView() {
		if(this.itemEditPosition > items.size() ) {
			setResult(RESULT_CANCELED);
			finish();
		}
		EditText editItem = (EditText) findViewById(R.id.etTodoItem);
		String selectedItem = items.get(this.itemEditPosition);
		editItem.setText(selectedItem);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
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
