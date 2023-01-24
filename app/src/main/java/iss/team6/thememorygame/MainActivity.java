package iss.team6.thememorygame;

import static iss.team6.thememorygame.ImageLoader.saveImageToFile;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button fetchBtn;
    private EditText fetchUrl;
    private GridView gridView;
    private ImageLoader imageLoader;
    private ProgressBar progressBar;
    private Button restartBtn;
    private TextView progressTextView;
    private DialogFragment dialogFragment;
    private static MediaPlayerTool mp = null;

    private Map<Integer,String> saveImgMap = new HashMap();

    public Map<Integer, String> getSaveImgMap() {
        return saveImgMap;
    }

    //the inputUrl will be passed to the fetch method,which will later use Picasso lib to download the first 20 images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchUrl=(EditText)findViewById(R.id.fetchUrl);
        fetchBtn=(Button) findViewById(R.id.fetchBtn);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(20);
        progressTextView = findViewById(R.id.progressTextView);
        //progressTextView.setText("downloading %d images of 20 images");
        restartBtn=(Button) findViewById(R.id.restartBtn);
        gridView=(GridView)findViewById(R.id.girdView);//initialize all ui elements
        imageLoader = new ImageLoader(MainActivity.this);
        saveImgMap = new HashMap<>();
        fetchBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                fetch();
            }
         });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageAdapter adapter = (ImageAdapter) gridView.getAdapter();
                String imgurlpath= (String) adapter.getItem(position);

                if (!saveImgMap.containsKey(position)){

                    try {
                        if (mp != null) {
                            mp.stop();
                            mp = null;
                        }
                        mp = new MediaPlayerTool(MainActivity.this, R.raw.m212);
                    } catch (Exception e) {}

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String filename = "image" + UUID.randomUUID().toString();
                            Bitmap bitmap =imageLoader.downloadImage(imgurlpath);
                            saveImageToFile(bitmap, filename);
                            //download
                            saveImgMap.put(position,filename);
                            ((ImageView)view).setColorFilter(Color.argb(120, 255, 255, 255));
                            if (saveImgMap.size()>=6){
                                startGame();
                            }
                        }
                    }).start();

                }else{
                    Toast.makeText(MainActivity.this,"image downloaded",Toast.LENGTH_SHORT).show();
                }
            }
        });}

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void fetch() {
        String imageUrl=fetchUrl.getText().toString();
        new ImageUrlParser(20).execute(imageUrl);
    }
 public void startGame(){
    dialogFragment= new MyDialogFragment();
    dialogFragment.show(getSupportFragmentManager(),"MyDialogueFragment");
 }


private class ImageUrlParser extends AsyncTask<String,Integer,ArrayList<String>> {
    private int toaalImgs;
    int  imagesDownloaded =0;
    private Elements imgElement;
    public ImageUrlParser(int toaalImgs) {
        this.toaalImgs = toaalImgs;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String imgUrl=strings[0];
        imagesDownloaded = 0;

        ArrayList<String>imageUrls=new ArrayList<>(toaalImgs);
        try{
            Document doc=Jsoup.connect(imgUrl).get();
            Elements imgElement=doc.select("img");
            for(int i=0; i < imgElement.size();i++) {
                String src = imgElement.get(i).attr("src");
                if (src.endsWith("jpeg") || src.endsWith("jpg")) {
                    imageUrls.add(src);
                    imagesDownloaded++;
                    publishProgress(imagesDownloaded);
                    Thread.sleep(200);
                    if (imagesDownloaded>=toaalImgs){
                        break;
                    }
                }
            }
            }
        catch(MalformedURLException malformedURLException){
            malformedURLException.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return imageUrls;
    }
    @Override
    protected void onPostExecute(ArrayList<String> imageUrls) {
        super.onPostExecute(imageUrls);
        gridView.setAdapter(new ImageAdapter( imageUrls,MainActivity.this));
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressTextView.setText(String.valueOf(values[0])+"of 20 images downloaded");
        progressBar.setProgress(values[0]);
    }
}

}
