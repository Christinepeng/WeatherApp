# API credential handling

Never commit API keys to source code, Gradle properties, documentation, or logs. The previously committed OpenWeather credential must be revoked in OpenWeather; removing it from current files does not invalidate copies or Git history.

For local development, set `OPENWEATHER_API_KEY` in the ignored root `local.properties` file or as an environment variable. Do not use the old key. With no key configured, automatic initial weather loading is skipped and requests fail before reaching OpenWeather.

Gradle BuildConfig keeps the key out of Git, but **does not hide it in an APK**. Before distributing an app with a private or billable credential, route requests through an authenticated, rate-limited backend that stores the key server-side. Do not put a replacement private Google Cloud key into this Android application. Use API and application restrictions for supported Google Maps mobile SDK keys.

If a key is exposed, revoke it at its provider first, assess usage, and update its legitimate consumers. Do not restore revoked keys from Google Cloud's recovery UI.
