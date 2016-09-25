package com.xml.parse;

import com.yline.log.LogFileUtil;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by yline on 2016/9/25.
 */
public class SaxParseUtil
{
	private static final String TAG = "SaxParseUtil";

	public void parseXmlWithSAX(InputStream inputStream)
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			ContentHandler handler = new ContentHandler();
			// 将ContentHandler的实例设置到XMLReader中
			xmlReader.setContentHandler(handler);
			// 开始执行解析
			xmlReader.parse(new InputSource(inputStream));
		}
		catch (Exception e)
		{
			LogFileUtil.i(TAG, "parseXMLWithSAX -> Exception", e);
		}
	}

	/**
	 * 导包全都是  带sax的包
	 * @param xmlData xml生成的String字符串
	 */
	public void parseXMLWithSAX(String xmlData)
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			ContentHandler handler = new ContentHandler();
			// 将ContentHandler的实例设置到XMLReader中
			xmlReader.setContentHandler(handler);
			// 开始执行解析
			xmlReader.parse(new InputSource(new StringReader(xmlData)));
		}
		catch (Exception e)
		{
			LogFileUtil.i(TAG, "parseXMLWithSAX -> Exception", e);
		}
	}

	/**
	 * 需要注意的是，在获取结点中的内容时，characters()方法可能会被调用多次，一些换行符也被当作内容解析出来，
	 * 我们需要针对这种情况在代码中做好控制
	 */
	public class ContentHandler extends DefaultHandler
	{
		private String nodeName;

		private StringBuilder id;

		private StringBuilder name;

		private StringBuilder wind;

		private StringBuilder weather;

		private StringBuilder temp;

		private StringBuilder pm;

		@Override    // 开始XML解析的时候调用
		public void startDocument() throws SAXException
		{
			id = new StringBuilder();
			name = new StringBuilder();
			wind = new StringBuilder();
			weather = new StringBuilder();
			temp = new StringBuilder();
			pm = new StringBuilder();
		}

		@Override    // 开始解析某个结点的时候调用
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			// 记录当前结点名
			nodeName = localName;
		}

		@Override    // 在获取结点中内容的时候调用
		public void characters(char[] ch, int start, int length) throws SAXException
		{
			// 根据当前的结点名判断将内容添加到哪一个StringBuilder对象中
			if ("id".equals(nodeName))
			{
				id.append(ch, start, length);
			}
			else if ("name".equals(nodeName))
			{
				name.append(ch, start, length);
			}
			else if ("wind".equals(nodeName))
			{
				wind.append(ch, start, length);
			}
			else if ("weather".equals(nodeName))
			{
				weather.append(ch, start, length);
			}
			else if ("temp".equals(nodeName))
			{
				temp.append(ch, start, length);
			}
			else if ("pm".equals(nodeName))
			{
				pm.append(ch, start, length);
			}
		}

		@Override    // 完成解析某个结点的时候调用
		public void endElement(String uri, String localName, String qName) throws SAXException
		{
			if ("infos".equals(localName))
			{
				LogFileUtil.v("ContentHandler", "id is " + id.toString().trim());
				LogFileUtil.v("ContentHandler", "name is " + name.toString().trim());
				LogFileUtil.v("ContentHandler", "wind is " + wind.toString().trim());
				LogFileUtil.v("ContentHandler", "weather is " + weather.toString().trim());
				LogFileUtil.v("ContentHandler", "temp is " + temp.toString().trim());
				LogFileUtil.v("ContentHandler", "pm is " + pm.toString().trim());
				// 最后要将StringBuilder清空掉
				id.setLength(0);
				name.setLength(0);
				wind.setLength(0);
				weather.setLength(0);
				temp.setLength(0);
				pm.setLength(0);
			}
		}

		@Override    // 完成整个XML解析的时候调用
		public void endDocument() throws SAXException
		{
		}
	}
}
