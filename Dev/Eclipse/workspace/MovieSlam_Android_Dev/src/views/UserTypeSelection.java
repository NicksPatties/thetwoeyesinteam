package views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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
    	startActivity(new Intent(getApplicationContext(), EmailSelection.class));
	}
	
	
	public void gotoSms(View view){
		ArrayList<String> alContacts = new ArrayList<String>();
		
		ContentResolver contResv = getContentResolver();
		Cursor cursor = contResv.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if(cursor.moveToFirst())
		{
		    
		    do
		    {
		        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

		        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
		        {
		            Cursor pCur = contResv.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
		            while (pCur.moveToNext()) 
		            {
		                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                alContacts.add(contactNumber);
		                break;
		            }
		            pCur.close();
		        }

		    } while (cursor.moveToNext()) ;
		}
		
		for (int i=0; i< alContacts.size(); i++){
			Log.d("debug", alContacts.get(i));
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_type_selection, menu);
		return true;
	}
}
