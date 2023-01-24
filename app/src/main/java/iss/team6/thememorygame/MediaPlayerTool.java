package iss.team6.thememorygame;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MediaPlayerTool 
{

	private MediaPlayer player = null ;
	private AudioManager amg = null ;
	
	public MediaPlayerTool(Context context ,int resouceID)
	{
		player = MediaPlayer.create(context, resouceID); 
		amg = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int max = amg.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) - 2;
		if(player  != null )
		{
			player.setVolume(max, max);
		}
		start();
	}
	public boolean getPlayingState()
	{
		if(player == null )
		{
			return false ;
		}
		return player.isPlaying() ;
	}
	public MediaPlayerTool(Context context ,String path)
	{
		player = new MediaPlayer();
		amg = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int max = amg.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
		player.setVolume(max, max);
		play(path);
	}
	/**
	 * 播放
	 */
	public void play(String path)
	{
		if(player == null)
		{
			return;
		}
		
		try 
		{ 
			player.reset();
			player.setDataSource(path);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *暂停
	 */
	public void pause()
	{
		if(player == null)
		{
			return;
		}
		player.pause();
	}
	/**
	 * 停止
	 */
	public void stop()
	{
		if(player == null)
		{
			return;
		}
		player.stop();
	}
	/**
	 * 开始
	 */
    public void start()
    {   if(player == null)
		{
			return;
		}
    	player.start();
    }
    /**
	 * 重新播放
	 */
    public void restart()
    {   if(player == null)
		{
			return;
		}
        player.seekTo(0);
    	player.start();
    }
    /**
     * 设置音量
     * 音量范围 0.0 ~ 1.0
     */
    public void setValume(float leftV ,float rightV)
    {
    	if(player != null)
    	{
    		player.setVolume(leftV, leftV);
    	}
    }
}
