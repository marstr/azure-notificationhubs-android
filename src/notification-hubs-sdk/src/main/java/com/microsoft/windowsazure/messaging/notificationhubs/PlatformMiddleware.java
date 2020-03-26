package com.microsoft.windowsazure.messaging.notificationhubs;

import android.telephony.TelephonyManager;

import java.util.Locale;

public final class PlatformMiddleware implements InstallationMiddleware {

    // TODO: update tags to final names/format.

    public static final int MOBILE_CARRIER_MASK = 0x0001;
    public static final String MOBILE_CARRIER_TAG = "nhMobileCarrier:";

    public static final int MANUFACTURER_MASK = 0x0002;
    public static final String MANUFACTURER_TAG = "nhManufacturer:";

    public static final int LANGUAGE_MASK = 0x0004;
    public static final String LANGUAGE_TAG = "nhLanguage:";

    public static final int COUNTRY_MASK = 0x0008;
    public static final String COUNTRY_TAG = "nhCountry:";

    public static final int DEFAULT_MASK = 0x000F;

    private int mask;

    private static TelephonyManager telephonyManager;

    public PlatformMiddleware() {
        this(DEFAULT_MASK);
    }

    public PlatformMiddleware(int mask) {
        this.mask = mask;
    }

    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {

            if ((this.mask & LANGUAGE_MASK) != 0) {
                subject.addTag(LANGUAGE_TAG + Locale.getDefault().getLanguage());
            }

            if ((this.mask & COUNTRY_MASK) != 0) {
                subject.addTag(COUNTRY_TAG + Locale.getDefault().getCountry());
            }

            // TODO: enrich with manufacturer data.

            // TODO: enrich with mobile-network data.

            next.enrichInstallation(subject);
        };
    }
}
