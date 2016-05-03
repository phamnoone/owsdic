package com.example.hongb_000.dictionaryows;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.NavigationDrawer.FragmentDrawer;
import com.example.hongb_000.dictionaryows.PII.LogIn;
import com.example.hongb_000.dictionaryows.PIII.MainTestActivity;
import com.example.hongb_000.dictionaryows.PIV.SettingActivity;
import com.example.hongb_000.dictionaryows.PVII.HelpActivity;
import com.example.hongb_000.dictionaryows.Search.SearchFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolBar;

    @InjectView(R.id.title)
    TextView mTVTitle;

    private SearchView mSearchView;
    private SearchFragment mSearchFragment;
    private FragmentDrawer drawerFragment;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.inject(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolBar);
        drawerFragment.setDrawerListener(this);
        displayView(0);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (i3 > i7 || i3 < i7) {
                    if (SearchFragment.actionMenu != null && SearchFragment.actionMenu.isOpen()) {
                        SearchFragment.actionMenu.close(true);
                    }
                }
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTVTitle.setText("Tìm Kiếm");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setQueryHint("Nhập từ tìm kiếm");

        EditText searchPlate = ((EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchPlate.setBackgroundResource(R.drawable.underlinetheme_edit_text_holo_light);
        searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchPlate.setTextColor(getResources().getColor(R.color.textColorPrimary));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    if (mSearchFragment != null) {
                        mSearchFragment.setQuery(newText);
                    }

                }
                return false;
            }
        });

        mTVTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
                MenuItemCompat.expandActionView(menu.findItem(R.id.action_search));
                mSearchView.requestFocus();
                InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(mSearchView, 0);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify HistoryActivity parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_search) {
            //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                mSearchFragment = new SearchFragment();
                fragment = mSearchFragment;
                break;
            case 1:
                Intent intentLogIn = new Intent(MainActivity.this, LogIn.class);
                startActivity(intentLogIn);
                break;
            case 2:
                // kiem tra kanji
                startActivity(new Intent(MainActivity.this, MainTestActivity.class));

                break;
            case 3:
                // cai dat
                startActivity(new Intent(MainActivity.this, SettingActivity.class));

                break;
            case 4:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (SearchFragment.actionMenu != null && SearchFragment.actionMenu.isOpen()) {
            SearchFragment.actionMenu.close(true);
        }
        new AlertDialog.Builder(MainActivity.this).setTitle("Xác nhận").setMessage("Bạn thực sự muốn thoát chương trình?")
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        MainActivity.this.finish();
                    }
                })
                .setPositiveButton("Trở lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

}

