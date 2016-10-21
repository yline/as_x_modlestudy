package com.system.app.music;

import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yline.log.LogFileUtil;

public class MusicBean
{
	private static final String TAG = "MusicBean";

	/** 读取本地歌曲时,内容 */
	private static final String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME, // 文件名+后缀名
			MediaStore.Audio.Media.DATA, // 路径名
			MediaStore.Audio.Media.DURATION, // 时长 ms
			MediaStore.Audio.Media.TITLE, // 歌名
			MediaStore.Audio.Media.SIZE, // 文件大小
			MediaStore.Audio.Media.MIME_TYPE // 文件类型
	};

	/** 文件名,带后缀名 */
	private String filePathName;

	/** 文件名(不带后缀) */
	private String fileName;

	/** 后缀名 */
	private String endName;

	/** 文件路径,/sdcard/ */
	private String path;

	/** 音乐时长 ms */
	private String duration = "-1";

	/** 歌曲名 */
	private String titleName = null;

	/** 歌曲文件大小 */
	private String size = "-1";

	/** 歌曲类型 */
	private String mimeType = null;

	public MusicBean(Cursor cursor)
	{
		super();

		this.filePathName = cursor.getString(cursor.getColumnIndex(projection[0]));
		parseFilePathName(filePathName);

		this.path = cursor.getString(cursor.getColumnIndex(projection[1]));
		this.duration = cursor.getString(cursor.getColumnIndex(projection[2]));
		this.titleName = cursor.getString(cursor.getColumnIndex(projection[3]));
		this.size = cursor.getString(cursor.getColumnIndex(projection[4]));
		this.mimeType = cursor.getString(cursor.getColumnIndex(projection[5]));
	}

	/**
	 * 解析名称
	 * @param filePathName
	 */
	private void parseFilePathName(String filePathName)
	{
		/** 文件名,后缀名 */
		if (TextUtils.isEmpty(filePathName))
		{
			LogFileUtil.v(TAG, "filePathName is null");
			return;
		}

		if (!filePathName.contains("."))
		{
			LogFileUtil.v(TAG, "musicName do not have a point name = " + filePathName);
			return;
		}

		// 解析出 文件名+后缀名
		this.fileName = filePathName.substring(filePathName.lastIndexOf(".") + 1, filePathName.length());
		this.endName = filePathName.substring(0, filePathName.length() - fileName.length() - 1);
	}

	public static String[] getProjection()
	{
		return projection;
	}

	public String getFilePathName()
	{
		return filePathName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getEndName()
	{
		return endName;
	}

	public String getPath()
	{
		return path;
	}

	public String getDuration()
	{
		return duration;
	}

	public String getTitleName()
	{
		return titleName;
	}

	public String getSize()
	{
		return size;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	@Override
	public String toString()
	{
		return "MusicBean [filePathName=" + filePathName + ", fileName=" + fileName + ", endName=" + endName + ", path=" + path + ", duration=" + duration + ", titleName=" + titleName + ", size=" + size + ", mimeType=" + mimeType + "]";
	}
}
