package serverConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;
import android.util.Log;

public class Post 
{	                        
	private InputStream _is = null;		                         
	private String _response = "";

	public Post(){}
	
	/**
	 * Returns a JSONArray with the response to a query
	 * @param _parametros
	 * @param _URL
	 * @return
	 */
//	public JSONArray getServerData(ArrayList<String> parameters, String URL)
	public JSONObject getServerData(ArrayList<String> parameters, String URL)
	{				 					 
		connectionPost( parameters, URL );
		
		if (_is != null) 
		{				                                                
			getResponsePost();
		}
		
		if (_response != null /*&& response.trim() != ""*/) 
		{		
//			return getJsonArray();
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
	private void connectionPost(ArrayList<String> parametros, String URL) 
	{				                         
		ArrayList<BasicNameValuePair> _nameValuePairs;	                         
		try 
		{                                              
			HttpClient _httpclient = new DefaultHttpClient();                                              
			HttpPost _httppost = new HttpPost(URL);				                                                
			//------------------------
			_httppost.setURI( new URI(URL) );
			//-------------------------
			_nameValuePairs = new ArrayList<BasicNameValuePair>();			
			                                                
			if (parametros != null) 
			{				                                                                        
				for (int i = 0; i < parametros.size() - 1; i += 2) 
				{			 
					_nameValuePairs.add(new BasicNameValuePair( parametros.get(i), parametros.get(i + 1)));				                                                                        
				}				 
				_httppost.setEntity(new UrlEncodedFormEntity(_nameValuePairs));				                                                
			}
					
			HttpResponse _response = _httpclient.execute(_httppost);
			HttpEntity _entity = _response.getEntity();
			_is = _entity.getContent();
			
		} 
		catch (Exception e) 
		{				                                                
			Log.e("log_tag", "Error in http connection " + e.toString());				                         
		}			
	}

	/**
	 * Parsea la respuesta a la consulta y la mete en el string "respuesta"		 
	 */
	private void getResponsePost() 
	{				 
		try 
		{				 
			BufferedReader _reader = new BufferedReader( new InputStreamReader(_is, "iso-8859-1"), 8 );				 
			StringBuilder _sb = new StringBuilder();				 
			String _line = null;
			 
			while ((_line = _reader.readLine()) != null) 
			{				                         
				_sb.append(_line + "\n");	
				Log.e("line",_line.toString());
			}				 
			_is.close();				 
			_response = _sb.toString();				 
			//Log.e("log_tag", "String JSon " + response.toString());
			
		} 
		catch (Exception e) 
		{				                         
			Log.e("log_tag", "Error converting result " + e.toString());				                         
		}
			 
	}
	
	/**
	 * Convierte el string "respuesta" en un JSONArray
	 * @return
	 */
//	private JSONArray getJsonArray() 
//	{				                         
//		JSONArray _jArray = null;
//		try 
//		{	
//			JSONObject _json = new JSONObject( _response );
//			_jArray = _json.getJSONArray( "result" );
//			
//		} catch ( Exception e ) 
//		{
//			System.out.print( "ERROR:" + e );
//		} 
//						                                                
//		return _jArray;		                         		 
//	}
			 
	/**
	 * Method that encrypts the password
	 * @param s
	 * @return
	 */
	public String md5(String s) 
	{
		StringBuffer sb = new StringBuffer();
        try 
        {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(s.getBytes("UTF-8"));
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        return sb.toString();
	}
	
	public JSONObject connectionPostUpload(ArrayList<String> parametros, String URL, String imagePath)
	{	
		try
		{
			HttpClient _httpclient = new DefaultHttpClient();
			HttpPost _httppost = new HttpPost(URL);	
			//------------------------
			_httppost.setURI( new URI(URL) );
			//-------------------------

//			imagePath.replace(" ", "");
			File file = new File(imagePath);
			
//        	Intent photoPickerIntent = new Intent(
//                    Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            // photoPickerIntent.setType("image/*");
//            photoPickerIntent.putExtra("crop", "true");
//            photoPickerIntent.putExtra("outputX", 512);
//            photoPickerIntent.putExtra("outputY", 512);
//            photoPickerIntent.putExtra("aspectX", 1);
//            photoPickerIntent.putExtra("aspectY", 1);
//            photoPickerIntent.putExtra("scale", true);
//            File file = new File(android.os.Environment
//                    .getExternalStoragePublicDirectory(f.getParent()), f.getName());
			
			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody cbFile = new FileBody(file, "image/jpeg");
			mpEntity.addPart(parametros.get(0), new StringBody(parametros.get(1)));
			mpEntity.addPart("imagen", cbFile);

			_httppost.setEntity(mpEntity);  
			HttpResponse _response = _httpclient.execute(_httppost);
			HttpEntity _entity = _response.getEntity();
			_is = _entity.getContent();
		}
		catch (Exception e)
		{	
			Log.e("log_tag", "Error in http connection " + e.toString());	
		}
		
		if (_is != null) 
		{				                                                
			getResponsePost();
		}
		if (_response != null ) 
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

}