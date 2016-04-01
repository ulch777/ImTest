package fm.ua.ulch.imtest;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageOnTouchListener implements View.OnTouchListener {
    Matrix matrix = new Matrix();

    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        Log.d("Resolution", "Viev height: " + v.getHeight());
        Log.d("Resolution", "Viev width: " + v.getWidth());
//        System.out.println("matrix=" + savedMatrix.toString());

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                mode = DRAG;
                Log.d("Resolution", "MotionEvent.ACTION_DOWN");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(midPoint, event);
                    mode = ZOOM;
                    Log.d("Resolution", "MotionEvent.ACTION_POINTER_DOWN");
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.d("Resolution", "MotionEvent.ACTION_UP");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d("Resolution", "MotionEvent.ACTION_POINTER_UP");
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                    Log.d("Resolution", "MotionEvent.ACTION_MOVE-yyyyyyy");

                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
//                        Log.d("Resolution", "MotionEvent.ACTION_MOVE-wwwww" + String.valueOf(matrix.));
                        if(scale<1) {
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            Log.d("Resolution", "MotionEvent.ACTION_MOVE-wwwww" + String.valueOf(scale));
                        }
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
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
}