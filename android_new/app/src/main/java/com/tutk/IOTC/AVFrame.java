/******************************************************************************
 *                                                                            *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 *                                                                            *
 *                                                                            *
 * Class: AVFrame.java                                                    *
 *                                                                            *
 * Author: joshua ju                                                          *
 *                                                                            *
 * Date: 2011-05-14                                                           *
 *                                                                            *
 ******************************************************************************/

package com.tutk.IOTC;

public class AVFrame {

	public static final int FRAMEINFO_SIZE = 24;

	// Codec ID
	public static final int MEDIA_CODEC_UNKNOWN = 0x00;
	public static final int MEDIA_CODEC_VIDEO_MPEG4 = 0x4C;
	public static final int MEDIA_CODEC_VIDEO_H263 = 0x4D;
	public static final int MEDIA_CODEC_VIDEO_H264 = 0x4E;
	public static final int MEDIA_CODEC_VIDEO_MJPEG = 0x4F;

	public static final int MEDIA_CODEC_AUDIO_ADPCM = 0x8B;
	public static final int MEDIA_CODEC_AUDIO_PCM = 0x8C;
	public static final int MEDIA_CODEC_AUDIO_SPEEX = 0x8D;
	public static final int MEDIA_CODEC_AUDIO_MP3 = 0x8E;
	public static final int MEDIA_CODEC_AUDIO_G726 = 0x8F;

	// Frame Flag
	public static final int IPC_FRAME_FLAG_PBFRAME = 0x61; // A/V P/B frame.
	public static final int IPC_FRAME_FLAG_IFRAME = 0x65; // A/V I frame.
	public static final int IPC_FRAME_FLAG_MD = 0x02; // For motion detection.
	public static final int IPC_FRAME_FLAG_IO = 0x03; // For Alarm IO detection.

	// audio sample rate
	public static final int AUDIO_SAMPLE_8K = 0x00;
	public static final int AUDIO_SAMPLE_11K = 0x01;
	public static final int AUDIO_SAMPLE_12K = 0x02;
	public static final int AUDIO_SAMPLE_16K = 0x03;
	public static final int AUDIO_SAMPLE_22K = 0x04;

	public static final int AUDIO_SAMPLE_24K = 0x05;
	public static final int AUDIO_SAMPLE_32K = 0x06;
	public static final int AUDIO_SAMPLE_44K = 0x07;
	public static final int AUDIO_SAMPLE_48K = 0x08;

	// audio sample data bit
	public static final int AUDIO_DATABITS_8 = 0;
	public static final int AUDIO_DATABITS_16 = 1;

	// audio channel number
	public static final int AUDIO_CHANNEL_MONO = 0;
	public static final int AUDIO_CHANNEL_STERO = 1;

	// -----------------------------------------------------
	public static final byte FRM_STATE_UNKOWN = -1;
	public static final byte FRM_STATE_COMPLETE = 0;
	public static final byte FRM_STATE_INCOMPLETE = 1;
	public static final byte FRM_STATE_LOSED = 2;

	// -----------------------------------------------------
	private short codec_id = 0; // UINT16 codec_id;
								// Media codec type defined in sys_mmdef.h,
								// MEDIA_CODEC_AUDIO_PCMLE16 for audio,
								// MEDIA_CODEC_VIDEO_H264 for video.

	// private int camIndex;
	private byte flags = -1;// Combined with IPC_FRAME_xxx.
	private byte onlineNum = 0;
	private int timestamp = 0; // Timestamp of the frame, in milliseconds.
	private int videoWidth = 0;
	private int videoHeight = 0;
	// -----------------------------

	private long frmNo = -1;
	private byte frmState = 0; // 0:complete; 1:incomplete; 2: losed
	private int frmSize = 0; // Raw data size in bytes.
	public byte[] frmData = null; // Raw data of the frame.
}
