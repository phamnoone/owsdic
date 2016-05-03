package com.example.hongb_000.dictionaryows.PII;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hongb_000.dictionaryows.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by USER on 7/15/2015.
 */
public class LogIn extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private static final String FIREBASE_URL = "https://androiddictionary.firebaseio.com";
    private Firebase mFirebaseRef,mFire;
    private String mName;
    private String pass;
    private ValueEventListener mConnectedListener;
    private int x=0;
    private String from;
    EditText txtemail;
    EditText txtpass;
    Button logIn,signUp;
    private Toolbar mToolbar;
    int j=0;
    private ListUsersAdapter listUsersAdapter;

    private static final String TAG = LogIn.class.getSimpleName();

    /* *************************************
     *              GENERAL                *
     ***************************************/
    /* TextView that is used to display information about the logged in user */
    private TextView mLoggedInStatusTextView;

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebase;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    private Button bntView;
    private  String url;
    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    /* The login button for Facebook */
    private LoginButton mFacebookLoginButton;
    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */
    private AccessTokenTracker mFacebookAccessTokenTracker;

    private ProfilePictureView profPict;


    /* *************************************
     *              GOOGLE                 *
     ***************************************/
    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that HistoryActivity PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    private boolean mGoogleLoginClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can resolve them when the user clicks
     * sign-in. */
    private ConnectionResult mGoogleConnectionResult;

    /* The login button for Google */
    private SignInButton mGoogleLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.pii_login1);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Mạng xã hội hỏi từ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        android.support.v7.app.ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFB567")));
        // Setup our Firebase mFirebaseRef

        Firebase.setAndroidContext(LogIn.this);
        mFirebaseRef = new Firebase(FIREBASE_URL).child("Users");
        signUp=(Button)findViewById(R.id.bntSignUpMain);
        logIn=(Button)findViewById(R.id.bntLogin);
        txtemail=(EditText)findViewById(R.id.txtEmailLogin);
        txtpass=(EditText)findViewById(R.id.txtPassLogin);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });

        printKeyHash();
        /* *************************************
         *              FACEBOOK               *
         ***************************************/
        /* Load the Facebook login button and set up the tracker to monitor access token changes */
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.login_with_facebook);
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                LogIn.this.onFacebookAccessTokenChange(currentAccessToken);
            }
        };


         /* *************************************
         *               GOOGLE                *
         ***************************************/
        /* Load the Google login button */
        mGoogleLoginButton = (SignInButton) findViewById(R.id.login_with_google);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleLoginClicked = true;
                if (!mGoogleApiClient.isConnecting()) {
                    if (mGoogleConnectionResult != null) {
                        resolveSignInError();
                    } else if (mGoogleApiClient.isConnected()) {
                        getGoogleOAuthTokenAndLogin();
                    } else {
                    /* connect API now */
                        Log.d(TAG, "Trying to connect to Google API");
                        mGoogleApiClient.connect();
                    }
                }
            }
        });
        /* Setup the Google API object to allow Google+ logins */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        /* *************************************
         *               GENERAL               *
         ***************************************/


        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebase= new Firebase(getResources().getString(R.string.firebase_url));

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Kiểm tra");
        mAuthProgressDialog.setMessage("Đang kết nối..");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFirebase.addAuthStateListener(mAuthStateListener);
    }

    private void SignUp() {
        Intent intent=new Intent(LogIn.this,SignUp.class);
        Bundle bundle=new Bundle();
        bundle.putString("URL", FIREBASE_URL);
        intent.putExtra("DATA", bundle);
        startActivity(intent);
    }

    private void LogIn(){
        for (int i=0;i<listUsersAdapter.getCount();i++){
            User user=(User) listUsersAdapter.getItem(i);
            if (txtemail.getText().toString().equals(user.getEmail())&&
                    txtpass.getText().toString().equals(user.getPass())) {
                x=x+1;
                from=user.getEmail();
                mName=user.getName();
            }
        }
        if (x!=0){
            Intent intent=new Intent(LogIn.this,Home.class);
            Bundle bundle=new Bundle();
            bundle.putString("from",from);
            bundle.putString("URL",FIREBASE_URL);
            bundle.putString("mName",mName);
            intent.putExtra("DATA", bundle);
            startActivity(intent);
        }
        else {
            new AlertDialog.Builder(LogIn.this).setTitle("Xảy ra lỗi")
                    .setMessage("Nếu chắc chắn rằng email và mật khẩu của bạn chính xác, hãy kiểm tra lại kết nối mạng!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }
        txtemail.setText("");
        txtpass.setText("");
        x=0;
    }
    @Override
    public void onStart() {
        super.onStart();
        listUsersAdapter=new ListUsersAdapter(mFirebaseRef,this,R.layout.pii_user);
        // Finally, HistoryActivity little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    //Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
//                    new AlertDialog.Builder(LogIn.this).setTitle("Lỗi kết nối").setMessage("Kiểm tra lại kết nối mạng!")
//                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            }).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }


    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.firebase.samples.logindemo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                System.out.println("KeyHash"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
            System.out.println("KeyHash"+e.toString());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("KeyHash"+e.toString());
            Log.d("KeyHash:", e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }

        // if changing configurations, stop tracking firebase session.
        mFirebase.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
        if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was HistoryActivity request by the Google API */
            if (resultCode != RESULT_OK) {
                mGoogleLoginClicked = false;
            }
            mGoogleIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }

        } else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If HistoryActivity user is currently authenticated, display HistoryActivity logout menu */
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }



    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebase.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.mAuthData.getProvider().equals("facebook")||this.mAuthData.getProvider().equals("google")) {
                /* Logout from Facebook */
                LoginManager.getInstance().logOut();
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }


    /**
     * Once HistoryActivity user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            String name = null;
            String email=null, uid=null,id=null,proId=null;
//            profPict=(ProfilePictureView)findViewById(R.id.avatar);

            if (authData.getProvider().equals("facebook")|| authData.getProvider().equals("google")) {
                id=(String)authData.getProviderData().get("id");
                name = (String) authData.getProviderData().get("displayName");
                uid=(String)authData.getUid();
//                profPict.setProfileId(id);

            }  else {
                Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
//                mFire=new Firebase(FIREBASE_URL).child("Users");
//                Query query=mFire.orderByChild("email").equalTo(uid);
//                query.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        j++;
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });

                for (int i=0;i<listUsersAdapter.getCount();i++){
                    User user=(User) listUsersAdapter.getItem(i);
                    if (uid.equals(user.getEmail().toString())) {
                        j=j+1;
                    }
                }
                from=uid;
                mName=name;
                new AlertDialog.Builder(LogIn.this).setTitle("Đăng nhập với tài khoản "+authData.getProvider()).setMessage("Tiếp tục với tên:"+name)
                        .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (j!=0){
                                    Intent intent=new Intent(LogIn.this,Home.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("from",from);
                                    bundle.putString("URL",FIREBASE_URL);
                                    bundle.putString("mName",mName);
                                    intent.putExtra("DATA", bundle);
                                    startActivity(intent);
                                }
                                else {
                                    User user=new User(from, mName,"","");
                                    mFirebaseRef.push().setValue(user);
                                    Intent intent1=new Intent(LogIn.this,Home.class);
                                    Bundle bundle1=new Bundle();
                                    bundle1.putString("from", from);
                                    bundle1.putString("URL",FIREBASE_URL);
                                    bundle1.putString("mName", mName);
                                    intent1.putExtra("DATA",bundle1);
                                    startActivity(intent1);
                                }
                                logout();
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                                dialog.cancel();
                            }
                        }).show();

            }
        }
        this.mAuthData = authData;
        supportInvalidateOptionsMenu();
    }

    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mAuthProgressDialog.show();
            mFirebase.authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));
        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do HistoryActivity logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                mFirebase.unauth();
                setAuthenticatedUser(null);
            }
        }
    }


    /* ************************************
    *              GOOGLE                *
    **************************************
    */
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        mAuthProgressDialog.show();
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(LogIn.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    mFirebaseRef.authWithOAuthToken("google", token, new AuthResultHandler("google"));
                } else if (errorMessage != null) {
                    mAuthProgressDialog.hide();
                    showErrorDialog(errorMessage);
                }
            }
        };
        task.execute();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        getGoogleOAuthTokenAndLogin();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
