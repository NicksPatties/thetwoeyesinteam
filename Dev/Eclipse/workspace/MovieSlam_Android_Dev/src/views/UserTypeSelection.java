package views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_type_selection, menu);
		return true;
	}
}
