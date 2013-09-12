package views;

import java.util.ArrayList;
import java.util.HashSet;

import tools.AdvLoaderThread;
import tools.ThreadResponseDelegate;
import tools.XmlRequestHandler;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.R.menu;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class EmailSelection extends Activity  implements ThreadResponseDelegate{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_selection);    	
    	
		new AdvLoaderThread(this){
			@Override
    		protected Object doInBackground(Object... arg0) {   
				_data = getNameEmailDetails();
    			return null;
    		}
			
			public ArrayList<String> getNameEmailDetails() {
    		    ArrayList<String> emlRecs = new ArrayList<String>();
    		    HashSet<String> emlRecsHS = new HashSet<String>();
    		    ContentResolver cr = ((Context)_delegate).getApplicationContext().getContentResolver();
    		    String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID, 
    		            ContactsContract.Contacts.DISPLAY_NAME,
    		            ContactsContract.Contacts.PHOTO_ID,
    		            ContactsContract.CommonDataKinds.Email.DATA, 
    		            ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
    		    String order = "CASE WHEN " 
    		            + ContactsContract.Contacts.DISPLAY_NAME 
    		            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, " 
    		            + ContactsContract.Contacts.DISPLAY_NAME 
    		            + ", " 
    		            + ContactsContract.CommonDataKinds.Email.DATA
    		            + " COLLATE NOCASE";
    		    String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
    		    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
    		    if (cur.moveToFirst()) {
    		        do {
    		            // names comes in hand sometimes
    		            String name = cur.getString(1);
    		            String emlAddr = cur.getString(3);

    		            // keep unique only
    		            if (emlRecsHS.add(emlAddr.toLowerCase())) {
    		                emlRecs.add(name+" ("+emlAddr+")");    		                
    		            }
    		        } while (cur.moveToNext());
    		    }

    		    cur.close();
    		    return emlRecs;
    		}
		}.execute();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.email_selection, menu);
		return true;
	}



	@Override
	public void threadResponseLoaded(ArrayList<String> response) {
		
		// generate email list
		ArrayList<String> emails = response;
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout round_info_wrapper = (TableLayout) findViewById(R.id.email_info_wrapper);
    	for (int i = 0; i < emails.size(); i++){
    		
    		View email_info_cell = layoutInflater.inflate(R.layout.email_info_cell, round_info_wrapper, false);
    		TextView email_txt = (TextView) email_info_cell.findViewById(R.id.email_txt);
    		email_txt.setText(emails.get(i));
			
    		round_info_wrapper.addView(email_info_cell);
    	}
    	
		
	}

}
