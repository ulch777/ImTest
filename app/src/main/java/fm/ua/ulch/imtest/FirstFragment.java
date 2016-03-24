package fm.ua.ulch.imtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        return view;

    }
}
