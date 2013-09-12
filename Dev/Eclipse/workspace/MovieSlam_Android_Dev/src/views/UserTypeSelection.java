package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import com.example.movieslam_android_dev.R;
import com.facebook.FacebookException;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

public class UserTypeSelection extends FragmentActivity {
	private List<GraphUser> tags;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_type_selection);
		
		Button pickFriendsButton = (Button) findViewById(R.id.friendSelector);
        pickFriendsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPickFriends();
            }
        });
	}
	
	
	
	public void gotoFBFriendSelector(View view){		
		
	}
	
	public void gotoUIDSelection(View view){	
		
		startActivity(new Intent(getApplicationContext(), UidInputPage.class));
	}
	
	public void gotoGenreSelection(View view){	
		Intent intent = new Intent(getApplicationContext(), GenreSelection.class);
		Bundle b = new Bundle();
		b.putString("target_source_type", "random");
		b.putString("target_id", "0");
		intent.putExtras(b);
		startActivity(intent);
	}
	
	private void onClickPickFriends() {
        final FriendPickerFragment fragment = new FriendPickerFragment();

        setFriendPickerListeners(fragment);

        showPickerFragment(fragment);
    }

    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onFriendPickerDone(fragment);
            }
        });
    }
    
    private void onFriendPickerDone(FriendPickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String results = "";

        List<GraphUser> selection = fragment.getSelection();
        tags = selection;
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = getString(R.string.no_friends_selected);
        }

        //showAlert(getString(R.string.you_picked), results);
    }
    
    private void showPickerFragment(PickerFragment<?> fragment) {
        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fpoContainer, fragment)
                .addToBackStack(null)
                .commit();

        //controlsContainer.setVisibility(View.GONE);

        // We want the fragment fully created so we can use it immediately.
        fm.executePendingTransactions();

        fragment.loadData(false);
    }
    
    
    
    public void gotoEmail(View view){
    	
    	// We're going to make up an array of email addresses
        final ArrayList<HashMap<String, String>> addresses = new ArrayList<HashMap<String, String>>();

        // Step through every contact
        final ContentResolver cr = getContentResolver();
        final Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
        {
            final String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            final String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            // Pull out every email address for this particular contact
            final Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, 
                                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            while (emails.moveToNext()) 
            {
                // Add email address to our array
                final String strEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                final HashMap<String, String> email = new HashMap<String, String>();
                email.put("Name", contactName);
                email.put("Email", strEmail);

                addresses.add(email);
            }
            emails.close();
        }

        // Make an adapter to display the list
        SimpleAdapter adapter = new SimpleAdapter(this, addresses, android.R.layout.two_line_list_item,
                                                    new String[] { "Name", "Email" },
                                                    new int[] { android.R.id.text1, android.R.id.text2 });

        // Show the list and let the user pick an email address
        new AlertDialog.Builder(this)
          .setTitle("Select Recipient")
          .setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //EditText e = (EditText)findViewById(R.id.youredittextid);
                HashMap<String, String> email = addresses.get(which);
                //e.setText(email.get("Email"));
                Log.d("debug",email.get("Email"));

              dialog.dismiss();
            }
          }).create().show(); 
        
    	//startActivity(new Intent(getApplicationContext(), EmailSelection.class));
	}
	
	
	public void gotoSms(View view){
		Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(intent, 1);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_type_selection, menu);
		return true;
	}
}
