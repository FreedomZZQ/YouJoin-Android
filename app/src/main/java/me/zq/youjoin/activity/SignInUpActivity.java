package me.zq.youjoin.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;


/**
 * YouJoin
 * A signin screen that offers signin via username/password.
 * Created by ZQ on 2015/11/13.
 */
public class SignInUpActivity extends BaseActivity {

    // UI references.

    @Bind(R.id.username_edit)
    EditText usernameEdit;
    @Bind(R.id.password_edit)
    EditText passwordEdit;
    @Bind(R.id.sign_commit_button)
    Button signCommitButton;
    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.login_form)
    ScrollView loginForm;
    @Bind(R.id.email_edit)
    EditText emailEdit;
    @Bind(R.id.password_confirm_edit)
    EditText passwordConfirmEdit;
    @Bind(R.id.username_signup_form)
    LinearLayout usernameSignupForm;

    private Boolean isSignIn;

    private static Activity welcomeActivity;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private SignTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_signin);
        ButterKnife.bind(this);
        // Set up the login form.

        isSignIn = getIntent().getBooleanExtra("isSignIn", false);
        if (isSignIn) {
            usernameSignupForm.setVisibility(View.INVISIBLE);
        }

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSign();
                    return true;
                }
                return false;
            }
        });

        signCommitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSign();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSign() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        usernameEdit.setError(null);
        passwordEdit.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirmPassword = passwordConfirmEdit.getText().toString();
        String email = emailEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            passwordEdit.setError(getString(R.string.error_invalid_password));
            focusView = passwordEdit;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            usernameEdit.setError(getString(R.string.error_field_required));
            focusView = usernameEdit;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameEdit.setError(getString(R.string.error_invalid_username));
            focusView = usernameEdit;
            cancel = true;
        }

        if(!isSignIn){
            passwordConfirmEdit.setError(null);
            emailEdit.setError(null);

            if(TextUtils.isEmpty(email)){
                emailEdit.setError(getString(R.string.error_field_required));
                focusView = emailEdit;
                cancel = true;
            } else if(!isEmailValid(email)){
                emailEdit.setError(getString(R.string.error_invalid_email));
                focusView = emailEdit;
                cancel = true;
            }

            if(!confirmPassword.equals(password)){
                passwordConfirmEdit.setError(getString(R.string.error_invalid_confirm_password));
                focusView = passwordConfirmEdit;
                cancel = true;
            }

        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //mAuthTask = new SignTask(username, password, email);
            //mAuthTask.execute((Void) null);
            actionSign(username, password, email);
        }
    }

    private void actionSign(String username, String password, String email){
        if(isSignIn){
            NetworkManager.postSignIn(username, password, new ResponseListener<UserInfo>() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG, "Signin error! " + volleyError.toString());
                    signFailure(volleyError.toString());
                }

                @Override
                public void onResponse(UserInfo userInfo) {
                    if (userInfo.getResult().equals("success")) {
                        Log.d(TAG, "Signin Success! username is : " + userInfo.getUsername());
                        signSuccess();
                    } else {
                        Log.d(TAG, "Signin Failure! username is : " + userInfo.getUsername());
                        signFailure(userInfo.getResult());
                    }
                }
            });
        }else {
            NetworkManager.postSignUp(username, password, email, new ResponseListener<UserInfo>() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG, "Signup error!" + volleyError.toString());
                    signFailure(volleyError.toString());
                }

                @Override
                public void onResponse(UserInfo userInfo) {
                    if (userInfo.getResult().equals("success")) {
                        Log.d(TAG, "Signup Success! username is : " + userInfo.getUsername());
                        signSuccess();
                    } else {
                        Log.d(TAG, "Signup Failure! username is : " + userInfo.getUsername());
                        signFailure(userInfo.getResult());
                    }
                }
            });
        }
    }

    private void signSuccess(){
        showProgress(false);

        MainActivity.actionStart(SignInUpActivity.this);
        SignInUpActivity.this.finish();
        welcomeActivity.finish();

    }

    private void signFailure(String error){
        showProgress(false);
        //passwordEdit.setError(getString(R.string.error_incorrect_password));
        passwordEdit.setError(error);
        passwordEdit.requestFocus();
    }

    private boolean isUsernameValid(String username) {
        return username.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SignTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;
        private final String email;

        SignTask(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(isSignIn){
                NetworkManager.postSignIn(username, password, new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Signin error! " + volleyError.toString());
                    }

                    @Override
                    public void onResponse(UserInfo userInfo) {
                        if (userInfo.getResult().equals("success")) {
                            Log.d(TAG, "Signin Success! username is : " + userInfo.getUsername());
                        } else {
                            Log.d(TAG, "Signin Failure! username is : " + userInfo.getUsername());

                        }
                    }
                });
            }else {
                NetworkManager.postSignUp(username, password, email, new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Signup error!" + volleyError.toString());
                    }

                    @Override
                    public void onResponse(UserInfo userInfo) {
                        Log.d(TAG, "Signup Success! username is: " + userInfo.getUsername());
                    }
                });
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                MainActivity.actionStart(SignInUpActivity.this);
                SignInUpActivity.this.finish();
                welcomeActivity.finish();
            } else {
                passwordEdit.setError(getString(R.string.error_incorrect_password));
                passwordEdit.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public static void actionStart(Activity welcomeContext, Boolean isSignIn) {
        Intent intent = new Intent(welcomeContext, SignInUpActivity.class);
        intent.putExtra("isSignIn", isSignIn);
        welcomeContext.startActivity(intent);
        welcomeActivity = welcomeContext;
    }
}

