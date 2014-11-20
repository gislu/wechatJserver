package lxt.service;

import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lxt.message.resp.Article;
import lxt.message.resp.NewsMessage;
import lxt.message.resp.TextMessage;
import lxt.util.MessageUtil;

public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent;

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			 
			// 文本消息 
			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
				String content = requestMap.get("Content").trim();
				if (content.startsWith("翻译")) {
					String keyWord = content.replaceAll("^翻译", "").trim();
						textMessage.setContent(BaiduTranslateService.translate(keyWord));
					respMessage=MessageUtil.textMessageToXml(textMessage);
				}

				else if (content.startsWith("菜单")){
					respContent = "菜单如下[呲牙]:\n\n1.点击微信对话框右侧的“+”按钮，将附加菜单中的位置发送给我后就可以得到解说哟~\n2.发送“翻译+内容”可使用智能翻译功能，如“翻译 我爱你”~\n3.发送“天气“可查看近期天气~";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
			}
				else if (content.startsWith("历史上的今天")){
					
					textMessage.setContent(TodayInHistoryService.getTodayInHistoryInfo());
					respMessage=MessageUtil.textMessageToXml(textMessage);
			}
				else if (content.startsWith("天气")){
					respContent = "今日合肥气温16~20°C，阴天，请注意穿衣哟~";
					textMessage.setContent(respContent);
					//textMessage.setContent(Weather.getWeatherInfo());
					respMessage=MessageUtil.textMessageToXml(textMessage);
			}
				else {
					respContent="没听懂你说的啥哎╮(╯▽╰)╭，回复“菜单”回到主菜单";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
			}
				
				
			
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息唉。。请把位置信息发送给我";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {				
				
				// 创建图文消息
				NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				List<Article> articleList = new ArrayList<Article>();
				//
				
				 String contentx = requestMap.get("Location_X"); 
				 String contenty = requestMap.get("Location_Y"); 
				 String hui= "您发送的经度坐标是：";
				 String fu= "您发送的纬度坐标是：";
				 String z= "\n";
				 
				
				Article article = new Article();
				article.setTitle("安徽大学翡翠湖");
				article.setDescription(hui + contentx + z +fu + contenty +"\n 您现在所在的位置是安徽大学翡翠湖，湖面总面积为947亩，为古梗水库改建而成。地势舒缓，兼有水面、各种树木，自然风光优美。");
				article.setPicUrl("http://e.hiphotos.baidu.com/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38%3Bt%3Dgif/sign=29d3e5aea786c9171c0e5a6ba8541baa/d01373f082025aaf18167f9dfbedab64034f1abb.jpg");
				article.setUrl("http://baike.baidu.com/subview/3122808/13356704.htm?fr=aladdin");
				articleList.add(article); 
				// 设置图文消息个数
				newsMessage.setArticleCount(articleList.size());
				// 设置图文消息包含的图文集合
				newsMessage.setArticles(articleList);
				// 将图文消息对象转换成xml字符串
				respMessage = MessageUtil.newsMessageToXml(newsMessage);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息唉。。请把位置信息发送给我";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息唉。。请把位置信息发送给我";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！[呲牙]本服务正在开发测试中[呲牙][呲牙]，\n1.点击微信对话框右侧的“+”按钮，将附加菜单中的位置发送给我后就可以得到解说哟~\n2.发送“翻译+内容”可使用智能翻译功能，如“翻译 我爱你”~\n3.发送“天气“可查看近期天气~";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

}
