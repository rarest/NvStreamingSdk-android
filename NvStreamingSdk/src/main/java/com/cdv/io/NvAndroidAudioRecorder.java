//================================================================================
//
// (c) Copyright China Digital Video (Beijing) Limited, 2017. All rights reserved.
//
// This code and information is provided "as is" without warranty of any kind,
// either expressed or implied, including but not limited to the implied
// warranties of merchantability and/or fitness for a particular purpose.
//
//--------------------------------------------------------------------------------
//   Birth Date:    May 17. 2017
//   Author:        NewAuto video team
//================================================================================
package com.cdv.io;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.AudioFormat;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;


public class NvAndroidAudioRecorder {
    private static final String TAG = "NvAndroidAudioRecorder";
    private static final boolean m_verbose = false;

    private static final int m_sampleRateInHz = 44100; // Guaranteed to work on all devices
    private static final int m_channelConfig = AudioFormat.CHANNEL_IN_MONO; // Guaranteed to work on all devices
    private static final int m_audioFormat = AudioFormat.ENCODING_PCM_16BIT; // Guaranteed to work on all devices
    private static final int m_sampleSizeInBytes = 2;
    private static final int m_sampleCountInChunk = 1024;
    private static final int m_chunkSizeInBytes = m_sampleCountInChunk * m_sampleSizeInBytes;

    private ByteBuffer m_chunkBuffer = null;

    private AudioRecord m_recorder;
    private RecordDataCallback m_recordDataCallback = null;
    private boolean m_isRecording = false;
    private Thread m_recordingThread = null;
    private AtomicInteger m_exitingRecordingThread = new AtomicInteger(0);

    public interface RecordDataCallback
    {
        void onAudioRecordDataArrived(ByteBuffer buffer, int sampleCount);
    }

    public NvAndroidAudioRecorder()
    {
        // Calculate internal buffer size to guarantee a smooth recording under load
        int internalBufferSize = 16384 * m_sampleSizeInBytes;
        final int minBufferSize = AudioRecord.getMinBufferSize(m_sampleRateInHz, m_channelConfig, m_audioFormat);
        if (internalBufferSize < minBufferSize)
            internalBufferSize = minBufferSize;

        try {
            m_chunkBuffer = ByteBuffer.allocateDirect(m_chunkSizeInBytes);
            m_recorder = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    m_sampleRateInHz,
                    m_channelConfig,
                    m_audioFormat,
                    internalBufferSize);

            if (m_recorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
                Log.e(TAG, "Failed to initialize AudioRecord object!");
                m_recorder = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean startRecord(RecordDataCallback recordDataCallback)
    {
        if (m_isRecording)
            return false;

        try {
            m_recorder.startRecording();
            m_isRecording = true;

            m_recordDataCallback = recordDataCallback;

            // Start a thread to read data from AudioRecord object
            m_recordingThread = new Thread(new Runnable() {
                public void run() {
                    readAudioData();
                }
            }, "Audio Recorder");
            m_recordingThread.start();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            if (m_isRecording) {
                m_recorder.stop();
                m_isRecording = false;
            }

            m_recordDataCallback = null;
            return false;
        }
    }

    public boolean stopRecord()
    {
        if (!m_isRecording)
            return false;

        try {
            // Stop recording thread
            m_exitingRecordingThread.set(1);
            m_recordingThread.join();
            m_exitingRecordingThread.set(0);
            m_recordingThread = null;
            m_recordDataCallback = null;

            m_recorder.stop();
            m_isRecording = false;
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void readAudioData()
    {
        // NOTE: called from recording thread

        // Calculate a proper wait time
        final int waitTimeInMs = (int)(m_sampleCountInChunk / (float)m_sampleRateInHz * 1000) / 5;

        try {
            for (;;) {
                // Fill buffer by read audio data one or more times
                int bytesInChunk = 0;
                m_chunkBuffer.position(0);
                for (;;) {
                    if (m_exitingRecordingThread.get() != 0)
                        return;

                    final int expectedReadBytes = m_chunkSizeInBytes - bytesInChunk;
                    // NOTE: It seems that even if we don't set blocking mode, read() will block a little time for us
                    // This behaviour will make us never sleep in the subsequent code
                    final int ret = m_recorder.read(m_chunkBuffer, expectedReadBytes);
                    if (ret < 0) {
                        // Error occurred
                        Log.e(TAG, "read() failed! errno=" + ret);
                        Thread.sleep(waitTimeInMs);
                    } else {
                        if (ret != 0) {
                            m_chunkBuffer.position(bytesInChunk + ret);
                            bytesInChunk += ret;
                        }

                        if (ret == expectedReadBytes)
                            break;

                        if (m_verbose)
                            Log.d(TAG, "Sleep a while to wait audio data");
                        // Wait for a while to let recorder fill some audio data
                        Thread.sleep(waitTimeInMs);
                    }
                }

                if (m_recordDataCallback != null)
                    m_recordDataCallback.onAudioRecordDataArrived(m_chunkBuffer, m_sampleCountInChunk);
            }
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }
    }
}

