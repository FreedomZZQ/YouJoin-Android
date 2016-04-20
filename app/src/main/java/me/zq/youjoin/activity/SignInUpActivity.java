package me.zq.youjoin.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.event.SigninSuccessEvent;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.StringUtils;


/**
 * YouJoin
 * A signin screen that offers signin via username/password.
 * Created by ZQ on 2015/11/13.
 */
public class SignInUpActivity extends BaseActivity
implements DataPresenter.SignIn, DataPresenter.SignUp{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_signin);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void attemptSign() {

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
            DataPresenter.signIn(username, password, SignInUpActivity.this);
        }else {
            DataPresenter.signUp(username, password, email, SignInUpActivity.this);
        }
    }

    @Override
    public void onSign(UserInfo info){

        if(info.getResult().equals(NetworkManager.SUCCESS)){
            YouJoinApplication.setCurrUser(info);
        }else{
            //passwordEdit.setError(getString(R.string.error_incorrect_password));
            passwordEdit.setError(getString(R.string.error_network));
            passwordEdit.requestFocus();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SigninSuccessEvent event){
        showProgress(false);
        MainActivity.actionStart(SignInUpActivity.this);
        SignInUpActivity.this.finish();
        welcomeActivity.finish();
    }

    private boolean isUsernameValid(String username) {
        return username.length() > 2 && StringUtils.isLetter(username.charAt(0));
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    private boolean isEmailValid(String email){
        return StringUtils.isEmailAddress(email);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

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
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void actionStart(Activity welcomeContext, Boolean isSignIn) {
        Intent intent = new Intent(welcomeContext, SignInUpActivity.class);
        intent.putExtra("isSignIn", isSignIn);
        welcomeContext.startActivity(intent);
        welcomeActivity = welcomeContext;
    }
}

