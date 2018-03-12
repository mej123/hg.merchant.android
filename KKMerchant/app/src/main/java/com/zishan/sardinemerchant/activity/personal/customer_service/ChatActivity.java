package com.zishan.sardinemerchant.activity.personal.customer_service;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.ui.BaseActivity;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.easeui.util.Config;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.MainPageActivity;
import com.zishan.sardinemerchant.fragment.personal.CustomChatFragment;
import com.zishan.sardinemerchant.utils.MessageHelper;

public class ChatActivity extends BaseActivity {

    protected CustomChatFragment chatFragment = null;
    protected String toChatUsername = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //IM服务号
        toChatUsername = bundle.getString(Config.EXTRA_SERVICE_IM_NUMBER);

        //可以直接new ChatFragment使用
        String chatFragmentTAG = "chatFragment";
        chatFragment = (CustomChatFragment) getSupportFragmentManager().findFragmentByTag(chatFragmentTAG);
        if (chatFragment == null) {
            chatFragment = new CustomChatFragment();
            //传入参数
            chatFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment, chatFragmentTAG).commit();
        }
        ChatClient.getInstance().chatManager().bindChatUI(toChatUsername);

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();

        if (CommonUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(Config.EXTRA_SERVICE_IM_NUMBER);
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {

            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    //    private ProgressDialog progressDialog;
//    private boolean progressShow;
    public static void createRandomAccountThenLoginChatServer(final Activity context) {
        // 自动生成账号,此处每次都随机生成一个账号,为了演示.正式应从自己服务器获取账号
        final String account = "18767101271";//Preferences.getInstance().getUserName();
        final String userPwd = "wislie1123";//Constant.DEFAULT_ACCOUNT_PWD;

//        progressDialog = getProgressDialog();
//        progressDialog.setMessage("系统正在自动为您注册用户…");//getString(R.string.system_is_regist)
//        progressDialog.show();
        // createAccount to huanxin server
        // if you have a account, this step will ignore
        ChatClient.getInstance().createAccount(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                Log.e("wislie", "demo register success");
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器
                        login(context, account, userPwd);
                    }
                });
            }

            @Override
            public void onError(final int errorCode, String error) {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                       /* if(progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }*/
                        if (errorCode == Error.NETWORK_ERROR) {
                            Toast.makeText(ClientApplication.getApp(), "网络连接不可用，请检查网络", Toast.LENGTH_SHORT).show();//R.string.network_unavailable
                        } else if (errorCode == Error.USER_ALREADY_EXIST) {
                            login(context, account, userPwd);
//                            Toast.makeText(ClientApplication.getApp(), "用户已经存在", Toast.LENGTH_SHORT).show(); //R.string.user_already_exists
                        } else if (errorCode == Error.USER_AUTHENTICATION_FAILED) {
                            Toast.makeText(ClientApplication.getApp(), "无开放注册权限", Toast.LENGTH_SHORT).show(); //R.string.no_register_authority
                        } else if (errorCode == Error.USER_ILLEGAL_ARGUMENT) {
                            Toast.makeText(ClientApplication.getApp(), "用户名不合法", Toast.LENGTH_SHORT).show();//R.string.illegal_user_name
                        } else {
                            Toast.makeText(ClientApplication.getApp(), "注册失败!", Toast.LENGTH_SHORT).show();//R.string.register_user_fail
                        }
//                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }


    private static void login(final Activity context, final String uname, final String upwd) {
     /*   progressShow = true;
        progressDialog = getProgressDialog();
        progressDialog.setMessage("正在联系客服，请稍等…");//getResources().getString(R.string.is_contact_customer)
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }*/


        if (ChatClient.getInstance().isLoggedInBefore()) {


            toChatActivity(context);
            return;
        }

        // login huanxin server
        ChatClient.getInstance().login(uname, upwd, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("wislie", "demo login success!");
               /* if (!progressShow) {
                    return;
                }*/


                toChatActivity(context);
            }

            @Override
            public void onError(int code, String error) {
                Log.e("wislie", "login fail,code:" + code + ",error:" + error);
                /*if (!progressShow) {
                    return;
                }*/
                context.runOnUiThread(new Runnable() {
                    public void run() {
//                        progressDialog.dismiss();
                        Toast.makeText(context,
                                "联系客服失败:",
//                                getResources().getString(R.string.is_contact_customer_failure_seconed),
                                Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    private static void toChatActivity(Activity context) {

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (!getActivity().isFinishing())
//                    progressDialog.dismiss();

        //此处演示设置技能组,如果后台设置的技能组名称为[shouqian|shouhou],这样指定即分配到技能组中.
        //为null则不按照技能组分配,同理可以设置直接指定客服scheduleAgent
        String queueName = null;
        queueName = "shouhou";
               /* switch (messageToIndex){
                    case Constant.MESSAGE_TO_AFTER_SALES:
                        queueName = "shouhou";
                        break;
                    case Constant.MESSAGE_TO_PRE_SALES:
                        queueName = "shouqian";
                        break;
                    default:
                        break;
                }*/
        Bundle bundle = new Bundle();
//                bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, selectedIndex);
        //设置点击通知栏跳转事件
        Conversation conversation = ChatClient.getInstance().chatManager().getConversation("\n" +
                "\n" +
                "kefuchannelimid_656399");//Preferences.getInstance().getCustomerAccount()
        String titleName = null;
        if (conversation.getOfficialAccount() != null) {
            titleName = conversation.getOfficialAccount().getName();
        }
        // 进入主页面

        Intent intent = new IntentBuilder(context)
                .setTargetClass(ChatActivity.class)
                .setVisitorInfo(MessageHelper.createVisitorInfo())
                .setServiceIMNumber("kefuchannelimid_656399")//Preferences.getInstance().getCustomerAccount()
//                .setScheduleQueue(MessageHelper.createQueueIdentity(queueName))
//                .setTitleName(titleName)
                .setTitleName("客服")
//						.setScheduleAgent(MessageHelper.createAgentIdentity("ceshiok1@qq.com"))
                .setShowUserNick(true)
//                .setBundle(bundle)
                .build();
        context.startActivity(intent);
//                finish();

//            }
//        });
    }

}
