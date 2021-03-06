package ehc.net;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;

import ehc.net.R;
import framework.ItemsFragment;
import adapters.SlidingMenuAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;

public class ItemsFragmentsContainer extends SherlockFragmentActivity
{
	//----------Variables------------
	private ViewPager _mPager;
	private ActionBar _ab;
	private HashMap<String,String> _tableButtons;
	private TabsAdapter _mTabsAdapter;
	private String _houseName;
	private ActionBarDrawerToggle _actbardrawertoggle;
	private DrawerLayout _dl;
	private ListView _drawer;
	// -------------------------------
	
	 @Override
	 protected void onCreate( Bundle savedInstanceState )
	 {
	    super.onCreate( savedInstanceState );
        setContentView( R.layout.pager_view);
        
        //----------------ActionBar-----
        _ab = getSupportActionBar();
        _ab.setHomeButtonEnabled( false );
        _ab.setDisplayHomeAsUpEnabled( true );
        _ab.setDisplayShowHomeEnabled( true );
        _ab.setDisplayUseLogoEnabled( false );
        _ab.setDisplayShowTitleEnabled( false );
		_ab.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
         //-------------------------------
		
        /////////////////////////////////////////////////////////////////////////////////////////
        _drawer = (ListView) findViewById( R.id.ListViewSlidingMenu );		
		final SlidingMenuAdapter _adapter = new 
				SlidingMenuAdapter( this.getBaseContext(), getIntent().getExtras().getString( "House" ) );
		_drawer.setAdapter( _adapter );
		_adapter.notifyDataSetChanged();
		 
		_dl = ( DrawerLayout ) findViewById( R.id.drawer_layout );
		
		_actbardrawertoggle = new ActionBarDrawerToggle( this, _dl, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close )
        {
			public void onDrawerClosed( View view ) 
			{
				super.onDrawerClosed( view );
			}

			public void onDrawerOpened( View drawerView ) 
			{
				super.onDrawerOpened( drawerView );
			
			}    	 
        };
        
        _dl.setDrawerListener( _actbardrawertoggle );	
		/////////////////////////////////////////////////////////////////////////////////////////
 
	        // Create a HashMap with < Key: position, Value button name >.
        _tableButtons = new HashMap<String,String>();
        
        
        for( int i = 0; i < getIntent().getExtras().getInt( "NumRooms" ); i++ )
        {
        	_tableButtons.put( Integer.toString( i ), getIntent().getExtras().getString(Integer.toString( i ) ) );
        }
       
    	// Link the XML which contains the pager.
        _mPager = ( ViewPager ) findViewById (R.id.pager );
        // Create an adapter with the fragments that will show on the ViewPager.
       _houseName = getIntent().getExtras().getString( "House" );
               
        _mTabsAdapter = new TabsAdapter( this, _mPager );
        
    	for( int i = 0; i < _tableButtons.size(); i++ )
    	{
    		_mTabsAdapter.addTab( _ab.newTab().setText( _tableButtons.get( Integer.toString( i ) ) ),null, null );
    	}
                
        // The position of the button that was clicked is obtained.
    	String buttonClicked = getIntent().getExtras().getString( "Room" );
    	String buttonPosition = getIntent().getExtras().getString( buttonClicked );
    		    	
        //Move the ViewPager to the desired view.
        _mPager.setCurrentItem( Integer.parseInt( buttonPosition ) );
        
	 }
	 
	 @Override
		public boolean onCreateOptionsMenu( Menu menu ) 
		{
			// TODO Auto-generated method stub
			return super.onCreateOptionsMenu( menu );
		}

		@Override
		public boolean onOptionsItemSelected( com.actionbarsherlock.view.MenuItem item ) 
		{
			// TODO Auto-generated method stub
			if( item.getItemId() == android.R.id.home )
			 {
				 if( _dl.isDrawerOpen( _drawer ) )
				 {
					 _dl.closeDrawer( _drawer );
				 }
				 else 
				 {
					_dl.openDrawer( _drawer );
				}
			 }
			return super.onOptionsItemSelected( item );
		}

		@Override
		protected void onPostCreate( Bundle savedInstanceState ) 
		{
			super.onPostCreate( savedInstanceState );
			_actbardrawertoggle.syncState();
		}

		@Override
		public void onConfigurationChanged( Configuration newConfig ) 
		{
			super.onConfigurationChanged( newConfig );
			_actbardrawertoggle.onConfigurationChanged( newConfig) ;
		}	
		
	 
	 private class TabsAdapter extends FragmentPagerAdapter implements
		ActionBar.TabListener, ViewPager.OnPageChangeListener
	{
		//----------Variables------------
		private final ActionBar _mActionBar;
		private final ViewPager _mViewPager;
		private final ArrayList<TabInfo> _mTabs = new ArrayList<TabInfo>();
		//----------------------
	
		final class TabInfo
		{
			//----------Variables------------
			private final Class<?> _clss;
			private final Bundle _args;
			//----------------------
	
			TabInfo( Class<?> _class, Bundle args )
			{
				_clss = _class;
				_args = args;
			}
		}
	
		public TabsAdapter( SherlockFragmentActivity activity, ViewPager pager )
		{
			super( activity.getSupportFragmentManager() );
			_mActionBar = activity.getSupportActionBar();
			_mViewPager = pager;
			_mViewPager.setAdapter( this );
			_mViewPager.setOnPageChangeListener( this );
		}
	
		public void addTab( ActionBar.Tab tab, Class<?> clss, Bundle args )
		{
			TabInfo _info = new TabInfo( clss, args );
			tab.setTag( _info );
			tab.setTabListener( this );
			_mTabs.add( _info );
			_mActionBar.addTab( tab );
			notifyDataSetChanged();
		}
	
		@Override
		public int getCount()
		{
			return _mTabs.size();
		}
	
		@Override
		public Fragment getItem( int position )
		{
			 return new ItemsFragment( ItemsFragmentsContainer.this, _tableButtons.get( String.valueOf( position ) ), _houseName );			
		}
	
		@Override
		public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels )
		{
		}
	
		@Override
		public void onPageSelected( int position )
		{
			_mActionBar.setSelectedNavigationItem( position );
		}
	
		@Override
		public void onPageScrollStateChanged( int state )
		{
		}
		
		@Override
		public void onTabSelected( Tab tab, FragmentTransaction ft )
		{
			Object _tag = tab.getTag();
			for ( int i = 0; i < _mTabs.size(); i++ )
			{
				if ( _mTabs.get( i ) == _tag )
				{
					_mViewPager.setCurrentItem( i );
				}
			}
		}
	
		@Override
		public void onTabUnselected( Tab tab, FragmentTransaction ft )
		{
		}
	
		@Override
		public void onTabReselected( Tab tab, FragmentTransaction ft )
		{
		}
	}	 
	    
	    @Override
	    public void onBackPressed() 
	    {
	    	// TODO Auto-generated method stub
	    	//super.onBackPressed();	    	
	    	SharedPreferences _pref = getSharedPreferences( "LOG",Context.MODE_PRIVATE );
			Editor _editor=_pref.edit();
			_editor.putString( "NEWACTIVITY", "OTHER" );
			_editor.commit();
	    	
	    	Class<?> _class = null;
			try 
			{
				_class = Class.forName( "ehc.net.ManagementMenu" );
			} catch ( ClassNotFoundException e ) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent _intent = new Intent( getApplicationContext(), _class );
			_intent.putExtra( "House" , _houseName );
			startActivity( _intent );
	    }
}
