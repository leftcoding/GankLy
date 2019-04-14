/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.superplayer.library.mediaplayer;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;
public class IjkVideoView extends FrameLayout implements
		MediaController.MediaPlayerControl {
	private String TAG = "IjkVideoView";
	// settable by the client
	private Uri mUri;
	private Map<String, String> mHeaders;

	// all possible internal states
	public static final int STATE_ERROR = -1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_PREPARING = 1;
	public static final int STATE_PREPARED = 2;
	public static final int STATE_PLAYING = 3;
	public static final int STATE_PAUSED = 4;
	public static final int STATE_PLAYBACK_COMPLETED = 5;

	public int getCurrentState() {
		return mCurrentState;
	}

	// mCurrentState is a VideoView object's current state.
	// mTargetState is the state that a method caller intends to reach.
	// For instance, regardless the VideoView object's current state,
	// calling pause() intends to bring the object to a target state
	// of STATE_PAUSED.
	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;

	// All the stuff we need for playing and showing a video
	private IRenderView.ISurfaceHolder mSurfaceHolder = null;
	private IMediaPlayer mMediaPlayer = null;
	// private int mAudioSession;
	private int mVideoWidth;
	private int mVideoHeight;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private int mVideoRotationDegree;
	private IMediaController mMediaController;
	private IMediaPlayer.OnCompletionListener mOnCompletionListener;
	private IMediaPlayer.OnPreparedListener mOnPreparedListener;
	private int mCurrentBufferPercentage;
	private IMediaPlayer.OnErrorListener mOnErrorListener;
	private IMediaPlayer.OnInfoListener mOnInfoListener;
	private long mSeekWhenPrepared; // recording the seek position while
									// preparing
	private boolean mCanPause = true;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;

	/** Subtitle rendering widget overlaid on top of the video. */
	// private RenderingWidget mSubtitleWidget;

	/**
	 * Listener for changes to subtitle data, used to redraw when needed.
	 */
	// private RenderingWidget.OnChangedListener mSubtitlesChangedListener;

	private Context mAppContext;
	private IRenderView mRenderView;
	private int mVideoSarNum;
	private int mVideoSarDen;
	private boolean usingAndroidPlayer = false;
	private boolean usingMediaCodec = false;
	private boolean usingMediaCodecAutoRotate = false;
	private boolean usingOpenSLES = false;
	private String pixelFormat = "";// Auto Select=,RGB 565=fcc-rv16,RGB
									// 888X=fcc-rv32,YV12=fcc-yv12,默认为RGB 888X
	private boolean enableBackgroundPlay = false;
	private boolean enableSurfaceView = true;
	private boolean enableTextureView = false;
	private boolean enableNoView = false;

	public IjkVideoView(Context context) {
		super(context);
		initVideoView(context);
	}

	public IjkVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVideoView(context);
	}

	public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initVideoView(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initVideoView(context);
	}

	// REMOVED: onMeasure
	// REMOVED: onInitializeAccessibilityEvent
	// REMOVED: onInitializeAccessibilityNodeInfo
	// REMOVED: resolveAdjustedSize

	private void initVideoView(Context context) {
		mAppContext = context.getApplicationContext();

		initBackground();
		initRenders();

		mVideoWidth = 0;
		mVideoHeight = 0;
		// REMOVED: getHolder().addCallback(mSHCallback);
		// REMOVED:
		// getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		// REMOVED: mPendingSubtitleTracks = new Vector<Pair<InputStream,
		// MediaFormat>>();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
	}

	public void setRenderView(IRenderView renderView) {
		if (mRenderView != null) {
			if (mMediaPlayer != null)
				mMediaPlayer.setDisplay(null);

			View renderUIView = mRenderView.getView();
			mRenderView.removeRenderCallback(mSHCallback);
			mRenderView = null;
			removeView(renderUIView);
		}

		if (renderView == null)
			return;

		mRenderView = renderView;
		renderView.setAspectRatio(mCurrentAspectRatio);
		if (mVideoWidth > 0 && mVideoHeight > 0)
			renderView.setVideoSize(mVideoWidth, mVideoHeight);
		if (mVideoSarNum > 0 && mVideoSarDen > 0)
			renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);

		View renderUIView = mRenderView.getView();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		renderUIView.setLayoutParams(lp);
		addView(renderUIView);

		mRenderView.addRenderCallback(mSHCallback);
		mRenderView.setVideoRotation(mVideoRotationDegree);
	}

	public void setRender(int render) {
		switch (render) {
		case RENDER_NONE:
			setRenderView(null);
			break;
		case RENDER_TEXTURE_VIEW: {
			TextureRenderView renderView = new TextureRenderView(getContext());
			if (mMediaPlayer != null) {
				renderView.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer);
				renderView.setVideoSize(mMediaPlayer.getVideoWidth(),
						mMediaPlayer.getVideoHeight());
				renderView.setVideoSampleAspectRatio(
						mMediaPlayer.getVideoSarNum(),
						mMediaPlayer.getVideoSarDen());
				renderView.setAspectRatio(mCurrentAspectRatio);
			}
			setRenderView(renderView);
			break;
		}
		case RENDER_SURFACE_VIEW: {
			SurfaceRenderView renderView = new SurfaceRenderView(getContext());
			setRenderView(renderView);
			break;
		}
		default:
			Log.e(TAG, String.format(Locale.getDefault(),
					"invalid render %d\n", render));
			break;
		}
	}

	/**
	 * Sets video path.
	 * 
	 * @param path
	 *            the path of the video.
	 */
	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}

	/**
	 * Sets video URI.
	 * 
	 * @param uri
	 *            the URI of the video.
	 */
	public void setVideoURI(Uri uri) {
		setVideoURI(uri, null);
	}

	/**
	 * Sets video URI using specific headers.
	 * 
	 * @param uri
	 *            the URI of the video.
	 * @param headers
	 *            the headers for the URI request. Note that the cross domain
	 *            redirection is allowed by default, but that can be changed
	 *            with key/value pairs through the headers parameter with
	 *            "android-allow-cross-domain-redirect" as the key and "0" or
	 *            "1" as the value to disallow or allow cross domain
	 *            redirection.
	 */
	private void setVideoURI(Uri uri, Map<String, String> headers) {
		mUri = uri;
		mHeaders = headers;
		mSeekWhenPrepared = 0;
		openVideo();
		requestLayout();
		invalidate();
	}

	// REMOVED: addSubtitleSource
	// REMOVED: mPendingSubtitleTracks

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
			AudioManager am = (AudioManager) mAppContext
					.getSystemService(Context.AUDIO_SERVICE);
			am.abandonAudioFocus(null);
		}
	}

	private void openVideo() {
		if (mUri == null || mSurfaceHolder == null) {
			// not ready for playback just yet, will try again later
			return;
		}
		// we shouldn't clear the target state, because somebody might have
		// called start() previously
		release(false);

		AudioManager am = (AudioManager) mAppContext
				.getSystemService(Context.AUDIO_SERVICE);
		am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);

		try {
			if (usingAndroidPlayer) {
				mMediaPlayer = new AndroidMediaPlayer();
			} else {
				IjkMediaPlayer ijkMediaPlayer = null;
				if (mUri != null) {
					ijkMediaPlayer = new IjkMediaPlayer();
					//TODO 设置是否打印debug （调试的时候使用IjkMediaPlayer.IJK_LOG_DEBUG，上线的时候使用IjkMediaPlayer.IJK_LOG_ERROR）
					ijkMediaPlayer
							.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

					if (usingMediaCodec) {
						ijkMediaPlayer.setOption(
								IjkMediaPlayer.OPT_CATEGORY_PLAYER,
								"mediacodec", 1);
						if (usingMediaCodecAutoRotate) {
							ijkMediaPlayer.setOption(
									IjkMediaPlayer.OPT_CATEGORY_PLAYER,
									"mediacodec-auto-rotate", 1);
						} else {
							ijkMediaPlayer.setOption(
									IjkMediaPlayer.OPT_CATEGORY_PLAYER,
									"mediacodec-auto-rotate", 0);
						}
					} else {
						ijkMediaPlayer.setOption(
								IjkMediaPlayer.OPT_CATEGORY_PLAYER,
								"mediacodec", 0);
					}

					if (usingOpenSLES) {
						ijkMediaPlayer.setOption(
								IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles",
								1);
					} else {
						ijkMediaPlayer.setOption(
								IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles",
								0);
					}

					if (TextUtils.isEmpty(pixelFormat)) {
						ijkMediaPlayer.setOption(
								IjkMediaPlayer.OPT_CATEGORY_PLAYER,
								"overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
					} else {
						ijkMediaPlayer.setOption(
								IjkMediaPlayer.OPT_CATEGORY_PLAYER,
								"overlay-format", pixelFormat);
					}
					ijkMediaPlayer.setOption(
							IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
					ijkMediaPlayer.setOption(
							IjkMediaPlayer.OPT_CATEGORY_PLAYER,
							"start-on-prepared", 0);

					ijkMediaPlayer.setOption(
							IjkMediaPlayer.OPT_CATEGORY_FORMAT,
							"http-detect-range-support", 0);
					ijkMediaPlayer.setOption(
							IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout",
							10000000);
					ijkMediaPlayer.setOption(
							IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);

					ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC,
							"skip_loop_filter", 48);
				}
				mMediaPlayer = ijkMediaPlayer;
			}

			if (enableBackgroundPlay) {
				mMediaPlayer = new TextureMediaPlayer(mMediaPlayer);
			}

			// TODO: create SubtitleController in MediaPlayer, but we need
			// a context for the subtitle renderers
			final Context context = getContext();
			// REMOVED: SubtitleController

			// REMOVED: mAudioSession
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnInfoListener(mInfoListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mCurrentBufferPercentage = 0;
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				mMediaPlayer.setDataSource(mAppContext, mUri, mHeaders);
			} else {
				mMediaPlayer.setDataSource(mUri.toString());
			}
			bindSurfaceHolder(mMediaPlayer, mSurfaceHolder);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();

			// REMOVED: mPendingSubtitleTracks

			// we don't set the target state here either, but preserve the
			// target state that was there before.
			mCurrentState = STATE_PREPARING;
			attachMediaController();
		} catch (IOException ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IllegalArgumentException ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} finally {
			// REMOVED: mPendingSubtitleTracks.clear();
		}
	}

	public void setMediaController(IMediaController controller) {
		if (mMediaController != null) {
			mMediaController.hide();
		}
		mMediaController = controller;
		attachMediaController();
	}

	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			View anchorView = this.getParent() instanceof View ? (View) this
					.getParent() : this;
			mMediaController.setAnchorView(anchorView);
			mMediaController.setEnabled(isInPlaybackState());
		}
	}

	IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(IMediaPlayer mp, int width, int height,
				int sarNum, int sarDen) {
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			mVideoSarNum = mp.getVideoSarNum();
			mVideoSarDen = mp.getVideoSarDen();
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				if (mRenderView != null) {
					mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
					mRenderView.setVideoSampleAspectRatio(mVideoSarNum,
							mVideoSarDen);
				}
				// REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				requestLayout();
			}
		}
	};

	IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
		public void onPrepared(IMediaPlayer mp) {
			mCurrentState = STATE_PREPARED;

			// Get the capabilities of the player for this stream
			// REMOVED: Metadata

			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(mMediaPlayer);
			}
			if (mMediaController != null) {
				mMediaController.setEnabled(true);
			}
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();

			long seekToPosition = mSeekWhenPrepared; // mSeekWhenPrepared may be
														// changed after
														// seekTo() call
			if (seekToPosition != 0) {
				seekTo((int) seekToPosition);
			}
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				// Log.i("@@@@", "video size: " + mVideoWidth +"/"+
				// mVideoHeight);
				// REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				if (mRenderView != null) {
					mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
					mRenderView.setVideoSampleAspectRatio(mVideoSarNum,
							mVideoSarDen);
					if (!mRenderView.shouldWaitForResize()
							|| mSurfaceWidth == mVideoWidth
							&& mSurfaceHeight == mVideoHeight) {
						// We didn't actually change the size (it was already at
						// the size
						// we need), so we won't get a "surface changed"
						// callback, so
						// start the video here instead of in the callback.
						if (mTargetState == STATE_PLAYING) {
							start();
							if (mMediaController != null) {
								mMediaController.show();
							}
						} else if (!isPlaying()
								&& (seekToPosition != 0 || getCurrentPosition() > 0)) {
							if (mMediaController != null) {
								// Show the media controls when we're paused
								// into a video and make 'em stick.
								mMediaController.show(0);
							}
						}
					}
				}
			} else {
				// We don't know the video size yet, but should start anyway.
				// The video size might be reported to us later.
				if (mTargetState == STATE_PLAYING) {
					start();
				}
			}
		}
	};

	private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
		public void onCompletion(IMediaPlayer mp) {
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			if (mMediaController != null) {
				mMediaController.hide();
			}
			if (mOnCompletionListener != null) {
				mOnCompletionListener.onCompletion(mMediaPlayer);
			}
		}
	};

	private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() {
		public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
			if (mOnInfoListener != null) {
				mOnInfoListener.onInfo(mp, arg1, arg2);
			}
			switch (arg1) {
			case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
				mVideoRotationDegree = arg2;
				Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
				if (mRenderView != null)
					mRenderView.setVideoRotation(arg2);
				break;
			}
			return true;
		}
	};

	private IMediaPlayer.OnErrorListener mErrorListener = new IMediaPlayer.OnErrorListener() {
		public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
			Log.d(TAG, "Error: " + framework_err + "," + impl_err);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null) {
				mMediaController.hide();
			}

			/* If an error handler has been supplied, use it and finish. */
			if (mOnErrorListener != null) {
				if (mOnErrorListener.onError(mMediaPlayer, framework_err,
						impl_err)) {
					return true;
				}
			}

			/*
			 * Otherwise, pop up an error dialog so the user knows that
			 * something bad has happened. Only try and pop up the dialog if
			 * we're attached to a window. When we're going away and no longer
			 * have a window, don't bother showing the user an error.
			 */
			if (getWindowToken() != null) {
				Resources r = mAppContext.getResources();
				String message = "Unknown error";

				if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
					message = "Invalid progressive playback";
				}

				new AlertDialog.Builder(getContext())
						.setMessage(message)
						.setPositiveButton("error",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										/*
										 * If we get here, there is no onError
										 * listener, so at least inform them
										 * that the video is over.
										 */
										if (mOnCompletionListener != null) {
											mOnCompletionListener
													.onCompletion(mMediaPlayer);
										}
									}
								}).setCancelable(false).show();
			}
			return true;
		}
	};

	private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
		public void onBufferingUpdate(IMediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
		}
	};

	/**
	 * Register a callback to be invoked when the media file is loaded and ready
	 * to go.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
		mOnPreparedListener = l;
	}

	/**
	 * Register a callback to be invoked when the end of a media file has been
	 * reached during playback.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
		mOnCompletionListener = l;
	}

	/**
	 * Register a callback to be invoked when an error occurs during playback or
	 * setup. If no listener is specified, or if the listener returned false,
	 * VideoView will inform the user of any errors.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
		mOnErrorListener = l;
	}

	/**
	 * Register a callback to be invoked when an informational event occurs
	 * during playback or setup.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
		mOnInfoListener = l;
	}

	// REMOVED: mSHCallback
	private void bindSurfaceHolder(IMediaPlayer mp,
			IRenderView.ISurfaceHolder holder) {
		if (mp == null)
			return;

		if (holder == null) {
			mp.setDisplay(null);
			return;
		}

		holder.bindToMediaPlayer(mp);
	}

	IRenderView.IRenderCallback mSHCallback = new IRenderView.IRenderCallback() {
		@Override
		public void onSurfaceChanged(
				@NonNull IRenderView.ISurfaceHolder holder, int format, int w,
				int h) {
			if (holder.getRenderView() != mRenderView) {
				Log.e(TAG, "onSurfaceChanged: unmatched render callback\n");
				return;
			}

			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = !mRenderView.shouldWaitForResize()
					|| (mVideoWidth == w && mVideoHeight == h);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				if (mSeekWhenPrepared != 0) {
					seekTo((int) mSeekWhenPrepared);
				}
				start();
			}
		}

		@Override
		public void onSurfaceCreated(
				@NonNull IRenderView.ISurfaceHolder holder, int width,
				int height) {
			if (holder.getRenderView() != mRenderView) {
				Log.e(TAG, "onSurfaceCreated: unmatched render callback\n");
				return;
			}

			mSurfaceHolder = holder;
			if (mMediaPlayer != null)
				bindSurfaceHolder(mMediaPlayer, holder);
			else
				openVideo();
		}

		@Override
		public void onSurfaceDestroyed(
				@NonNull IRenderView.ISurfaceHolder holder) {
			if (holder.getRenderView() != mRenderView) {
				Log.e(TAG, "onSurfaceDestroyed: unmatched render callback\n");
				return;
			}

			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			// REMOVED: if (mMediaController != null) mMediaController.hide();
			// REMOVED: release(true);
			releaseWithoutStop();
		}
	};

	public void releaseWithoutStop() {
		if (mMediaPlayer != null)
			mMediaPlayer.setDisplay(null);
	}

	/*
	 * release the media player in any state
	 */
	public void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			// REMOVED: mPendingSubtitleTracks.clear();
			mCurrentState = STATE_IDLE;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}
			AudioManager am = (AudioManager) mAppContext
					.getSystemService(Context.AUDIO_SERVICE);
			am.abandonAudioFocus(null);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK
				&& keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
				&& keyCode != KeyEvent.KEYCODE_VOLUME_MUTE
				&& keyCode != KeyEvent.KEYCODE_MENU
				&& keyCode != KeyEvent.KEYCODE_CALL
				&& keyCode != KeyEvent.KEYCODE_ENDCALL;
		if (isInPlaybackState() && isKeyCodeSupported
				&& mMediaController != null) {
			if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
					mMediaController.show();
				} else {
					start();
					mMediaController.hide();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
				if (!mMediaPlayer.isPlaying()) {
					start();
					mMediaController.hide();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
					mMediaController.show();
				}
				return true;
			} else {
				toggleMediaControlsVisiblity();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void toggleMediaControlsVisiblity() {
		if (mMediaController.isShowing()) {
			mMediaController.hide();
		} else {
			mMediaController.show();
		}
	}

	@Override
	public void start() {
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
		mTargetState = STATE_PLAYING;
	}

	@Override
	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
		mTargetState = STATE_PAUSED;
	}

	public void suspend() {
		release(false);
	}

	public void resume() {
		openVideo();
	}

	@Override
	public int getDuration() {
		if (isInPlaybackState()) {
			return (int) mMediaPlayer.getDuration();
		}

		return -1;
	}

	@Override
	public int getCurrentPosition() {
		if (isInPlaybackState()) {
			return (int) mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public void seekTo(int msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
		} else {
			mSeekWhenPrepared = msec;
		}
	}

	@Override
	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	@Override
	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	private boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	@Override
	public boolean canPause() {
		return mCanPause;
	}

	@Override
	public boolean canSeekBackward() {
		return mCanSeekBack;
	}

	@Override
	public boolean canSeekForward() {
		return mCanSeekForward;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	// REMOVED: getAudioSessionId();
	// REMOVED: onAttachedToWindow();
	// REMOVED: onDetachedFromWindow();
	// REMOVED: onLayout();
	// REMOVED: draw();
	// REMOVED: measureAndLayoutSubtitleWidget();
	// REMOVED: setSubtitleWidget();
	// REMOVED: getSubtitleLooper();

	// -------------------------
	// Extend: Aspect Ratio
	// -------------------------

	private static final int[] s_allAspectRatio = {
			IRenderView.AR_ASPECT_FIT_PARENT,
			IRenderView.AR_ASPECT_FILL_PARENT,
			IRenderView.AR_ASPECT_WRAP_CONTENT, IRenderView.AR_MATCH_PARENT,
			IRenderView.AR_16_9_FIT_PARENT, IRenderView.AR_4_3_FIT_PARENT };
	private int mCurrentAspectRatioIndex = 3;//默认是不拉伸填充
	private int mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];

	public int toggleAspectRatio() {
		mCurrentAspectRatioIndex++;
		if(mCurrentAspectRatioIndex>5){
			mCurrentAspectRatioIndex = 0;
		}
		mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
		if (mRenderView != null)
			mRenderView.setAspectRatio(mCurrentAspectRatio);
		return mCurrentAspectRatio;
	}

	// -------------------------
	// Extend: Render
	// -------------------------
	public static final int RENDER_NONE = 0;
	public static final int RENDER_SURFACE_VIEW = 1;
	public static final int RENDER_TEXTURE_VIEW = 2;

	private List<Integer> mAllRenders = new ArrayList<Integer>();
	private int mCurrentRenderIndex = 0;
	private int mCurrentRender = RENDER_NONE;

	private void initRenders() {
		mAllRenders.clear();

		if (enableSurfaceView)
			mAllRenders.add(RENDER_SURFACE_VIEW);
		if (enableTextureView
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mAllRenders.add(RENDER_TEXTURE_VIEW);
		if (enableNoView)
			mAllRenders.add(RENDER_NONE);

		if (mAllRenders.isEmpty())
			mAllRenders.add(RENDER_SURFACE_VIEW);
		mCurrentRender = mAllRenders.get(mCurrentRenderIndex);
		setRender(mCurrentRender);
	}

	public int toggleRender() {
		mCurrentRenderIndex++;
		mCurrentRenderIndex %= mAllRenders.size();

		mCurrentRender = mAllRenders.get(mCurrentRenderIndex);
		setRender(mCurrentRender);
		return mCurrentRender;
	}

	// -------------------------
	// Extend: Background
	// -------------------------

	private void initBackground() {
		if (enableBackgroundPlay) {
			// MediaPlayerService.intentToStart(getContext());
			// mMediaPlayer = MediaPlayerService.getMediaPlayer();
		}
	}

	public void setAspectRatio(int aspectRatio) {
		for (int i = 0; i < s_allAspectRatio.length; i++) {
			if (s_allAspectRatio[i] == aspectRatio) {
				mCurrentAspectRatioIndex = i;
				mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
				if (mRenderView != null) {
					mRenderView.setAspectRatio(mCurrentAspectRatio);
				}
				break;
			}
		}
	}

}
