package fm.ua.ulch.imtest;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageOnTouchListener implements View.OnTouchListener/*, View.OnClickListener */{
    Matrix startMatrix = new Matrix();
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    int imHeight;
    int imWidth;

    float[] values = new float[9];

    float globalX;
    float globalY;
    float width;
    float height;

//    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
//    long lastClickTime = 0;

    //    GestureDetector gD;
//    GestureDetector gestureDetector;
//    boolean tapped;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final ImageView view = (ImageView) v;
        if (view.getScaleType() != ImageView.ScaleType.MATRIX) {

            try {
                Bitmap mBitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
                view.setScaleType(ImageView.ScaleType.MATRIX);
                getStartMatrix(view);
                matrix.set(startMatrix);
                imWidth = mBitmap.getWidth();
                imHeight = mBitmap.getHeight();
                view.setImageMatrix(matrix);
            } catch (Exception e) {
                Log.d("Exeption", "No downloaded image", e);
            }
//            gD = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
//                @Override
//                public boolean onDoubleTap(MotionEvent e){
//
//                    return true;
//                }
//            });
//            view.setOnClickListener(new onDoubleClickListener() {
//                @Override
//                public void onSingleClick(View v) {
//
//                }
//
//                @Override
//                public void onDoubleClick(View v) {
//                    matrix.set(startMatrix);
//                    view.setImageMatrix(matrix);
//                    MainActivity.rlIndicstor.setVisibility(View.VISIBLE);
//                }
//            });
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
                } else {
                    matrix.set(startMatrix);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (getScaleX(matrix) < 1 || getScaleY(matrix) < 1) {
                    matrix.set(startMatrix);
                }
                Log.d("Resolution", "MotionEvent.ACTION_UP");
                getMatrixValues(matrix);
                if (getScaleX(matrix) <= 1 || getScaleY(matrix) <= 1) {
                    MainActivity.rlIndicstor.setVisibility(View.VISIBLE);
                } else {
                    if (getScaleX(matrix) > 1 || getScaleY(matrix) > 1) {
                        MainActivity.rlIndicstor.setVisibility(View.GONE);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d("Resolution", "MotionEvent.ACTION_POINTER_UP");
                getMatrixValues(matrix);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);

                    Log.d("Resolution", "MotionEvent.ACTION_MOVE-yyyyyyy");

                    getMatrixValues(matrix);

                } else if (mode == ZOOM) {
                    float newDist = 0;
                    try {
                        newDist = spacing(event);
                    } catch (Exception e) {
                        Log.d("Exeption", "Zooming + pagechanging", e);
                    }
                    if (newDist > 10f) {
                        if (getScaleX(matrix) >= 1 && getScaleY(matrix) >= 1) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            Log.d("Resolution", "MotionEvent.ACTION_MOVE-wwwww" + String.valueOf(scale));
                            getMatrixValues(matrix);
                        } else {
                            matrix.set(startMatrix);
                        }
                    }
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
//        return gestureDetector.onTouchEvent(event);
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

//    @Override
//    public void onClick(View v) {
//        long clickTime = System.currentTimeMillis();
//        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
//            onDoubleClick(v);
//        } else {
//            onSingleClick(v);
//        }
//        lastClickTime = clickTime;
//    }
//
//    private void onDoubleClick(View v) {
//        Toast.makeText(v.getContext(), "onDoubleClick", Toast.LENGTH_SHORT).show();
//        Log.d("onDoubleClick", "onDoubleClick");
//    }
//    private void onSingleClick(View v) {
//        Toast.makeText(v.getContext(), "onSingleClick", Toast.LENGTH_SHORT).show();
//
//    }
//    private abstract class onDoubleClickListener implements View.OnClickListener {
//
//        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
//
//        long lastClickTime = 0;
//
//        @Override
//        public void onClick(View v) {
//            long clickTime = System.currentTimeMillis();
//            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
//                onDoubleClick(v);
//            } else {
//                onSingleClick(v);
//            }
//            lastClickTime = clickTime;
//        }
//
//        public abstract void onSingleClick(View v);
//
//        public abstract void onDoubleClick(View v);
////            ImageView view = (ImageView) v;
////            matrix.set(startMatrix);
////            view.setImageMatrix(matrix);
////            MainActivity.rlIndicstor.setVisibility(View.VISIBLE);
//
//    }
}