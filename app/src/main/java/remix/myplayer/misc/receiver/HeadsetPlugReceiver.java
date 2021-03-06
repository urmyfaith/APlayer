package remix.myplayer.misc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import remix.myplayer.Global;
import remix.myplayer.service.Command;
import remix.myplayer.service.MusicService;
import remix.myplayer.util.Constants;

import static remix.myplayer.util.Util.sendLocalBroadcast;

/**
 * Created by Remix on 2016/3/23.
 */

/**
 * 接收耳机插入与拔出的广播
 * 当检测到耳机拔出并且正在播放时，发送停止播放的广播
 */
public class HeadsetPlugReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        final String action = intent.getAction();
        boolean headsetOn = true;
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            headsetOn = false;
        }
        if (intent.hasExtra("state")) {
            headsetOn = intent.getIntExtra("state", -1) == 1;
        }

        Global.setHeadsetOn(headsetOn);
        Intent eqIntent = new Intent(Constants.SOUNDEFFECT_ACTION);
        eqIntent.putExtra("IsHeadsetOn", Global.getHeadsetOn());
        sendLocalBroadcast(eqIntent);

        if (!headsetOn /**&& MusicServiceRemote.isPlaying()*/) {
            Intent cmdIntent = new Intent(MusicService.ACTION_CMD);
            cmdIntent.putExtra("Control", Command.HEADSET_CHANGE);
            sendLocalBroadcast(cmdIntent);
        }
        try {
            abortBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
