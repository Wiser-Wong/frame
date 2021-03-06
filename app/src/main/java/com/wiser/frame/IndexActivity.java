package com.wiser.frame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bumptech.glide.Glide;
import com.wiser.library.adapter.WISERRVAdapter;
import com.wiser.library.base.WISERActivity;
import com.wiser.library.base.WISERBuilder;
import com.wiser.library.helper.WISERHelper;
import com.wiser.library.manager.permission.IWISERPermissionCallBack;
import com.wiser.library.pager.banner.BannerHolder;
import com.wiser.library.pager.banner.BannerPagerView;
import com.wiser.library.util.WISERDate;
import com.wiser.library.util.WISERTextView;
import com.wiser.library.view.choice.ChoiceIrregularLayout;
import com.wiser.library.view.choice.OnChoiceAdapter;
import com.wiser.library.view.choice.OnChoiceListener;
import com.wiser.library.view.irregular.IrregularLayout;
import com.wiser.library.view.irregular.OnIrregularAdapter;
import com.wiser.library.view.irregular.OnItemClickListener;
import com.wiser.library.view.marquee.MarqueeAdapter;
import com.wiser.library.view.marquee.MarqueeView;
import com.wiser.library.zxing.WISERQRCode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexActivity extends WISERActivity<IndexBiz> implements WISERRVAdapter.OnFooterCustomListener, OnItemClickListener<String> {

	@BindView(R.id.tv_name) TextView								tvName;

	@BindView(R.id.iv_qr) ImageView									ivQR;

	@BindView(R.id.il_view) IrregularLayout<String>					irregularLayout;

	@BindView(R.id.marquee) MarqueeView<IndexModel>					marqueeView;

	@BindView(R.id.tv_d) TextView									textView;

	@BindView(R.id.bv_frg) BannerPagerView							bannerPagerView;

	@BindView(R.id.cil_layout) ChoiceIrregularLayout<IndexModel>	choiceIrregularLayout;

	@Override protected WISERBuilder build(WISERBuilder builder) {
		// builder.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		builder.layoutId(R.layout.activity_index);
		builder.layoutEmptyId(R.layout.view_empty);
		builder.layoutErrorId(R.layout.view_error);
		builder.layoutLoadingId(R.layout.view_loading);
		builder.recycleView().recycleViewId(R.id.home_rlv);
//		builder.isSystemStatusBarPaddingTop();
		// builder.setFullScreenToggle(true);
		// builder.removeStateBar();
		// builder.hideVirtualKey();
		// builder.recycleView().recycleViewStaggeredGridManager(2,
		// LinearLayoutManager.VERTICAL, new WISERStaggeredDivider(20, 0, 20, 0), null);
		builder.recycleView().recycleViewLinearManager(LinearLayoutManager.VERTICAL, null);
		builder.recycleView().recycleViewGridManager(2, LinearLayoutManager.VERTICAL, null);
		builder.recycleView().recycleAdapter(new IndexAdapter(this));
		builder.isRootLayoutRefresh(true, false);
		builder.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);
		builder.recycleView().isFooter(true);
		builder.recycleView().footerLayoutId(R.layout.title_layout);
		builder.recycleView().setOnFooterCustomListener(this);
		// builder.setProgressBackgroundColorSchemeColor(Color.BLACK);
		// builder.setColorSchemeColors(getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.cpv_default_color),getResources().getColor(R.color.design_default_color_primary));
		// builder.tintFitsSystem(false);
		 builder.setStatusBarFullTransparent();
//		builder.tintStateBarColor(getResources().getColor(R.color.red));
		builder.swipeBack(true);
		return builder;
	}

	@SuppressLint("SetTextI18n") @Override public void initData(Intent intent) {

		biz().addAdapterData();
		// tvName.setText(WISERDate.getLongForDateStr("2018-09-11",WISERDate.DATE_HG,true)+"");
		tvName.setText(WISERDate.getDateStrForLong(1536595200000L, WISERDate.DATE_HZ, false) + "");
		// onRefresh();
		// WISERHelper.display().commitReplace(R.id.fl_index, new TabPageFragment());

		WISERQRCode.createQRCodeBitmapForUrl("", "WiserWong", R.mipmap.ic_launcher, ivQR, false);

		marquee();

//		WISERHelper.permissionManage().requestPermission(this, 11, Manifest.permission.CAMERA, new IWISERPermissionCallBack() {
//
//			@Override public void hadPermissionResult() {
//				WISERHelper.toast().show("请求权限成功");
//			}
//		});

		WISERTextView.textformatSpan(tvName, new String[] { "我试试", "顶顶顶顶", "对对对", "的嘎嘎嘎" }, new int[] { 1, 3 }, Color.YELLOW, Color.BLUE, new WISERTextView.SpanClickCallBack() {

			@Override public void spanClick() {
				WISERHelper.toast().show("点击链接文本");
			}
		});

		List<String> list = new ArrayList<>();
		list.add("2222");
		list.add("22www22");
		list.add("22ee22");
		list.add("22dddd22");
		list.add("222w2");
		list.add("22eeeee22");

		irregularLayout.setOnItemClickListener(this);
		irregularLayout.setOnIrregularAdapter(new OnIrregularAdapter<String>() {
			@Override
			public void onCreateItemView(View itemView, int position, String s) {
				((TextView)itemView.findViewById(R.id.tv_align_text)).setText(s);
			}
		});
		irregularLayout.setItems(list);

		// 存储数据
		MConfig mConfig = new MConfig(this);
		mConfig.name = "Wiser";
		mConfig.commit();

		// share存储
		MShareConfig mShareConfig = new MShareConfig();
		mShareConfig.saveString("name", "Wiser");

		WISERHelper.fileCacheManage().writeFile(WISERHelper.fileCacheManage().configureFileDir(this), "log.txt", "今天你好");

		// WISERHelper.toast().show(WISERDate.getDateCovert("2011-4-1", "yyyy-m-d",
		// "yyyy-mm-dd"));

		List<String> list1 = new ArrayList<>();
		list1.add("http://pic1.win4000.com/wallpaper/6/51e35bd76cd74.jpg");
		list1.add("http://b-ssl.duitang.com/uploads/item/201706/10/20170610095055_G5LM8.jpeg");
		list1.add("http://i0.hdslb.com/bfs/article/a22a162bca94cdf7438ea556dfa021b181bf3873.jpg");

		bannerPagerView.setPages(this, new BannerHolder<String>() {

			AppCompatImageView ivBanner;

			@Override public View createView(Context context, LayoutInflater inflater, ViewGroup container) {
				ivBanner = new AppCompatImageView(context);
				ivBanner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				ivBanner.setAdjustViewBounds(true);
				ivBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);
				return ivBanner;
			}

			@Override public void bindData(Context context, int position, String data) {
				if (TextUtils.isEmpty(data)) return;
				Glide.with(context).load(data).thumbnail(0.1f).into(ivBanner);
			}
		}, list1).isDot(true).startTurning(2000);

		// WISERHelper.toast().show(WISERDate.getWeek("2019年3月26日",
		// WISERDate.DATE_HZ,true));

		choiceIrregularLayout.setOnChoiceListener(new OnChoiceListener<IndexModel>() {

			@Override public void onChoiceItemClick(ViewGroup viewGroup, View view, int position, IndexModel indexModel) {
				indexModel.isCheck = !indexModel.isCheck;
				if (viewGroup.equals(choiceIrregularLayout)) choiceIrregularLayout.notifyItemPositionData(position, indexModel);
			}
		});

		choiceIrregularLayout.setChoiceAdapter(new OnChoiceAdapter<IndexModel>() {

			@Override public void onCreateItemView(View itemView, int position, IndexModel indexModel) {
				if (indexModel == null) return;
				TextView tvChoiceName = itemView.findViewById(R.id.tv_align_text);
				if (indexModel.isCheck) {
					tvChoiceName.setBackgroundColor(Color.YELLOW);
					tvChoiceName.setTextColor(Color.RED);
				} else {
					tvChoiceName.setBackgroundColor(Color.GRAY);
					tvChoiceName.setTextColor(Color.WHITE);
				}
				tvChoiceName.setText(indexModel.age);
			}
		});

		choiceIrregularLayout.setItems(biz().getIndexModels());

//		WISERHelper.toast().show(WISERDate.getDateStrForLong(27647527 - WISERDate.getCurrentTimestamp(),WISERDate.DATE_HZ,true));
		WISERHelper.toast().show(WISERDate.getCurrentDateStr(WISERDate.DATE_HZ,true));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = null;
		Date endDate= null;
		try {
			curDate = df.parse("2020-4-16 00:00:00");
			endDate = df.parse("2020-5-16 00:00:00");
			long diff = endDate.getTime() - curDate.getTime();
			System.out.println(WISERDate.getTimes(27647527,"HH:mm:ss"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		WISERHelper.permissionManage().requestPermissions(this, 11111, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS,Manifest.permission.READ_EXTERNAL_STORAGE}, new IWISERPermissionCallBack() {
			@Override
			public void hadPermissionResult() {
				WISERHelper.toast().show("请求多个权限成功了");
			}
		});

		adapter().setItems(biz().indexModels);

		WISERHelper.toast().show(WISERDate.getCurrentDay());
	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 11:
			case 11111:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					WISERHelper.permissionManage().onPermission(requestCode);
				} else {
					WISERHelper.toast().show("错误权限");
				}
				break;
		}
	}

	// 跑马灯
	private void marquee() {
		marqueeView.setMarquee(biz().indexModels, new MarqueeAdapter<IndexModel>(this) {

			@Override protected View createItemView(IndexModel data) {
				if (data == null) return null;
				View view = inflate(R.layout.marquee_item);
				TextView tvMarquee = view.findViewById(R.id.tv_marquee);
				tvMarquee.setText(data.age);
				return view;
			}
		}).setMarqueeAnim(R.anim.anim_marquee_bottom_in, R.anim.anim_marquee_top_out).setTimeInterval(3000).start();
	}

	@Override public void onRefresh() {
		WISERHelper.toast().show("刷新");
		new Handler().postDelayed(new Runnable() {

			@Override public void run() {
				// showLoading();
				refreshComplete();
			}
		}, 4000);
	}

	@Override public void onLoadMore() {
		super.onLoadMore();
		WISERHelper.toast().show("上拉刷新");
		if (adapter().getLoadState() == WISERRVAdapter.LOAD_END || adapter().getLoadState() == WISERRVAdapter.LOAD_RUNNING) return;
		adapter().loadState(WISERRVAdapter.LOAD_RUNNING);
		adapter().loadTip("上拉刷新");
		new Handler(getMainLooper()).postDelayed(new Runnable() {

			@Override public void run() {
				adapter().addList(biz().addNewData());
				adapter().loadState(WISERRVAdapter.LOAD_END);
				adapter().loadTip("已经到头了");
				// showLoading();
				// adapter().loadState(adapter().LOAD_COMPLETE);
			}
		}, 4000);
	}

	@OnClick({ R.id.iv_qr, R.id.tv_d }) public void onClickView(View view) {
		switch (view.getId()) {
			case R.id.tv_name:
				// loadingRefresh();
				// WISERHelper.downUploadManage().fileDownloader().create("https://github.com/Wiser-Wong/MultidexRecord.git")
				// .setPath(WISERHelper.fileCacheManage().configureStorageDir() + File.separator
				// + getResources().getString(R.string.app_name) + "/down.txt")
				// .setListener(new FileDownloadListener() {
				//
				// @Override protected void connected(BaseDownloadTask task, String etag,
				// boolean isContinue, int soFarBytes, int totalBytes) {
				// super.connected(task, etag, isContinue, soFarBytes, totalBytes);
				// WISERHelper.toast().show("链接");
				// }
				//
				// @Override protected void pending(BaseDownloadTask task, int soFarBytes, int
				// totalBytes) {
				// new MToast().show("pending--->>soFarBytes:-->>" + soFarBytes +
				// "totalBytes:-->>" + totalBytes);
				// }
				//
				// @Override protected void progress(BaseDownloadTask task, int soFarBytes, int
				// totalBytes) {
				// new MToast().show("progress--->>soFarBytes:-->>" + soFarBytes +
				// "totalBytes:-->>" + totalBytes);
				// }
				//
				// @Override protected void completed(BaseDownloadTask task) {
				// WISERHelper.toast().show("下载完成");
				// }
				//
				// @Override protected void paused(BaseDownloadTask task, int soFarBytes, int
				// totalBytes) {
				//
				// }
				//
				// @Override protected void error(BaseDownloadTask task, Throwable e) {
				// WISERHelper.toast().show("下载错误");
				// }
				//
				// @Override protected void warn(BaseDownloadTask task) {
				//
				// }
				// }).start();
				break;
			case R.id.iv_qr:

				// RemoteViews remoteViews = new RemoteViews(getPackageName(),
				// R.layout.notification_layout);
				// remoteViews.setTextViewText(R.id.tv1, "我是一个爸爸" + new Random().nextInt(100));
				// remoteViews.setTextViewText(R.id.tv2, "我是一个妈妈");
				// remoteViews.setTextViewText(R.id.tv3, "我是一个孩子");
				// remoteViews.setImageViewResource(R.id.iv1, R.mipmap.ic_launcher_round);
				// WISERHelper.uiManage().notification().showRemoteViewNotification(1,
				// R.mipmap.scan_flash, "新消息提示", remoteViews, ClickBroadcastReceiver.class);
				// WISERHelper.uiManage().notification(true).showProgressNotification(1, "我来了",
				// "更新", "正在下载", BitmapFactory.decodeResource(getResources(),
				// R.mipmap.scan_photo), R.mipmap.scan_flash, 100, 0,
				// ClickBroadcastReceiver.class);

				// WISERHelper.display().intent(TabLayoutActivity.class);
				// WISERHelper.display().intent(SmartActivity.class);
				// WISERHelper.display().intent(ScanActivity.class);
				WISERHelper.display().intent(WebViewActivity.class);
				// WISERHelper.display().intent(ZoomScrollViewActivity.class);
				// WISERHelper.display().intent(SlidingMenuActivity.class);
				// WISERHelper.display().intentTransitionAnimation(ZoomScrollViewActivity.class,null,Pair.create((View)ivQR,""));
				break;
			case R.id.tv_d:
				// WISERHelper.display().intent(TabLayoutActivity.class);
				// WISERHelper.display().commitReplace(R.id.fragment1,new
				// TestFragment(),TestFragment.class.getName());
				IndexDialogFragment.newInstance().show(getSupportFragmentManager(), "");
				// IndexDialogFragment.newInstance().setLocation(textView,
				// WISERDialogFragment.CONTROL_FIT).show(getSupportFragmentManager(), "");

				// WISERHelper.uiManage().notification(true).showNotification(1, "我来了", "更新",
				// "正在下载", BitmapFactory.decodeResource(getResources(), R.mipmap.scan_photo),
				// R.mipmap.scan_flash, true,ClickBroadcastReceiver.class);
				WISERHelper.uiManage().notification(true).showProgressNotification(1, "我来了", "更新", "正在下载", BitmapFactory.decodeResource(getResources(), R.mipmap.scan_photo), R.mipmap.scan_flash, 100,
						20, true, ClickBroadcastReceiver.class);
				break;
		}
	}

	@Override public void footerListener(View footerView, int state) {
		if (adapter() == null) return;
		if (footerView == null) return;
		TextView tvFooter = footerView.findViewById(R.id.tv_title_name);
		switch (state) {
			case WISERRVAdapter.LOAD_RUNNING:
				footerView.setVisibility(View.VISIBLE);
				tvFooter.setText("加载呢");
				break;
			case WISERRVAdapter.LOAD_COMPLETE:
				footerView.setVisibility(View.GONE);
				break;
			case WISERRVAdapter.LOAD_END:
				footerView.setVisibility(View.VISIBLE);
				tvFooter.setText("没啥数据了");
				break;
		}
	}

	public void showT(String s) {
		WISERHelper.toast().show(s);
	}

	@Override public void onItemClick(ViewGroup viewGroup, View view, int position, String s) {
		showT(s);
	}
}
