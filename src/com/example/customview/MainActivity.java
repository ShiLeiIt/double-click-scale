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
	 * ��ȡѹ�����ͼƬ
	 * 
	 * @param res
	 * @param resId
	 * @param reqWidth
	 *            ����ͼƬѹ���ߴ���С���
	 * @param reqHeight
	 *            ����ͼƬѹ���ߴ���С�߶�
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// ���Ȳ�����ͼƬ,����ȡͼƬ�ߴ�
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// ��inJustDecodeBounds��Ϊtrueʱ,�������ͼƬ����ȡͼƬ�ߴ���Ϣ
		options.inJustDecodeBounds = true;
		// ��ʱ���ὫͼƬ��Ϣ�ᱣ����options������,decode�������᷵��bitmap����
		BitmapFactory.decodeResource(res, resId, options);

		// ����ѹ������,��inSampleSize=4ʱ,ͼƬ��ѹ����ԭͼ��1/4
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ��inJustDecodeBounds��Ϊfalseʱ,BitmapFactory.decode...�ͻ᷵��ͼƬ������
		options.inJustDecodeBounds = false;
		// ���ü���ı���ֵ��ȡѹ�����ͼƬ����
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * ����ѹ������ֵ
	 * 
	 * @param options
	 *            ����ͼƬ��������Ϣ
	 * @param reqWidth
	 *            ����ͼƬѹ���ߴ���С���
	 * @param reqHeight
	 *            ����ͼƬѹ���ߴ���С�߶�
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// ����ͼƬԭ���ֵ
		final int height = options.outHeight;
		final int width = options.outWidth;
		// ��ʼ��ѹ������Ϊ1
		int inSampleSize = 1;

		// ��ͼƬ���ֵ�κ�һ����������ѹ��ͼƬ���ֵʱ,����ѭ������ϵͳ
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// ѹ������ֵÿ��ѭ����������,
			// ֱ��ԭͼ���ֵ��һ�����ѹ��ֵ��~����������ֵΪֹ
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

	//˫����ʵ��
	class DoubleClick implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (count == 0) {
					pretime = System.currentTimeMillis();
					System.out.println("ǰһ��ʱ���� " + pretime + " count = "
							+ count);
					count++;
				} else if (count == 1) {
					currentTime = System.currentTimeMillis();
					System.out.println("��ǰʱ���� " + currentTime + " count = "
							+ count);
					if (currentTime - pretime < 500) {
						System.out.println("����˫���¼�");
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

	//ͼƬ�Ŵ�ʵ��
	private Bitmap bmp; // bitmapͼƬ����
	private int primaryWidth; // ԭͼƬ��
	private int primaryHeight; // ԭͼƬ��
	private double scaleWidth, scaleHeight; // �߿����

	private void scale(double scale_width, double scale_height) {
		
		scaleWidth = scaleHeight = 1;
		scaleWidth = scaleWidth * scale_width; // ���ŵ�ԭ����*��
		scaleHeight = scaleHeight * scale_height;
		bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		// ����BitMap����������ʾͼƬ
		bmp = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.apple);
		// ԭʼ��С
		primaryWidth = bmp.getWidth();
		primaryHeight = bmp.getHeight();

		Matrix matrix = new Matrix(); // ��������ͼƬ��������
		matrix.postScale((float) scaleWidth, (float) scaleHeight); // ���ø߿��������ά����
 
		// ���ź��BitMap
		Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, primaryWidth,
				primaryHeight, matrix, true);

		// ��������BitMap
		imageView.setImageBitmap(newBmp);

	}

}
