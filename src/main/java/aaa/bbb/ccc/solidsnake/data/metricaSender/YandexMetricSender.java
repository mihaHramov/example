package aaa.bbb.ccc.solidsnake.data.metricaSender;

import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import java.util.Hashtable;
import java.util.Map;

import aaa.bbb.ccc.solidsnake.model.UserEvent;

import static aaa.bbb.ccc.solidsnake.utils.Constants.CURRENCY;
import static aaa.bbb.ccc.solidsnake.utils.Constants.DEPSUM;
import static aaa.bbb.ccc.solidsnake.utils.Constants.GOAL;
import static aaa.bbb.ccc.solidsnake.utils.Constants.OFFERID;
import static aaa.bbb.ccc.solidsnake.utils.Constants.PID;
import static aaa.bbb.ccc.solidsnake.utils.Constants.STATUS;

public class YandexMetricSender implements IMetricEventSender {

    @Override
    public void sendEvent(UserEvent event) {
        Map<String, Object> map = new Hashtable<>();
        map.put(OFFERID, event.getOfferid());
        map.put(STATUS, event.getStatus());
        map.put(CURRENCY, event.getCurrency());
        map.put(PID, event.getPid());
        map.put(DEPSUM, event.getDepsum());
        map.put(GOAL, event.getGoal());
        YandexMetrica.reportEvent(event.getGoal(), map);
        Log.d("mihaHramov","yandex sender");
    }
}
