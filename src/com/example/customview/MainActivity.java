package com.example.customview;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.imageview);

		imageView.setImageBitmap(decodeSampledBitmapFromResource(
				getResources(), R.drawable.apple, 300, 300));
		imageView.setOnTouchListener(new DoubleClick());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 获取压缩后的图片
	 * 
	 * @param res
	 * @param resId
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
		BitmapFactory.decodeResource(res, resId, options);

		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 计算压缩比例值
	 * 
	 * @param options
	 *            解析图片的配置信息
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 保存图片原宽高值
		final int height = options.outHeight;
		final int width = options.outWidth;
		// 初始化压缩比例为1
		int inSampleSize = 1;

		// 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// 压缩比例值每次循环两倍增加,
			// 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	int count;
	long currentTime;
	long pretime;
	boolean flag;

	void initDouble() {
		count = 0;
		currentTime = 0;
		pretime = 0;
	}

	//双击简单实现
	class DoubleClick implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (count == 0) {
					pretime = System.currentTimeMillis();
					System.out.println("前一个时间是 " + pretime + " count = "
							+ count);
					count++;
				} else if (count == 1) {
					currentTime = System.currentTimeMillis();
					System.out.println("当前时间是 " + currentTime + " count = "
							+ count);
					if (currentTime - pretime < 500) {
						System.out.println("出现双击事件");
						if (flag == false) {
							scale(1.25, 1.25);
							flag = true;
						} else {
							flag = false;
							scale(0.8, 0.8);
						}
					}
					initDouble();
				}
			}
			return false;
		}

	}

	//图片放大实现
	private Bitmap bmp; // bitmap图片对象
	private int primaryWidth; // 原图片宽
	private int primaryHeight; // 原图片高
	private double scaleWidth, scaleHeight; // 高宽比例

	private void scale(double scale_width, double scale_height) {
		
		scaleWidth = scaleHeight = 1;
		scaleWidth = scaleWidth * scale_width; // 缩放到原来的*倍
		scaleHeight = scaleHeight * scale_height;
		bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		// 创建BitMap对象，用于显示图片
		bmp = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.apple);
		// 原始大小
		primaryWidth = bmp.getWidth();
		primaryHeight = bmp.getHeight();

		Matrix matrix = new Matrix(); // 矩阵，用于图片比例缩放
		matrix.postScale((float) scaleWidth, (float) scaleHeight); // 设置高宽比例（三维矩阵）
 
		// 缩放后的BitMap
		Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, primaryWidth,
				primaryHeight, matrix, true);

		// 重新设置BitMap
		imageView.setImageBitmap(newBmp);

	}

}
