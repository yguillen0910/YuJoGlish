package com.yujoglish;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.metaio.tools.io.AssetsManager;
import com.yujoglish.configuracion.usuario.SelectUserActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class FTPActivity extends Activity   {
    
	static final String FTP_HOST= "192.168.1.108";
	static final String FTP_USER = "yujoglish";
	static final String FTP_PASS  ="123456";
	public String fileNamePrincipal = "";
	/*
	 * Tiempo de espera
	 */
	private int leadTime = 800;
	
	Button btn, btn_download;
	 EditText eTFileName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftp);

        btn = (Button) findViewById(R.id.button1);
       
        btn_download = (Button) findViewById(R.id.button2);
        
        eTFileName = (EditText) findViewById(R.id.fileName);
        
        
        
    }

	public void uploadFile(File fileName){
    	
    	System.out.println("UPLOAD FILE "+fileName);
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		  
    	StrictMode.setThreadPolicy(policy);
		 FTPClient client = new FTPClient();
		try {
			client.connect(FTP_HOST,21);
			System.out.println("Conexion_?? "+client.isConnected());
			client.login(FTP_USER, FTP_PASS);
			
			client.setType(FTPClient.TYPE_BINARY);
			
			client.changeDirectory("/");
			client.upload(fileName, new MyTransferListener());
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				client.disconnect(true);	
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
    }
		
    public class MyTransferListener implements FTPDataTransferListener {

    	public void started() {
    		//btn.setVisibility(View.GONE);
    		// Transfer started
    		System.out.println(" Started ...");
    	}

    	public void transferred(int length) {
    		// Yet other length bytes has been transferred since the last time this
    		// method was called
    		System.out.println(" transferred ..." + length);
    	}

    	public void completed() {
    		//btn.setVisibility(View.VISIBLE);
    		// Transfer completed
    		
    		//runActivityTime();
    		System.out.println(" completed ..." );
    	}

    	public void aborted() {
    		//btn.setVisibility(View.VISIBLE);
    		// Transfer aborted
    		System.out.println(" aborted ..." );
    	}

    	public void failed() {
    		//btn.setVisibility(View.VISIBLE);
    		// Transfer failed
    		System.out.println(" failed ..." );
    	}

    }
    
    public boolean downloadStart(String fileNamePrincipal, String nombreLeccion) {
    	
    	System.out.println("DownloadStart...");
        
    	FTPClient ftp = new FTPClient();
        
        StrictMode.ThreadPolicy policy =  new StrictMode.ThreadPolicy.Builder().permitAll().build();
       
        StrictMode.setThreadPolicy(policy);
        
        try {
			ftp.connect(FTP_HOST,21);
			System.out.println("Conexion_?? "+ftp.isConnected());
	        
	        ftp.login(FTP_USER, FTP_PASS);
	        
	       File dirStructure = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/YuJoGlish/"+nombreLeccion);
	       dirStructure.mkdirs();
	       String fileName="";
	       fileName = fileNamePrincipal;
	       
	       File sdcardFileDownloadPath = new File(dirStructure, fileName);
	       System.out.println("fileDownload "+ sdcardFileDownloadPath.getAbsolutePath());
	       System.out.println(fileName);
	       ftp.download(fileName, sdcardFileDownloadPath, new MyTransferListener());
	       return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
      
 
    }

	
	public void onUploadButtonClick(View v){
		
		fileNamePrincipal = eTFileName.getText().toString();
		File f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/YuJoGlish/"+"shapes/"+fileNamePrincipal);
		uploadFile(f);
		
		
	}
	
	public void onDownloadButtonClick(View v){
		fileNamePrincipal = eTFileName.getText().toString();
		downloadStart(fileNamePrincipal, "shapes");
		
	}
	
	public void main() {
		Intent select = new Intent(this,MainActivity.class);
		startActivity(select);
	}
	
	/**
	 * M�todo para setear tiempo de espera 
	 * a la ejecuci�n de la tarea onRestart
	 */
	public void runActivityTime()
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				main();
			}
		}, leadTime);
	}

}