package com.iflytek.demo.download;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {

	private int threadId;
	private long startIndex;
	private long endIndex;
	private String path;
	private static int runningThreadCount = 3;

	DownloadThread(int threadId, long startIndex, long endIndex, String path) {
		this.threadId = threadId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.path = path;
	}

	@Override
	public void run() {
		try {
			// 子线程下载数据

			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3000);

			// 得到上次下載到的位置
			File file = new File(Environment.getExternalStorageDirectory()+ "/"+ threadId + ".txt");
			//判断文件是否存在,及文件中是否有数据
			if (file.exists() && file.length() > 0) {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				String content = br.readLine();
				int position = Integer.parseInt(content);
				// 把上次下载到的位置赋值startIndex
				startIndex = position;
			}

			// 只请求下载部分数据资源
			conn.setRequestProperty("Range", "bytes=" + startIndex + "-"
					+ endIndex);

			// 打开temp文件
			RandomAccessFile raf = new RandomAccessFile(Environment.getExternalStorageDirectory()+ "/temp.jpg", "rwd");
			// 从指定位置开始写数据
			raf.seek(startIndex);

			int code = conn.getResponseCode();
			if (code == 206) {// 服务器端返回206表示请求部分数据资源成功
				System.out.println("=====================线程" + threadId
						+ "开始下载");
				InputStream is = conn.getInputStream();
				int len = -1;
				byte[] buffer = new byte[1024 * 1024];
				// 记录下载数据的长度
				long totalSize = 0;
				while ((len = is.read(buffer)) != -1) {

					raf.write(buffer, 0, len);

					// 计算当前下载的数据长度
					totalSize = totalSize + len;

					// 记录当前下载到的位置
					long currentPosition = startIndex + totalSize;
					System.out.println("=======" + threadId
							+ "=========currentPosition======"
							+ currentPosition);
					// 把当前位置记录到threadId.txt
					RandomAccessFile f = new RandomAccessFile(
							Environment.getExternalStorageDirectory()+ "/"+ threadId + ".txt", "rwd");
					f.write((currentPosition + "").getBytes());
					f.close();

				}
				raf.close();
				is.close();
				System.out.println("=====================线程" + threadId
						+ "下载完成");

				synchronized (DownloadThread.class) {
					runningThreadCount--;
					if (runningThreadCount == 0) {
						System.out
								.println("=====================文件下载完成=================");

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/*	private EditText et_path;
	// 线程的个数
	private static int threadCount = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_path = (EditText) findViewById(R.id.et_path);
	}

	public void download(View view){

		final String path = et_path.getText().toString().trim();

		if(TextUtils.isEmpty(path)){
			Toast.makeText(this, "请输入文件的网络地址", 0).show();
			return;
		}else{
			// 使用多线程下载文件
			new Thread(){
				public void run() {
					try {

						URL url = new URL(path);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();

						conn.setRequestMethod("GET");
						conn.setConnectTimeout(3000);

						int code = conn.getResponseCode();

						if (code == 200) {
							// 1. 得到服务器上文件的大小
							long length = conn.getContentLength();
							// 在本地创建一个临时的空文件
							RandomAccessFile raf = new RandomAccessFile(Environment.getExternalStorageDirectory()+ "/temp.jpg", "rwd");

							// 设置文件的大小
							raf.setLength(length);

							// 2.计算每个线程下载数据块的大小
							long blockSize = length / threadCount;
							// 3.计算每个子线程下载数据的开始位置和结束位置
							for (int threadId = 0; threadId < threadCount; threadId++) {
								// 子线程下载数据的开始位置
								long startIndex = threadId * blockSize;
								// 子线程下载数据的結束位置
								long endIndex = (threadId + 1) * blockSize - 1;
								// 最后一个子线程下载数据的结束位置
								if (threadId == threadCount - 1) {
									endIndex = length - 1;
								}
								// 创建子线程下载数据
								new DownloadThread(threadId,startIndex,endIndex,path).start();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();

		}
	}*/

