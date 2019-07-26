package aaa.bbb.ccc.solidsnake;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetricaConfig config = YandexMetricaConfig
                .newConfigBuilder(getString(R.string.yandex_app_id))
                .build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
        AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {

            }

            @Override
            public void onInstallConversionFailure(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        };

        AppsFlyerLib.getInstance().init(getString(R.string.app_flyer_id), conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
    }
}
