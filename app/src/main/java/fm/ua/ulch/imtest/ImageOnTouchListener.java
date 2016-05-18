package fm.ua.ulch.imtest;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageOnTouchListener implements View.OnTouchListener {
    Matrix startMatrix = new Matrix();
    Matrix matrix = new Matrix();
    Matrix imgMatrix = new Matrix();
    Matrix newImgMatrix = new Matrix();

//    Matrix matrix = DownloadImageTask.imgMatrix;

    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
//    Bitmap mBitmap;
//    Bitmap imgBitmap;
    int imHeight;
    int imWidth;

    float[] values = new float[9];

    float globalX;
    float globalY;
    float width;
    float height;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        if (view.getScaleType() != ImageView.ScaleType.MATRIX) {
            Bitmap mBitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();

            view.setScaleType(ImageView.ScaleType.MATRIX);
            getStartMatrix(view);
            matrix.set(startMatrix);
            imWidth = mBitmap.getWidth();
            imHeight = mBitmap.getHeight();
//            matrix.postTranslate((float) (MainActivity.screenWidth - mBitmap.getWidth()) / 2, (float) (MainActivity.screenHeight - mBitmap.getHeight()) / 2);
            view.setImageMatrix(matrix);
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                if (getScaleX(matrix) > 1 || getScaleY(matrix) > 1) {
                    savedMatrix.set(matrix);
                    startPoint.set(event.getX(), event.getY());
                    mode = DRAG;
                }

                Log.d("Resolution", "MotionEvent.ACTION_DOWN");
                getMatrixValues(matrix);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (getScaleX(matrix) >= 1 || getScaleY(matrix) >= 1) {
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(midPoint, event);
                        mode = ZOOM;
                        Log.d("Resolution", "MotionEvent.ACTION_POINTER_DOWN");
                        getMatrixValues(matrix);
                    }
//                else {matrix.set(startMatrix);}
                }else {matrix.set(startMatrix);}
                break;

            case MotionEvent.ACTION_UP:
                if (getScaleX(matrix) < 1 || getScaleY(matrix) < 1) {
                    matrix.set(startMatrix);
                }
                Log.d("Resolution", "MotionEvent.ACTION_UP");
                getMatrixValues(matrix);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d("Resolution", "MotionEvent.ACTION_POINTER_UP");
                getMatrixValues(matrix);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
//                    if (getScaleX(matrix) > 1 || getScaleY(matrix) > 1) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);

                    Log.d("Resolution", "MotionEvent.ACTION_MOVE-yyyyyyy");
                    getMatrixValues(matrix);
//                    }

                } else if (mode == ZOOM) {
//                    if (getScaleX(matrix) >= 1 || getScaleY(matrix) >= 1) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        if (getScaleX(matrix) >= 1 && getScaleY(matrix) >= 1) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            Log.d("Resolution", "MotionEvent.ACTION_MOVE-wwwww" + String.valueOf(scale));
                            getMatrixValues(matrix);
                        } else {matrix.set(startMatrix);}
                    }
//                    }else {matrix.set(startMatrix);}
                }
                break;
        }
//        float[] values = new float[9];
//        matrix.getValues(values);
//        values[Matrix.MTRANS_Y] = (float)(MainActivity.screenHeight-height)/2;
//        values[Matrix.MTRANS_Y] = 100f;
//        matrix.setValues(values);
//        float globalX = values[Matrix.MTRANS_X];
//        float globalY = values[Matrix.MTRANS_Y];
//        float mwidth = values[Matrix.MSCALE_X] * width;
//        float mheight = values[Matrix.MSCALE_Y]*height;
//        Log.d("Resolution", "Matrix OnTouch: globalX: " + globalX + ", globalY: " + globalY + ", width: " + mwidth + ", height: " + mheight);
//        imgBitmap = Bitmap.createBitmap(mBitmap, (int)globalX, (int)globalY, (int)mwidth, (int)mheight, matrix, true);
//        imgBitmap = Bitmap.createScaledBitmap(mBitmap, imWidth*2, imHeight*2, true);

        view.setImageMatrix(matrix);
//        view.setImageBitmap(mBitmap);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void getMatrixValues(Matrix matrix) {
//        float[] values = new float[9];
        matrix.getValues(values);
        globalX = values[Matrix.MTRANS_X];
        globalY = values[Matrix.MTRANS_Y];
        width = values[Matrix.MSCALE_X] * imWidth;
        height = values[Matrix.MSCALE_Y] * imHeight;
        Log.d("Resolution", "Matrix: globalX: " + globalX + ", globalY: " + globalY + ", width: " + width + ", height: " + height);
    }

    private float getScaleX(Matrix matrix) {
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    private float getScaleY(Matrix matrix) {
        matrix.getValues(values);
        return values[Matrix.MSCALE_Y];
    }

    private void getStartMatrix(ImageView view) {
        Bitmap mBitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        startMatrix.postTranslate((float) (MainActivity.screenWidth - mBitmap.getWidth()) / 2, (float) (MainActivity.screenHeight - mBitmap.getHeight()) / 2);
        startMatrix.getValues(values);
        globalX = values[Matrix.MTRANS_X];
        globalY = values[Matrix.MTRANS_Y];
        width = values[Matrix.MSCALE_X] * imWidth;
        height = values[Matrix.MSCALE_Y] * imHeight;
        Log.d("Resolution", "StartMatrix: globalX: " + globalX + ", globalY: " + globalY + ", width: " + width + ", height: " + height);
    }
}