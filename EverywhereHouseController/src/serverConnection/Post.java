package serverConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import ehc.net.Main;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Post 
{	                        
	private static InputStream _is = null;		                         
	private static String _response = "";
	public final static String _ip = "http://5.231.69.226/EHControlConnect/index.php";
	//"http://192.168.2.207/EHControlConnect/index.php";
	//"http://192.168.1.43/EHControlConnect/index.php";

	public Post(){}
	
	/**
	 * Returns a JSONArray with the response to a query
	 * @param _parametros
	 * @param _URL
	 * @return
	 */
	public static JSONObject getServerData( ArrayList<String> parameters, String URL )
	{				 					 
		connectionPost( parameters, URL );
		
		if (_is != null) 
		{				                                                
			getResponsePost();
		}
		
		if ( _response != null ) 
		{		
			JSONObject _json = null;
			try 
			{
				_json = new JSONObject( _response );
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return _json;
		} 
		else 
		{				                                                                                               
			return null;				                                                                        
		}				                                                
	}
	
	/**
	 * 
	 * @param _parametros
	 * @param _URL
	 */
	private static void connectionPost( ArrayList<String> parametros, String URL ) 
	{				                         
		ArrayList<BasicNameValuePair> _nameValuePairs;	                         
		try 
		{                                              
			HttpClient _httpclient = new DefaultHttpClient();                                              
			HttpPost _httppost = new HttpPost( URL );				                                                
			//------------------------
			_httppost.setURI( new URI( URL ) );
			//-------------------------
			_nameValuePairs = new ArrayList<BasicNameValuePair>();			
			                                                
			if ( parametros != null ) 
			{				                                                                        
				for (int i = 0; i < parametros.size() - 1; i += 2) 
				{			 
					_nameValuePairs.add( new BasicNameValuePair( parametros.get( i ), parametros.get( i + 1 ) ) );				                                                                        
				}				 
				_httppost.setEntity( new UrlEncodedFormEntity( _nameValuePairs ) );				                                                
			}
					
			HttpResponse _response = _httpclient.execute( _httppost );
			HttpEntity _entity = _response.getEntity();
			_is = _entity.getContent();
			
		} 
		catch (Exception e) 
		{				                                                
			Log.e("log_tag", "Error in http connection " + e.toString() );				                         
		}			
	}

	/**
	 * Parsea la respuesta a la consulta y la mete en el string "respuesta"		 
	 */
	private static void getResponsePost() 
	{				 
		try 
		{				 
			BufferedReader _reader = new BufferedReader( new InputStreamReader( _is, "iso-8859-1"), 8 );				 
			StringBuilder _sb = new StringBuilder();				 
			String _line = null;
			 
			while ( ( _line = _reader.readLine() ) != null ) 
			{				                         
				_sb.append( _line + "\n" );	
				Log.e( "line", _line.toString() );
			}				 
			_is.close();				 
			_response = _sb.toString();			
		} 
		catch (Exception e) 
		{				                         
			Log.e( "log_tag", "Error converting result " + e.toString() );				                         
		}
			 
	}
	
	/**
	 * Convierte el string "respuesta" en un JSONArray
	 * @return
	 */
	@SuppressWarnings("unused")
	private JSONArray getJsonArray() 
	{				                         
		JSONArray _jArray = null;
		try 
		{	
			JSONObject _json = new JSONObject( _response );
			_jArray = _json.getJSONArray( "result" );
			
		} catch ( Exception e ) 
		{
			System.out.print( "ERROR:" + e );
		} 
						                                                
		return _jArray;		                         		 
	}
			 
	/**
	 * Method that encrypts the password
	 * @param s
	 * @return
	 */
	public static String md5( String s ) 
	{
		StringBuffer sb = new StringBuffer();
        try 
        {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
            final byte[] array = md.digest( s.getBytes( "UTF-8" ) );
            for ( int i = 0; i < array.length; ++i ) 
            {
                sb.append( Integer.toHexString( ( array[ i ] & 0xFF) | 0x100 ).substring( 1, 3 ) );
            }
            return sb.toString();
        } 
        catch ( Exception e ) 
        {
        	e.printStackTrace();
        }
        return sb.toString();
	}
	
	public static JSONObject connectionPostUpload( ArrayList<String> parametros, String URL, String imagePath )
	{	
		try
		{
			HttpClient _httpclient = new DefaultHttpClient();
			HttpPost _httppost = new HttpPost( URL );	
			_httppost.setURI( new URI( URL ) );

			
			File file = new File( imagePath );
						
			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody cbFile = new FileBody( file, "image/jpeg" );
			mpEntity.addPart( parametros.get( 0 ), new StringBody( parametros.get( 1 ) ) );
			mpEntity.addPart( "imagen", cbFile );

			_httppost.setEntity( mpEntity );  
			HttpResponse _response = _httpclient.execute( _httppost );
			HttpEntity _entity = _response.getEntity();
			_is = _entity.getContent();
		}
		catch ( Exception e )
		{	
			Log.e( "log_tag", "Error in http connection " + e.toString() );	
		}
		
		if (_is != null) 
		{				                                                
			getResponsePost();
		}
		if ( _response != null ) 
		{		
			JSONObject _json = null;
			try 
			{
				_json = new JSONObject( _response );
			} 
			catch ( JSONException e ) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return _json;
		} 
		else 
		{				                                                                                               
			return null;				                                                                        
		}		
	}
	
	// Background process
    public  static class logOutConnection extends AsyncTask<String, String, String>
    {   
    	private Context _context;
    	
    	public logOutConnection( Context context )
    	{
    		_context = context;
    	}
    	
    	@Override
		protected String doInBackground( String... params ) 
		{
			// TODO Auto-generated method stub			
			//Variable 'Data' saves the query response
    		SharedPreferences _pref = _context.getSharedPreferences( "LOG",Context.MODE_PRIVATE );
    		
    		ArrayList<String> _parametros = new ArrayList<String>();
    		
			_parametros.add( "command" );
			_parametros.add( "logout" );
			_parametros.add( "username" );
			_parametros.add( _pref.getString( "USER", "" ) );
			_parametros.add( "regid" );
			_parametros.add( _pref.getString( "ID", "" ) );
			
			 Editor _editor=_pref.edit();
	        _editor.putString( "ID", "" );
	        _editor.commit();
			
			getServerData( _parametros,_ip );
			
			return null;
		}  
    }
}
