package fm.ua.ulch.imtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FirstFragment extends Fragment {

    public static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private String imgURL;

    public static FirstFragment newInstance(String url) {

        FirstFragment pageFragment = new FirstFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PAGE_NUMBER, url);
        pageFragment.setArguments(arguments);

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgURL = getArguments().getString(ARGUMENT_PAGE_NUMBER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.first_fragment, container, false);

        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ProgressBar pb_horizontal = (ProgressBar) view.findViewById(R.id.pb_horizontal);
        pb_horizontal.setProgress(0);

//        textView.setText("Page " + imgURL);

        new DownloadImageTask(imgURL, img, textView, pb_horizontal).execute(imgURL);
//        img.setMaxHeight(1000);
//        img.setMaxWidth(1000);

//        Log.d("Resolution", "image: " + img.getWidth() + " x " + img.getHeight());


//        Matrix matrix = new Matrix();
//        matrix.reset();
//        matrix.setTranslate(MainActivity.screenWidth/2,MainActivity.screenHeight/2);
//        img.setImageMatrix(matrix);
        img.setOnTouchListener(new ImageOnTouchListener());
//        img.setScaleType(ImageView.ScaleType.CENTER);

        img.setOnClickListener(new onDoubleClickListener());


        return view;

    }

    private class onDoubleClickListener implements View.OnClickListener {
//
        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public void onSingleClick(View v){
            Toast.makeText(v.getContext(), "onSingleClick", Toast.LENGTH_SHORT).show();
        }

        public void onDoubleClick(View v) {
            Toast.makeText(v.getContext(), "onDoubleClick", Toast.LENGTH_SHORT).show();
            Log.d("onDoubleClick", "onDoubleClick");
//            ImageView view = (ImageView) v;
//            matrix.set(startMatrix);
//            view.setImageMatrix(matrix);
//            MainActivity.rlIndicstor.setVisibility(View.VISIBLE);
        }
    }
}
