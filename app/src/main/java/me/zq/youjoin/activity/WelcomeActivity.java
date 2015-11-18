package me.zq.youjoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;

public class WelcomeActivity extends BaseActivity
            implements View.OnClickListener{

    @Bind(R.id.choose_signin_button)
    Button chooseSigninButton;
    @Bind(R.id.choose_signup_button)
    Button chooseSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_welcome);
        ButterKnife.bind(this);

        chooseSigninButton.setOnClickListener(this);
        chooseSignupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        boolean isSignIn = false;
        switch (view.getId()){
            case R.id.choose_signin_button:
                isSignIn = true;
                break;
            case R.id.choose_signup_button:
                isSignIn = false;
                break;
            default:
                break;
        }

        SignInUpActivity.actionStart(this, isSignIn);
    }
}
