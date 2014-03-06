package ehc.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenu extends Activity
{
	//---------Variables----------------
	private Button _buttonProfile;
	private Button _buttonManagement;
	private Button _buttonEvent;
	private Button _buttonConfig;
	private ImageView _logo;
	//-------------------------------
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_menu_view );
        /**
         * ------------------------------------
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         */
       // _buttonProfile = ( Button ) findViewById( R.id.buttonProfile );
        _buttonManagement = ( Button ) findViewById( R.id.buttonManagement );
       // _buttonEvent = ( Button ) findViewById( R.id.buttonEvent );
       // _buttonConfig = ( Button ) findViewById( R.id.buttonConfig );
        _logo = (ImageView) findViewById(R.id.world);
        
        Animation anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(anim);        
        
        
        _buttonManagement.setOnClickListener( new View.OnClickListener() 
        {
			
			@Override
			public void onClick( View v ) 
			{
				log( "Bot�n gesti�n pulsado" );
				createdManagementIntent();
			}
		});
                 
    }
	
	/**
	   * 
	   * @param is
	   * @return
	   * @throws IOException
	   */
	 public String convertStreamToString(InputStream is) throws IOException {
	           if (is != null) {
	                    StringBuilder sb = new StringBuilder();
	                    String line;
	                    try {
	                            BufferedReader reader = new BufferedReader(
	                                            new InputStreamReader(is, "UTF-8"));
	                            while ((line = reader.readLine()) != null) {
	                                    sb.append(line).append("\n");
	                            }
	                    }
	                    finally {
	                            is.close();
	                    }
	                    return sb.toString();
	            } else {
	                    return "";
	            }
	 }
	
	
	/**
	 * M�todo que ejecuta la activity gesti�n
	 */
	 
	private void createdManagementIntent()
	{
		try {
			Class<?> _clazz = Class.forName( "ehc.net.ManagementMenu");
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
	}
	
	
	 /**
     * M�todo para debugear
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d( "Acci�n :", _text );
    }
    
    
    protected void onResume()
    {
    	super.onResume();
    	log( "Resumed" );
    }
    
    protected void onPause()
    {
    	super.onPause();
    	log( "Paused" );
    }
    protected void onStop()
    {
    	super.onStop();
    	log( "Stoped" );
    }
}