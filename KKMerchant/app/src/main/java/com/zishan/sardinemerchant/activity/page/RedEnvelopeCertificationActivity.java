package com.zishan.sardinemerchant.activity.page;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wislie.rxjava.UserConfig;
import com.example.wislie.rxjava.model.personal.OrderAttachmentEntity;
import com.example.wislie.rxjava.model.personal.StoreOrderEntity;
import com.example.wislie.rxjava.presenter.base.personal.StoreOrderPresenter;
import com.example.wislie.rxjava.util.NetworkUtil;
import com.example.wislie.rxjava.view.base.personal.StoreOrderView;
import com.zishan.sardinemerchant.ClientApplication;
import com.zishan.sardinemerchant.R;
import com.zishan.sardinemerchant.activity.BActivity;
import com.zishan.sardinemerchant.adapter.page.RedEnvelopeCerificationAdapter;
import com.zishan.sardinemerchant.utils.Skip;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.OnClick;
/*import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;*/

import top.ftas.ftasbase.common.util.GsonParser;
import top.ftas.ftaswidget.recyclerview.SwipeItemLayout;
import top.ftas.ftaswidget.recyclerview.base.BaseQuickAdapter;
import top.ftas.ftaswidget.springview.SpringView;
import top.ftas.ftaswidget.springview.SpringViewHeader;

/**
 * 红包核销
 * Created by wislie on 2017/11/3.
 */

public class RedEnvelopeCertificationActivity extends BActivity<StoreOrderView, StoreOrderPresenter>
        implements StoreOrderView{ //, EasyPermissions.PermissionCallbacks

    @BindView(R.id.cerification_springview)
    SpringView mSpringView;
    @BindView(R.id.cerification_recycler_view)
    RecyclerView mRecycler;

    //当前页
    private int mCurrentPage;
    //每页的size
    private final int PAGE_SIZE = 10;
    //总页数
    private int mPageCount;

    private List<StoreOrderEntity> mDataList = new ArrayList<>();
    private RedEnvelopeCerificationAdapter mAdapter;


    //订单类型
    private final int[] order_type = {3};

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_red_envelope_cerification;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        setActionbarTitle("红包核销");
        setActionBarHomeIcon(R.mipmap.back_white_icon);
        TextView historyText = setActionBarMenuText("记录");
        historyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Skip.toActivity(RedEnvelopeCertificationActivity.this, CertificationRecordActivity.class);
            }
        });
    }

    @Override
    protected StoreOrderPresenter createPresenter() {
        return new StoreOrderPresenter(this, this);
    }

    @Override
    protected void initContentView() {
        initSpringView();
        mRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mAdapter = new RedEnvelopeCerificationAdapter(R.layout.item_red_envelope_cerification, mDataList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.openLoadMore(PAGE_SIZE, true);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(RedEnvelopeCertificationActivity.this,
                        CerificationDetailActivity.class);
                StoreOrderEntity data = mAdapter.getItem(position);
                if (data == null) return;
                OrderAttachmentEntity attachment = GsonParser.parseJsonToClass(data.getAttachment(),
                        OrderAttachmentEntity.class);
                intent.putExtra("useTimetamp", attachment.getStartTime());
                intent.putExtra("userMobile", attachment.getUserMobile());
                intent.putExtra("oprName", attachment.getOprName());
                intent.putExtra("orderId", data.getId());
                intent.putParcelableArrayListExtra("products", attachment.getProducts());
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestRecordList();
            }
        });
        showProgressDialog();
        loadData();
    }

    private void initSpringView() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setHeader(new SpringViewHeader(this));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtil.isNetworkConnected(ClientApplication.getApp())) {
                    finishRefreshAndLoad();
                } else {
                    loadData();
                }
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    private void loadData() {
        mCurrentPage = 0;
        mPresenter.getStoreOrderList(String.valueOf(UserConfig.getInstance(ClientApplication.getApp()).getStoreId()),
                null, null, null, order_type, mCurrentPage, PAGE_SIZE, true);
    }


    private void requestRecordList() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            mPresenter.getStoreOrderList(String.valueOf(UserConfig.getInstance(ClientApplication.getApp()).getStoreId()),
                    null, null, null, order_type, mCurrentPage, PAGE_SIZE, false);
        } else {
            finishRefreshAndLoad();
        }

    }

    @OnClick({R.id.scan_code_cerification})
    public void onClick(View view) {
        switch (view.getId()) {
            //扫码核销
            case R.id.scan_code_cerification:
//                startQRcode();
                Intent intent = new Intent(RedEnvelopeCertificationActivity.this, QRcodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 关闭加载提示
     */
    private void finishRefreshAndLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpringView != null)
                    mSpringView.onFinishFreshAndLoad();
            }
        }, 1000);
    }

    @Override
    protected int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.top_actionbar_bg_color);
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        *//**
         * 处理二维码扫描结果
         *//*
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Skip.toActivity(RedEnvelopeCertificationActivity.this, ScanCodeResultActivity.class);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Skip.toActivity(RedEnvelopeCertificationActivity.this, ScanCodeResultActivity.class);
                    Toast.makeText(RedEnvelopeCertificationActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        *//**
         * 选择系统图片并解析
         *//*
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            //解析结果
                            Toast.makeText(RedEnvelopeCertificationActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Skip.toActivity(RedEnvelopeCertificationActivity.this, ScanCodeResultActivity.class);
                            Toast.makeText(RedEnvelopeCertificationActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT)
                    .show();
        }
    }
*/

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;


    /**
     * EsayPermissions接管权限处理逻辑
     *
     * @param //requestCode
     * @param //permissions
     * @param //grantResults
     */
 /*   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }*/


   /* @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void startQRcode() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
//            onClick(viewId);
            Intent intent = new Intent(RedEnvelopeCertificationActivity.this, QRcodeActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null *//* click listener *//*)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }*/


    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void dismissProgressDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void reLogin() {
        reOnLogin();
    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void newStoreOrderList(List<StoreOrderEntity> dataList) {
        mAdapter.setNewData(dataList);
        finishRefreshAndLoad();
    }

    @Override
    public void addStoreOrderList(List<StoreOrderEntity> dataList) {
        mAdapter.notifyDataChangedAfterLoadMore(dataList, true);
    }

    @Override
    public void showCompleteAllData() {
        mAdapter.notifyDataChangedAfterLoadMore(false);
        View footerView = LayoutInflater.from(this).inflate(R.layout.item_footer_no_more, null);
        footerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mAdapter.addFooterView(footerView);
    }

    @Override
    public void getTotalPageCount(int pageCount, int totalElements) {
        mPageCount = pageCount;
    }

}
