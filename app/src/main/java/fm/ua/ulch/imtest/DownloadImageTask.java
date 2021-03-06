package fm.ua.ulch.imtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 21-Mar-16.
 */
public class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
    TextView textView;
    ImageView bmImage;
    String url;
    ProgressBar pb_horizontal;
    Matrix matrix = new Matrix();

    public DownloadImageTask(String url, ImageView bmImage, TextView textView, ProgressBar pb_horizontal) {
        this.url = url;
        this.bmImage = bmImage;
        this.textView = textView;
        this.pb_horizontal = pb_horizontal;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap image = downloadImage(urls[0]);
        return image;
    }

    protected void onPostExecute(Bitmap result) {
//        imgMatrix.reset();
        Log.d("Resolution", "image: " + result.getWidth() + " x " + result.getHeight());

        int width = result.getWidth();
        int height = result.getHeight();

        float scaleWidth = ((float) MainActivity.screenWidth) / width;
        float scaleHeight = ((float) MainActivity.screenHeight) / height;

        matrix.reset();
        matrix.setTranslate(MainActivity.screenWidth / 2, MainActivity.screenHeight / 2);

        float scale = (scaleWidth <= scaleHeight) ? scaleWidth : scaleHeight;
        matrix.postScale(scale, scale);

        Bitmap resizedResult = Bitmap.createBitmap(result, 0, 0, width, height, matrix, true);

        pb_horizontal.setVisibility(View.GONE);
//
//        float[] values = new float[9];
//        imgMatrix.getValues(values);
//        float globalX = values[Matrix.MTRANS_X];
//        float globalY = values[Matrix.MTRANS_Y];
//        float mwidth = values[Matrix.MSCALE_X] * resizedResult.getWidth();
//        float mheight = values[Matrix.MSCALE_Y] * resizedResult.getHeight();
//        Log.d("Resolution", "Matrix download: globalX: " + globalX + ", globalY: " + globalY + ", width: " + mwidth + ", height: " + mheight);

        bmImage.setImageBitmap(resizedResult);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pb_horizontal.setProgress(values[0]);
//        textView.setText("Loading... " + String.valueOf(values[0]));
//        Log.d("myLogs", "Loading... " + String.valueOf(values[0]));

    }

    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
//        imgMatrix.reset();
        pb_horizontal.setVisibility(View.VISIBLE);
        pb_horizontal.setProgress(0);

    }

    private Bitmap downloadImage(String urlString) {
        URL url;
        HttpURLConnection httpCon = null;
        InputStream is = null;
        ByteArrayOutputStream buffer = null;
        try {
            url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();

            is = httpCon.getInputStream();
            int fileLength = httpCon.getContentLength();
            buffer = new ByteArrayOutputStream();
            int nRead, totalBytesRead = 0;
            byte[] data = new byte[2048];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                totalBytesRead += nRead;
                double progress = (double) totalBytesRead / (double) fileLength * 100;
                publishProgress((int) progress);
            }
            byte[] image = buffer.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpCon.disconnect();
        }
        return null;
    }
}