package views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import models.Gameplay;
import tools.FriendPickerApplication;
import views.component.PickFriendsActivity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.example.movieslam_android_dev.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class UserTypeSelection extends FragmentActivity {
	
//	private List<GraphUser> tags;
	private static final int PICK_FRIENDS_ACTIVITY = 1;
	static final int PICK_CONTACT = 2;
	
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_type_selection);        
        
        lifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChanged(session, state, exception);
            }
        });
        lifecycleHelper.onCreate(savedInstanceState);

        if (Gameplay.get_fbConnected() == 0){
        	Button friendSelector_btn = (Button) this.findViewById(R.id.friendSelector_btn);
        	friendSelector_btn.setEnabled(false);
        	friendSelector_btn.setBackgroundResource(R.drawable.button_small_disabled);
        	
        	Button userSelector_btn = (Button) this.findViewById(R.id.userSelector_btn);
        	userSelector_btn.setEnabled(false);
        	userSelector_btn.setBackgroundResource(R.drawable.button_small_disabled);
        	
        	Button emailInvite_btn = (Button) this.findViewById(R.id.emailInvite_btn);
        	emailInvite_btn.setEnabled(false);
        	emailInvite_btn.setBackgroundResource(R.drawable.button_small_disabled);
        	
        	Button smsInvite_btn = (Button) this.findViewById(R.id.smsInvite_btn);
        	smsInvite_btn.setEnabled(false);
        	smsInvite_btn.setBackgroundResource(R.drawable.button_small_disabled);
        }
        //ensureOpenSession();
	}
	
	public void gotoFIDSelection(View view){	
		onClickPickFriends();
	}
	
	private boolean ensureOpenSession() {
        if (Session.getActiveSession() == null ||
                !Session.getActiveSession().isOpened()) {
            Session.openActiveSession(this, true, new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    onSessionStateChanged(session, state, exception);
                }
            });
            return false;
        }
        return true;
    }

    private void onSessionStateChanged(Session session, SessionState state, Exception exception) {
        if (pickFriendsWhenSessionOpened && state.isOpened()) {
            pickFriendsWhenSessionOpened = false;

            startPickFriendsActivity();
        }
    }

    private void displaySelectedFriends(int resultCode) {
        String name = "";
        String fid = "";
        
        FriendPickerApplication application = (FriendPickerApplication) getApplication();

        Collection<GraphUser> selection = application.getSelectedUsers();
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> fids = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
                fids.add(user.getId());
            }
            name = TextUtils.join(", ", names);
            fid  = TextUtils.join(", ", fids);           
            
            // go to genre selection page if fb friend is selected
            Intent intent = new Intent(getApplicationContext(), GenreSelection.class);
            Gameplay.setChallOppoFID(fid);
            Gameplay.setOppoFName(name);
            Gameplay.setOppoLName(name);
    		Bundle b = new Bundle();
    		b.putString("target_source_type", "fid");
    		b.putString("target_id", fid);
    		intent.putExtras(b);
    		startActivity(intent);
        }       
    }
	
	private void onClickPickFriends() {
        startPickFriendsActivity();
    }

    private void startPickFriendsActivity() {
        if (ensureOpenSession()) {
            FriendPickerApplication application = (FriendPickerApplication) getApplication();
            application.setSelectedUsers(null);

            Intent intent = new Intent(this, PickFriendsActivity.class);
            // Note: The following line is optional, as multi-select behavior is the default for
            // FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
            // friend picker if single-select functionality was desired, or if a different user ID was
            // desired (for instance, to see friends of a friend).
            PickFriendsActivity.populateParameters(intent, null, false, true);
            startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
        } else {
            pickFriendsWhenSessionOpened = true;
        }
    }
    /*
    @Override
    protected void onStart() {
        super.onStart();

        // Update the display every time we are started.
        displaySelectedFriends(RESULT_OK);
    }
    */
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FRIENDS_ACTIVITY:
                displaySelectedFriends(resultCode);
                break;
            case PICK_CONTACT:
            	Uri contactUri = data.getData();
                ContentResolver resolver = getContentResolver();
                long contactId = -1;
                String player_name = null;
                String player_number = null;

                // get display name from the contact
                Cursor cursor = resolver.query( contactUri, new String[] { Contacts._ID, Contacts.DISPLAY_NAME }, null, null, null );
                if(cursor.moveToFirst()){
                    contactId = cursor.getLong(0);
                    player_name = cursor.getString(1);
                    cursor = resolver.query(Phone.CONTENT_URI, new String[] { Phone.TYPE, Phone.NUMBER }, Phone._ID + "=" + contactId, null, null);
                }
                player_number = cursor.moveToNext() ? cursor.getString(1) : null;
                // launch sms app                
                Uri uri = Uri.parse("smsto:"+player_number);
        		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        		intent.putExtra("sms_body", "Hello "+player_name);  
        		startActivity(intent);
         	   break;
            default:
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                break;
        }
    }	
	
	
	public void gotoUIDSelection(View view){	
		
		startActivity(new Intent(getApplicationContext(), UidInputPage.class));
	}
	
	public void gotoGenreSelection(View view){
		Gameplay.setChallID("0");
		Gameplay.setChallType("self");
		Intent intent = new Intent(getApplicationContext(), GenreSelection.class);
		Bundle b = new Bundle();
		b.putString("target_source_type", "random");
		b.putString("target_id", "0");
		intent.putExtras(b);
		startActivity(intent);
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
                
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email.get("Email")});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Let's play Movie Slam!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Hey "+email.get("Email")+",\n\n I want to play Movie Slam! Click here to download from the iTunes App STore or play on Facebook!"));
                emailIntent.setType("text/plain");
                startActivity(Intent.createChooser(emailIntent, "Send a mail ..."));
                

              dialog.dismiss();
            }
          }).create().show(); 
        
    	//startActivity(new Intent(getApplicationContext(), EmailSelection.class));
	}
	
	
	public void gotoSms(View view){
		Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(intent, PICK_CONTACT);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_type_selection, menu);
		return true;
	}
}
