package iss.team6.thememorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader extends AsyncTask<Object, Void, Bitmap> {
    ImageView imageView;
    private Context context;
    static String filename;
    static MainActivity mainActivity;

    public ImageLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }



    @Override
    protected Bitmap doInBackground(Object... objects) {
        imageView = (ImageView) objects[0];
        String imgurlpath = (String) objects[1];
        return downloadImage(imgurlpath);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
    public Bitmap downloadImage(String imgurlpath) {
        Bitmap bitmap = null;
        InputStream stream;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 4;
        try {
            URL url=new URL(imgurlpath);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("ImageLoader", "Error downloading image: " + httpConnection.getResponseCode());
                return null;
            }else {
                stream = httpConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
                int width = 100;
                int height = 100;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

     static void saveImageToFile(Bitmap bitmap,String filename){
         if (bitmap == null) {
             return;
         }
        Bitmap.CompressFormat compressFormat= Bitmap.CompressFormat.JPEG;
        int quality=100;
        OutputStream fout=null;
        //for(int i=0;i<6;i++) {
            try {
                //fout = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + filename + ".jpg");
                fout = new FileOutputStream(mainActivity.getFilesDir() + "/" + filename + ".jpg");
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
            bitmap.compress(compressFormat, quality, fout);
            try {
                fout.flush();
                fout.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        //}


    }
    public static Bitmap getBitmapFromFile(){
        try{
            return BitmapFactory.decodeStream(new FileInputStream(Environment.getExternalStorageDirectory().getPath()+"/"+filename+".jpg"));
        }
        catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        }
        return null;
    }
}
