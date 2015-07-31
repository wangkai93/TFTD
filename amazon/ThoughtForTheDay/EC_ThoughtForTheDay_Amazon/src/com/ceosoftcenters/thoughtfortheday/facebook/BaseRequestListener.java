package com.ceosoftcenters.thoughtfortheday.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.ceosoftcenters.thoughtfortheday.facebook.AsyncFacebookRunner.RequestListener;


public abstract class BaseRequestListener implements RequestListener {

    public void onFacebookError(FacebookError e) {
//        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onFileNotFoundException(FileNotFoundException e) {
//        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onIOException(IOException e) {
//        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onMalformedURLException(MalformedURLException e) {
//        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }
    
}
