package com.launcher.mummu.loaderexam;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    public static String DISPLAY_PHOTO = ContactsContract.Contacts.PHOTO_URI;
    public static Uri EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    public static String EMAIL_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    public static String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;
    public static String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    public static String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public static Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//    public static String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    public static String STARRED_CONTACT = ContactsContract.Contacts.STARRED;
    public String CONTACT_ID = ContactsContract.Contacts._ID;
    String[] projection = new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER, STARRED_CONTACT, DISPLAY_PHOTO};
    private ListView mRecyclerView;
    private Uri QUERY_URI = ContactsContract.Contacts.CONTENT_URI;
    private String mCurFilter;
    private CursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (ListView) findViewById(R.id.recyclerView);


        Cursor cursor = getContentResolver().query(QUERY_URI, projection, null, null, null);
        adapter = new CursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mRecyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurFilter));
        } else {
            baseUri = QUERY_URI;
        }
        String select = "((" + DISPLAY_NAME + " NOTNULL) AND ("
                + HAS_PHONE_NUMBER + "=1) AND ("
                + DISPLAY_NAME + " != '' ))";

        return new CursorLoader(this, baseUri,
                projection, select, null,
                DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }
}
