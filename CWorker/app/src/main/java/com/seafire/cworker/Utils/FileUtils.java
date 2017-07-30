package com.seafire.cworker.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.seafire.cworker.App;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Description: 文件操作工具类
 * Created by guzhenfu on 2016/2/26 13:39.
 */
public class FileUtils {

	private FileUtils() {}

	/**
	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @param context
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String readInStream(InputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 * 向手机写图片
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static String writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file.getAbsolutePath();
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (TextUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size
	 *            字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 获取目录文件个数
	 *
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * 检查路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path) {
		return new File(path).exists();
	}

	/**
	 * 计算SD卡的剩余空间
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 新建目录
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装SD卡
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}
	
	/**
	 * 检查是否安装外置的SD卡
	 * 
	 * @return
	 */
	public static boolean checkExternalSDExists() {
		
		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();
		
		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除空目录
	 * 
	 * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
	 * 
	 * @return
	 */
	public static int deleteBlankPath(String path) {
		File f = new File(path);
		if (!f.canWrite()) {
			return 1;
		}
		if (f.list() != null && f.list().length > 0) {
			return 2;
		}
		if (f.delete()) {
			return 0;
		}
		return 3;
	}

	/**
	 * 重命名
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reNamePath(String oldName, String newName) {
		File f = new File(oldName);
		return f.renameTo(new File(newName));
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
			f.delete();
			return true;
		}
		return false;
	}
	
	/**
	 * 清空一个文件夹
	 */
	public static void clearFileWithPath(String filePath) {
		List<File> files = FileUtils.listPathFiles(filePath);
		if (files.isEmpty()) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				clearFileWithPath(f.getAbsolutePath());
			} else {
				f.delete();
			}
		}
	}

	/**
	 * 获取SD卡的根目录
	 * 
	 * @return
	 */
	public static String getSDRoot() {
		
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * 获取手机外置SD卡的根目录
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {
		
		Map<String, String> evn = System.getenv();
		
		return evn.get("SECONDARY_STORAGE");
	}

	/**
	 * 列出root目录下所有子目录
	 *
	 * @return 绝对路径
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// 过滤掉以.开始的文件夹
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}
	
	/**
	 * 获取一个文件夹下的所有文件
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();
		for (File f : files) {
			if (f.isFile())
				allDir.add(f);
			else 
				listPath(f.getAbsolutePath());
		}
		return allDir;
	}

	public enum PathStatus {
		SUCCESS, EXITS, ERROR
	}

	/**
	 * 创建目录
	 *
	 */
	public static PathStatus createPath(String newPath) {
		File path = new File(newPath);
		if (path.exists()) {
			return PathStatus.EXITS;
		}
		if (path.mkdir()) {
			return PathStatus.SUCCESS;
		} else {
			return PathStatus.ERROR;
		}
	}

	/**
	 * 截取路径名
	 * 
	 * @return
	 */
	public static String getPathName(String absolutePath) {
		int start = absolutePath.lastIndexOf(File.separator) + 1;
		int end = absolutePath.length();
		return absolutePath.substring(start, end);
	}
	
	/**
	 * 获取应用程序缓存文件夹下的指定目录
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getAppCache(Context context, String dir) {
		String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/";
		File savedir = new File(savePath);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		savedir = null;
		return savePath;
	}

	public static void closeIO(Closeable... closeables) {
		if (null == closeables || closeables.length <= 0) {
			return;
		}
		for (Closeable cb : closeables) {
			try {
				if (null == cb) {
					continue;
				}
				cb.close();
			} catch (IOException e) {
				throw new RuntimeException(
						FileUtils.class.getClass().getName(), e);
			}
		}
	}

	public static String getFilePath(Uri mUri) {
		try {
			if (mUri.getScheme().equals("file")) {
				return mUri.getPath();
			} else {
				return getFilePathByUri(mUri);
			}
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	public static String getFilePathByUri(Uri mUri) throws FileNotFoundException {
		Cursor cursor = null;
		String s = null;
		try {
			cursor = App.getContext().getContentResolver()
                    .query(mUri, null, null, null, null);
			cursor.moveToFirst();
			s = cursor.getString(1);
		} finally {
			cursor.close();
		}
		return s;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res,resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromFilePath(String filePath,
														 int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public  static   void     Copy(String     oldPath,     String     newPath)
	{
		try     {
			int     bytesum     =     0;
			int     byteread     =     0;
			File     oldfile     =     new     File(oldPath);
			if     (oldfile.exists())     {
				InputStream     inStream     =     new     FileInputStream(oldPath);
				FileOutputStream     fs     =     new     FileOutputStream(newPath);
				byte[]     buffer     =     new     byte[1444];
				int     length;
				while     (     (byteread     =     inStream.read(buffer))     !=     -1)     {
					bytesum     +=     byteread;
					System.out.println(bytesum);
					fs.write(buffer,     0,     byteread);
				}
				inStream.close();
			}
		}
		catch     (Exception     e)     {
			System.out.println( "error  ");
			e.printStackTrace();
		}
	}



	/**
	 * <br>功能简述:4.4及以上获取图片的方法
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		boolean bDocumentProvider = false;
		if (isKitKat) {
			try {
				bDocumentProvider = DocumentsContract.isDocumentUri(context, uri);
			} catch (Exception e) {
				bDocumentProvider = false;
			}
		}
		// DocumentProvider
		if (isKitKat && bDocumentProvider) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}


	/**
	 * 打开相册
	 * @param context
	 * @param request
     */
	public static void startAlbum(Activity context, int request) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			context.startActivityForResult(intent, request);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			try {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				context.startActivityForResult(intent, request);
			} catch (Exception e2) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	/**
	 * 打开相机
	 * @param context
	 * @param request
     */
	public static void startCapture(Activity context, int request, String path) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), path)));
		context.startActivityForResult(intent, request);
	}

//	public static UpdateInfo GetUpdateInfo(@NonNull String path){
//		UpdateInfo info = null;
//		URL url;
//		HttpURLConnection conn = null;
//		InputStream is = null;
//		try {
//			url = new URL(path);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setConnectTimeout(10000);
//			is = conn.getInputStream();
//			info = GetUpdateInfoFromIs(is);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return info;
//	}


//	/**
//	 * 是否应该下载APP
//	 * @param is
//	 * @return
//     */
//	private static UpdateInfo GetUpdateInfoFromIs(InputStream is) {
//		XmlPullParser parser = Xml.newPullParser();
//		UpdateInfo infos = new UpdateInfo();// 实体
//		int type;
//		try {
//			parser.setInput(is, "utf-8"); // 设置解析的数据源
//			type = parser.getEventType();
//			while (type != XmlPullParser.END_DOCUMENT) {
//				switch (type) {
//					case XmlPullParser.START_TAG:
//						if ("Version".equals(parser.getName())) {
//							infos.setVersion(parser.nextText()); // 获取版本号
//						} else if ("Url".equals(parser.getName())) {
//							infos.setUrl(parser.nextText()); // 获取要升级的APK文件
//						} else if ("Info".equals(parser.getName())) {
//							infos.setDescription(parser.nextText()); // 获取该文件的信息
//						}
//						break;
//				}
//				type = parser.next();
//			}
//		} catch (XmlPullParserException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return infos;
//	}

	/**
	 * HttpURLConnection 下载文件
	 * @param subscriber
	 * @param path
	 * @param file
     */
	public static void downloadApk(Subscriber subscriber, String path, File file){
		URL url;
		HttpURLConnection conn;
		InputStream is=null;
		FileOutputStream fos = null;
		BufferedInputStream bis=null;
		int temp = 0;
		int length = 0;
		float progress;
		try {
			url = new URL(Uri.encode(path, "-![.:/,%?&=]"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			length = conn.getContentLength();
			//subscriber.onNext(length);

			is = conn.getInputStream();
//             如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				return;
			}
			if (!file.exists()){
				file.createNewFile();
			}

			fos=new FileOutputStream(file);
			bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				temp += len;
				progress = ((float)total)/length;
				if (temp > length/100){
					temp = 0;
					LogUtils.i("progress:--" + progress);
					subscriber.onNext(progress);
				}
				if (total == length)
					subscriber.onNext(1.0f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos!=null) {
					fos.close();
				}
				if(bis!=null) {
					bis.close();
				}
				if(is!=null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}