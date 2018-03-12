package com.zishan.sardinemerchant.utils;

import android.content.Context;
import android.text.TextUtils;

import com.hyphenate.helpdesk.model.AgentIdentityInfo;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.hyphenate.helpdesk.model.OrderInfo;
import com.hyphenate.helpdesk.model.QueueIdentityInfo;
import com.hyphenate.helpdesk.model.VisitorInfo;
import com.hyphenate.helpdesk.model.VisitorTrack;

/**
 * 对轨迹跟踪的消息操作 此类不是必须，只是为了演示和初始化一些数据
 */
public class MessageHelper {

	public static final String IMAGE_URL_1 = "http://o8ugkv090.bkt.clouddn.com/hd_one.png";
	public static final String IMAGE_URL_2 = "http://o8ugkv090.bkt.clouddn.com/hd_two.png";
	public static final String IMAGE_URL_3 = "http://o8ugkv090.bkt.clouddn.com/hd_three.png";
	public static final String IMAGE_URL_4 = "http://o8ugkv090.bkt.clouddn.com/hd_four.png";


	public static VisitorInfo createVisitorInfo() {
		VisitorInfo info = ContentFactory.createVisitorInfo(null);
		info.nickName("Admin")//Preferences.getInstance().getNickName()
		    .name("18767101271")//Preferences.getInstance().getUserName()
		    .qq("10000")
			.phone("15811200000")
		    .companyName("easemob")
		    .description("")
		    .email("abc@ccc.com");
		return info;
	}




	public static VisitorTrack createVisitorTrack(Context context, int index) {
		VisitorTrack track = ContentFactory.createVisitorTrack(null);
		switch(index) {
		case 3:
			track.title("轨迹消息1")//context.getString(R.string.test_track_title1)
                 .price("￥5400")
                 .desc("缪缪女士高跟鞋")//context.getString(R.string.em_example3_text)
                 .imageUrl(IMAGE_URL_3)
                 .itemUrl("http://www.baidu.com");
			break;
		case 4:
			track.title("轨迹消息2")//context.getString(R.string.test_track_title2)
					.price("￥3915000")
					.desc("卡地亚的一块手表")//context.getString(R.string.em_example4_text)
            .     imageUrl(IMAGE_URL_4)
                 .itemUrl("http://www.baidu.com");
			break;
			default:
				break;
		}
		return track;
	}
	
	public static OrderInfo createOrderInfo(Context context, int index) {
		OrderInfo info = ContentFactory.createOrderInfo(null);
		switch(index) {
		case 1:
			info.title("订单消息1")//context.getString(R.string.test_order_title1)
			    .orderTitle(String.format("%s：7890","订单号"))//context.getString(R.string.order_number)
			    .price("￥8000")
			    .desc("古驰亮色夹克")//context.getString(R.string.em_example1_text)
			    .imageUrl(IMAGE_URL_1)
			    .itemUrl("http://www.baidu.com");
			break;
		case 2:
			info.title("订单消息2")//context.getString(R.string.test_order_title2)
				.orderTitle(String.format("%s：7890","订单号"))//context.getString(R.string.order_number)
		        .price("￥158000")
		        .desc("铂金女士手提袋")//context.getString(R.string.em_example2_text)
		        .imageUrl(IMAGE_URL_2)
		        .itemUrl("http://www.baidu.com");
			break;
			default:
				break;
		}
		return info;
		
	}
	
	public static AgentIdentityInfo createAgentIdentity(String agentName) {
		if (TextUtils.isEmpty(agentName)){
			return null;
		}
		AgentIdentityInfo info = ContentFactory.createAgentIdentityInfo(null);
		info.agentName(agentName);
		return info;
	}
	
	public static QueueIdentityInfo createQueueIdentity(String queueName) {
		if (TextUtils.isEmpty(queueName)){
			return null;
		}
		QueueIdentityInfo info = ContentFactory.createQueueIdentityInfo(null);
		info.queueName(queueName);
		return info;
	}
}
