package com.mxin.android.group_cniao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.core.ISinaInfo;
import com.mxin.android.group_cniao.core.ISinaLogin;
import com.mxin.android.group_cniao.core.WeiboUtils;
import com.mxin.android.group_cniao.entity.UserEvent;
import com.mxin.android.group_cniao.listener.BmobLoginCallback;
import com.mxin.android.group_cniao.listener.MyTextWatch;
import com.mxin.android.group_cniao.nottp.CallServer;
import com.mxin.android.group_cniao.nottp.HttpListner;


import com.mxin.android.group_cniao.utils.BmobManager;
import com.mxin.android.group_cniao.utils.SharedPreferencesUtils;
import com.sina.weibo.sdk.openapi.models.User;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements HttpListner<String> {

    /**
     * tab下划线
     */
    @BindView(R.id.view_line_left)
    View mViewLineLeft;
    @BindView(R.id.view_line_right)
    View mViewLineRight;

    /**
     * 用户名密码输入框
     */
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;

    /**
     * 快速登录电话密码
     */
    @BindView(R.id.et_quick_phone)
    EditText mEtQuickPhone;
    @BindView(R.id.et_quick_code)
    EditText mEtQuickCode;

    @BindView(R.id.ll_register)
    LinearLayout mLlRegister;
    /**
     * 忘记密码
     */
    @BindView(R.id.ll_forget_pwd)
    LinearLayout mLlForgetPwd;

    @BindView(R.id.tv_register)
    TextView mTvRegister;

    /**
     * 账号登陆的布局
     */
    @BindView(R.id.ll_login)
    LinearLayout mLlLogin;
    /**
     * 快速登录的布局
     */
    @BindView(R.id.ll_quick_login)
    LinearLayout mLlQuickLogin;

    /**
     * 快速登录
     */
    @BindView(R.id.tv_quick_register)
    TextView mTvQuickRegister;
    /**
     * 账号登录
     */
    @BindView(R.id.tv_count_register)
    TextView mTvCountRegister;
    @BindView(R.id.btn_get_code)
    Button mBtnGetCode;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    /**
     * 快速登录按钮
     */
    @BindView(R.id.quick_login_btn)
    Button mQuickLoginBtn;
    /** 新浪微博登录**/
    @BindView(R.id.sina_weibo)
    TextView mSinaWeibo;


    /** 封装了 "access_token"，"expires_in"，"refresh_token "，并提供了他们的管理功能  */
 //   private Oauth2AccessToken mAccessToken;
  //  * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
  //  private SsoHandler mSsoHandler;


    /**
     * 账号密码是否为空
     */
    private boolean isUsernameNull = false;
    private boolean isPwdNull = false;
    /**
     * 手机号验证码是否为空
     */
    private boolean isPhoneNull = false;
    private boolean isCodeNull = false;
    /**
     * tab下划线
     */
    private Animation mAnimaRight;
    private Animation mAnimaLeft;
    /**
     * 字体的颜色
     */
    private int mOrrange;
    private int mGray;
    /**
     * 短信验证倒计时
     */
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
        initAnimation();
     //   mSsoHandler = new SsoHandler(LoginActivity.this);
    }

    /**
     * 初始化数据
     */
    private void init() {
        //初始化微博
       WeiboUtils.initWeibo(this);

        mOrrange = getResources().getColor(R.color.orange);
        mGray = getResources().getColor(R.color.content_color);

        mEtQuickPhone.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isPhoneNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mQuickLoginBtn.setEnabled((isPhoneNull && isCodeNull) ? true : false);
            }
        });

        mEtQuickCode.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isCodeNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mQuickLoginBtn.setEnabled((isPhoneNull && isCodeNull) ? true : false);
            }
        });

        mUsername.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isUsernameNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mLoginBtn.setEnabled((isUsernameNull && isPwdNull) ? true : false);
            }
        });

        mPassword.addTextChangedListener(new MyTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                isPwdNull = TextUtils.isEmpty(editable.toString()) ? false : true;
                mLoginBtn.setEnabled((isUsernameNull && isPwdNull) ? true : false);
            }
        });




    }

    /**
     * 发送验证码倒计时
     */

    private void countdownTimer() {
        mBtnGetCode.setEnabled(false);
        mCount = 60;
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCount--;
                        mBtnGetCode.setText(mCount + "");

                        if (mCount <= 0) {
                            mBtnGetCode.setText("重新发送");
                            mBtnGetCode.setEnabled(true);
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    /***
     * 初始化动画
     */
    private void initAnimation() {
        /* tab底部线条往左边移动*/
        mAnimaRight = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_left);
        mAnimaRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvQuickRegister.setTextColor(mOrrange);
                mTvCountRegister.setTextColor(mGray);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mViewLineLeft.setVisibility(View.VISIBLE);
                mViewLineRight.setVisibility(View.INVISIBLE);
                mLlLogin.setVisibility(View.GONE);
                mLlQuickLogin.setVisibility(View.VISIBLE);
                mLlForgetPwd.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        /* tab底部线条往右边移动*/
        mAnimaLeft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_right);
        mAnimaLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvQuickRegister.setTextColor(mGray);
                mTvCountRegister.setTextColor(mOrrange);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewLineLeft.setVisibility(View.INVISIBLE);
                mViewLineRight.setVisibility(View.VISIBLE);
                mLlLogin.setVisibility(View.VISIBLE);
                mLlQuickLogin.setVisibility(View.GONE);
                mLlForgetPwd.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 登录操作
     */
    public void login() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "您的用户名或者密码为空!", Toast.LENGTH_LONG).show();
            return;
        }
        //解析bmob后端的账号密码
        Request<String> request = NoHttp.createStringRequest("https://api.bmob.cn/1/users", RequestMethod.POST);
        //添加头部
        request.addHeader("X-Bmob-Application-Id", "3bb64c8e02ecd42a22f26f0b2a589782");
        request.addHeader("X-Bmob-REST-API-Key", "14fa1d0eb886262ba5175ab70c6f0d4c");

        //添加Body
        String body = "{\"username\"" + ":" + username + "\"password\"" + ":" + password;
        request.setDefineRequestBodyForJson(body);

        CallServer.getRequestInstance().add(LoginActivity.this, 0, request, this, true, true);
    }

    public void quicklogin() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

       /* if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "您的用户名或者密码为空!", Toast.LENGTH_LONG).show();
            return;
        }*/
        //解析bmob后端的账号密码
        Request<String> request = NoHttp.createStringRequest("https://api.bmob.cn/1/users", RequestMethod.POST);
        //添加头部
        request.addHeader("X-Bmob-Application-Id", "3bb64c8e02ecd42a22f26f0b2a589782");
        request.addHeader("X-Bmob-REST-API-Key", "14fa1d0eb886262ba5175ab70c6f0d4c");

        //添加Body
        String body = "{\"username\"" + ":" + username + "\"password\"" + ":" + password;
        request.setDefineRequestBodyForJson(body);

        CallServer.getRequestInstance().add(LoginActivity.this, 0, request, this, true, true);
    }

    /**
     * 登录回调接口是否成功
     * @param what
     * @param response
     */
    @Override
    public void OnSucceed(int what, Response<String> response) {

        switch (what) {
            case 0:
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                finish();
                EventBus.getDefault().post(new UserEvent(mUsername.getText().toString()
                        , mPassword.getText().toString()));
                SharedPreferencesUtils.setParam(LoginActivity.this,"isLogin",true);
                break;
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
        switch (what) {
            case 0:
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    @OnClick({R.id.sina_weibo,R.id.tv_register, R.id.tv_quick_register, R.id.tv_count_register, R.id.btn_get_code, R.id.login_btn, R.id.quick_login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //账号注册
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            //滑动滚动条
            case R.id.tv_quick_register:
                mViewLineRight.startAnimation(mAnimaRight);
                break;
            case R.id.tv_count_register:
                mViewLineLeft.startAnimation(mAnimaLeft);
                break;
            //获取短信验证码
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(mEtQuickPhone.getText().toString())){
                    Toast.makeText(LoginActivity.this,"电话号码不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                BmobManager.getInstance(new BmobLoginCallback() {
                    @Override
                    public void loginFailure() {
                        Toast.makeText(LoginActivity.this,"获取失败！",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void loginSuccess() {
                        Toast.makeText(LoginActivity.this,"发送成功！",Toast.LENGTH_LONG).show();
                        countdownTimer();
                    }
                }).requestSMSCode("13713963614");
                break;
            //登录按钮
            case R.id.login_btn:
                login();
                break;

            case R.id.quick_login_btn:
                login();
                break;
            case R.id.sina_weibo:
                WeiboUtils.loginWeibo(this, new ISinaLogin() {
                    @Override
                    public void weiboLoginSuccess() {
                        WeiboUtils.getWeiboInfo(LoginActivity.this, new ISinaInfo() {
                            @Override
                            public void getWBInfoSuccess(User user) {
                                EventBus.getDefault().post(new UserEvent(user.screen_name,
                                        user.created_at));
                                finish();
                            }

                            @Override
                            public void getWBInfoFailure() {

                            }
                        });

                    }

                    @Override
                    public void weiboLoginFarlure() {

                    }
                });
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //进行反注册eventBus
        EventBus.getDefault().unregister(this);
    }
}
