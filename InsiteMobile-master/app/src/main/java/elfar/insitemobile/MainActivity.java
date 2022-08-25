package elfar.insitemobile;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

//import com.vansuita.pickimage.bean.PickResult;
//import com.vansuita.pickimage.bundle.PickSetup;
//import com.vansuita.pickimage.dialog.PickImageDialog;
//import com.vansuita.pickimage.listeners.IPickResult;

import elfar.insitemobile.Settings.PreferenceActivity;
import elfar.insitemobile.Tabs.Actions;
import elfar.insitemobile.Tabs.Disables;
import elfar.insitemobile.Tabs.Emergency;
import elfar.insitemobile.Tabs.EventTable;

public class MainActivity extends AppCompatActivity implements Communicator/*,IPickResult*/ {

    public static FragmentManager fragmentManager;
    PowerManager.WakeLock wl;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShowUserAgreement();
        setContentView(R.layout.activity_main);

        AppManager AppManagerIns = new AppManager(this); //Here the context is passing
        fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.MainActivityToolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.vpTabsContainer);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.HomeTabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        turnOffDozeMode(getApplicationContext());
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "insitemobile:MyWakeLock");
        wl.acquire();

        //Start foreground service
        startService();
        final ScreenActionReceiver screenactionreceiver = new ScreenActionReceiver();
        registerReceiver(screenactionreceiver, screenactionreceiver.getFilter());

        AppManager.ConnectAllStations();
        Log.i("InsiteMobileLog", "App started from onCreate");
        AppManager.appendLogz("App started from onCreate");

        //Catching all exceptions and writing them to log
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                //Catch your exception
                // Without System.exit() this will not work.
                AppManager.appendLogz("Unhandeled exception: " + paramThrowable.getMessage());
                System.exit(2);
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("InsiteMobileLog", "onPause()");
        AppManager.appendLogz("onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("InsiteMobileLog", "onStop()");
        AppManager.appendLogz("onStop()");
    }

    @Override
    public void onDestroy() {
        Log.i("InsiteMobileLog", "onDestroy()");
        AppManager.appendLogz("onDestroy()");
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("InsiteMobileLog", "onResume()");
        AppManager.appendLogz("onResume()");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.i("InsiteMobileLog", "onRestart()");
        AppManager.appendLogz("onRestart()");
    }

    public void startService() {

        Intent serviceIntent = new Intent(this, AppService.class);
        serviceIntent.putExtra("inputExtra", "InsiteMobileService");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, AppService.class);
        stopService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //When using dialog fragment
            /*FragmentManager fm = getSupportFragmentManager();
            SettingsPasswordDialog dialogFragment = new SettingsPasswordDialog();
            dialogFragment.show(fm, "Sample Fragment");*/
            //Open keyboard
            //InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            //Using alertDialog
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            final EditText txtPassword = new EditText(MainActivity.this);
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            alert.setMessage("You have to enter a password to get to the settings page");
            alert.setTitle("Password");
            alert.setView(txtPassword);

            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputPassword = txtPassword.getText().toString();
                    if (inputPassword.equals(GlobalInfo.getSettingsPassword())) {
                        Intent i = new Intent(MainActivity.this, PreferenceActivity.class);
                        startActivity(i);
                    } else {
                        txtPassword.setText("");
                        Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
            alert.show();
            return true;
        }

        /*if (id == R.id.action_uploadPhoto) {
            PickImageDialog.build(new PickSetup()).show(this);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    //Not relevant anymore because of use of AlertDialog
    //Getting data from SettingsPasswordDialog
    public void RespondSettingsPassword(String password) {
        Intent i = new Intent(this, PreferenceActivity.class);
        startActivity(i);
    }

    public void turnOffDozeMode(Context context) {  //you can use with or without passing context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            /*if (pm.isIgnoringBatteryOptimizations(packageName)) // if you want to desable doze mode for this package
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);*/
            /*else { */// if you want to enable doze mode
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            /*}*/
            try {
                context.startActivity(intent);
            } catch (Exception e) {

            }
        }
    }

    public void ShowUserAgreement() {
        Boolean isFirstRun = getSharedPreferences("UserAgreement", MODE_PRIVATE).getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(MainActivity.this, UserAgreementActivity.class));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_table, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    //Setting tabs
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    EventTable eventTable = new EventTable();
                    return eventTable;
                case 2:
                    Actions actions = new Actions();
                    return actions;
                case 1:
                    Disables disables = new Disables();
                    return disables;
                case 3:
                    Emergency emergency = new Emergency();
                    return emergency;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }


    }

    //Get the picture to upload
//    @Override
//    public void onPickResult(PickResult r) {
//        if (r.getError() == null) {
//            //If you want the Uri.
//            //Mandatory to refresh image from Uri.
//            //getImageView().setImageURI(null);
//
//            //Setting the real returned image.
//            //getImageView().setImageURI(r.getUri());
//
//            //If you want the Bitmap.
//            //getImageView().setImageBitmap(r.getBitmap());
//
//            //Image path
//            //r.getPath();
//        } else {
//            //Handle possible errors
//            //TODO: do what you have to do with r.getError();
//            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }

}
