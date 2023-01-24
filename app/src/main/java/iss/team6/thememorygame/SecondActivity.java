package iss.team6.thememorygame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {

    private GameLogic gameLogic;
    private Handler mHandler = new Handler();
    private Map<String, Bitmap> cardImgMap;

    public class ImageClickListener implements View.OnClickListener {
      @Override
      public void onClick(View view) {
          Card[] cards=null;
          int [] coordinate=(int[])view.getTag();
          int x=coordinate[0];
          int y=coordinate[1];
          boolean status=gameLogic.canSwapCard(x,y);
          if(status==true)
          {
              cards=gameLogic.swapAndMatchCard(x,y);
              if(cards != null)
              {
                  Card currentCard = gameLogic.getCardByPosition(x, y);
                  String showCardName=currentCard.getImageName();
                  ImageView imageView = (ImageView) view;
                  imageView.setImageBitmap(cardImgMap.get(showCardName));
                  if(cards.length == 2)
                  {
                      if(cards[0].isRemoved())
                      {
                          Runnable mUpdateTimeTask = new Runnable() {
                              public void run() {
                                  int firstCardX=gameLogic.getFirstSwappedCard().getX();
                                  int firstCardY=gameLogic.getFirstSwappedCard().getY();
                                  GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
                                  ImageView firstCardView=getImageView(firstCardX* gridLayout.getColumnCount()
                                          +firstCardY);
                                  firstCardView.setVisibility(View.INVISIBLE);
                                  imageView.setVisibility(View.INVISIBLE);
                                  TextView textView=findViewById(R.id.score);
                                  String marks=gameLogic.getUpdatedScore();
                                  textView.setText(marks);
                                  if(gameLogic.isGameEnded())
                                  {
                                      Context context = getApplicationContext();
                                      Chronometer chronometer =  (Chronometer) findViewById(R.id.chronometer);
                                      chronometer.stop();
                                      String time=chronometer.getText().toString();
                                      CharSequence text = "you complete the game in "+time;
                                      int duration = Toast.LENGTH_SHORT;
                                      Toast toast = Toast.makeText(context, text, duration);
                                      toast.show();
                                      finish();
                                  }
                                  else
                                  {
                                      gameLogic.resetState();
                                  }
                              }
                          };
                          mHandler.removeCallbacks(mUpdateTimeTask);
                          mHandler.postDelayed(mUpdateTimeTask, 2000);
                      }
                      else
                      {
                          Runnable mUpdateTimeTask = new Runnable() {
                              public void run() {
                                  int firstCardX=gameLogic.getFirstSwappedCard().getX();
                                  int firstCardY=gameLogic.getFirstSwappedCard().getY();
                                  GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
                                  ImageView firstCardView=getImageView(firstCardX* gridLayout.getColumnCount()
                                          +firstCardY);
                                  setPlaceholder(firstCardView);
                                  ImageView secondCardView = (ImageView) view;
                                  setPlaceholder(secondCardView);
                                  gameLogic.resetState();
                              }
                          };
                          mHandler.removeCallbacks(mUpdateTimeTask);
                          mHandler.postDelayed(mUpdateTimeTask, 2000);

                      }
                  }
              }


          }
      }
    }

    private void initCardImageMap(String[] cardImgNames)
    {
        cardImgMap = new HashMap();
        for(String imgName: cardImgNames)
        {

            String imgPath = getFilesDir() + "/" + imgName + ".jpg";
            File imgFile = new  File(imgPath);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                cardImgMap.put(imgName, myBitmap);

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String[] ImageNameList=intent.getStringArrayExtra("ImgPaths");
        initCardImageMap(ImageNameList);
        gameLogic=new GameLogic(ImageNameList);
        setContentView(R.layout.activity_second);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        final int childCount=gridLayout.getChildCount();
        for(int i=0;i<childCount;i++)
        {
            int [] position={i/gridLayout.getColumnCount(),i%gridLayout.getColumnCount()};
            ImageView imageView=(ImageView)gridLayout.getChildAt(i);
            imageView.setOnClickListener(new ImageClickListener());
            imageView.setTag(position);
        }
        Chronometer chronometer =  (Chronometer) findViewById(R.id.chronometer);

        chronometer.start();
    }

    public ImageView getImageView(int position)
    {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        ImageView imageView=(ImageView)gridLayout.getChildAt(position);
        return imageView;
    }

    public void setPlaceholder(ImageView imageView)
    {
        int drawableId=getResources().getIdentifier("card_back","drawable",getPackageName());
        imageView.setImageResource(drawableId);
    }

}