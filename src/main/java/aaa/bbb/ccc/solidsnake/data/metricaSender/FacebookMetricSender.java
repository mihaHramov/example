package aaa.bbb.ccc.solidsnake.data.metricaSender;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import aaa.bbb.ccc.solidsnake.model.UserEvent;

import static aaa.bbb.ccc.solidsnake.utils.Constants.OFFERID;
import static aaa.bbb.ccc.solidsnake.utils.Constants.PID;
import static aaa.bbb.ccc.solidsnake.utils.Constants.STATUS;

public class FacebookMetricSender implements IMetricEventSender {
    private Context context;

    public FacebookMetricSender(Context context) {
        this.context = context;
    }

    @Override
    public void sendEvent(UserEvent event) {
        Bundle params = new Bundle();
        params.putString(OFFERID, event.getOfferid());
        params.putString(PID, event.getPid());
        params.putString(STATUS, event.getStatus());
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        switch (event.getGoal()) {
            case "fd":
                logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL, params);
                break;
            case "reg":
                params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, "");
                logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
                break;
            case "dep":
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, event.getCurrency());
                logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, event.getSum(), params);

                break;
        }
        Log.d("mihaHramov","facebookSender");
    }
}
