package aaa.bbb.ccc.solidsnake.data.metricaSender;

import android.content.Context;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;

import java.util.Hashtable;
import java.util.Map;

import aaa.bbb.ccc.solidsnake.model.UserEvent;

import static aaa.bbb.ccc.solidsnake.utils.Constants.OFFERID;
import static aaa.bbb.ccc.solidsnake.utils.Constants.PID;
import static aaa.bbb.ccc.solidsnake.utils.Constants.STATUS;

public class AppsFlyerMetricSender implements IMetricEventSender {
    private Context context;

    public AppsFlyerMetricSender(Context context) {
        this.context = context;
    }

    @Override
    public void sendEvent(UserEvent event) {
        Map<String, Object> parametr = new Hashtable<>();
        parametr.put(AFInAppEventParameterName.REVENUE, event.getDepsum());
        parametr.put(AFInAppEventParameterName.CURRENCY, event.getCurrency());
        parametr.put(PID, event.getPid());
        parametr.put(STATUS, event.getStatus());
        parametr.put(OFFERID, event.getOfferid());
        AppsFlyerLib.getInstance().trackEvent(context, event.getGoal(), parametr);
        Log.d("mihaHramov","AppsFlyer");
    }
}
