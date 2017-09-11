package com.shenni.lives.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
<author>Ming</author>
<date> 2016-12-12 </date>
<summary>捕获异常信息并写入错误日志</summary>
 **/
public class CrashHandler  implements UncaughtExceptionHandler {

	    private UncaughtExceptionHandler defaultUEH;
	//构造函数，获取默认的处理方法
	    public CrashHandler() 
	    {
	        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	    }
	//这个接口必须重写，用来处理我们的异常信息
	    @Override
	    public void uncaughtException(Thread thread, Throwable ex)
	    {
	        final Writer result = new StringWriter();
	        final PrintWriter printWriter = new PrintWriter(result);
	//获取跟踪的栈信息，除了系统栈信息，还把手机型号、系统版本、编译版本的唯一标示
	        StackTraceElement[] trace = ex.getStackTrace();
	        StackTraceElement[] trace2 = new StackTraceElement[trace.length+3];
	        System.arraycopy(trace, 0, trace2, 3, trace.length);
	        trace2[0] = new StackTraceElement("Android", "MODEL", android.os.Build.MODEL, -1);
	        trace2[1] = new StackTraceElement("Android", "VERSION", android.os.Build.VERSION.RELEASE, -1);
	        trace2[2] = new StackTraceElement("Android", "FINGERPRINT", android.os.Build.FINGERPRINT, -1);
	//追加信息，因为后面会回调默认的处理方法
	        ex.setStackTrace(trace2);
	        ex.printStackTrace(printWriter);
	 //把上面获取的堆栈信息转为字符串，打印出来
	        String stacktrace = result.toString();
	        printWriter.close();
	        //这里把刚才异常堆栈信息写入SD卡的Log日志里面
	        writeLog(stacktrace);
	        defaultUEH.uncaughtException(thread, ex);
	    }
		/**
		 * 
		 * 写文件日志
		 */
		public static void writeLog(String text)  {
			  
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

					File saveFile = new File(Constants.SavePath, "errorlog.txt");
					if(saveFile.exists())
						saveFile.delete();
						try {
							FileOutputStream outStream = new FileOutputStream(saveFile,true);
							 outStream.write(("----------"+DateUtil.getcurrentTime()+"--------\n"+text+"\n").getBytes());
					         outStream.close();

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
				        
				} 

			} 
		public static void writeLog(String text, String filename)  {
			  
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

				File saveFile = new File(Constants.SavePath, filename);
//				if(saveFile.exists())
//					saveFile.delete();
					try {
						FileOutputStream outStream = new FileOutputStream(saveFile,true);
						 outStream.write(("----------"+DateUtil.getcurrentTime()+"--------\n"+text+"\n").getBytes());
				         outStream.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			        
			} 

		} 
}
