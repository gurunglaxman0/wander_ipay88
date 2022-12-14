package today.wander.acs.wander_i_pay88

import android.app.Activity
import android.os.Build
import androidx.annotation.NonNull
import com.ipay.IPayIH
import com.ipay.IPayIHR
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** WanderIPay88Plugin */
class WanderIPay88Plugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var channelDelegate: IPayChannelDelegate
    private var activity: Activity? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "today.wander.acs.ipay88")
        channel.setMethodCallHandler(this)
        channelDelegate = IPayChannelDelegate(channel)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        when (call.method) {
            "checkout" -> {
                result.success(null)
                activity?.let { _activity ->
                    val params = IPayParams()
                    params.refNo = call.argument<String>("refNo") ?: ""
                    params.paymentId = call.argument<String>("paymentId") ?: ""
                    params.merchantKey = call.argument<String>("merchantKey") ?: ""
                    params.merchantCode = call.argument<String>("merchantCode") ?: ""
                    params.backendPostUrl = call.argument<String>("backendPostURL") ?: ""
                    params.amount = call.argument<String>("amount") ?: "1.00"
                    params.currency = call.argument<String>("currency") ?: "MYR"
                    params.prodDesc = call.argument<String>("prodDesc") ?: ""
                    params.userName = call.argument<String>("userName") ?: ""
                    params.userEmail = call.argument<String>("userEmail") ?: ""
                    params.remark = call.argument<String>("remark")
                    params.actionType = call.argument<String>("actionType")
                    params.country = call.argument<String>("country") ?: "MY"
                    params.lang = call.argument<String>("lang")
                    params.timoutInMinutes = call.argument<String>("timeoutInMinutes")

                    val intent = IPayLauncherActivity.create(_activity, params, channelDelegate)
                    _activity.startActivity(intent)
                }
            }
            "requery" -> {
                result.success(null)
                activity?.let { a ->
                    IPayIHR().apply {
                        merchantCode = call.argument<String>("merchantCode") ?: ""
                        amount = call.argument<String>("amount") ?: "1.00"
                        refNo = call.argument<String>("refNo") ?: ""
                        country_Code = call.argument<String>("countryCode") ?: "MY"
                    }.let {
                        val intent = IPayIH().requery(it, a, channelDelegate)
                        a.startActivityForResult(intent, 2)
                    }
                }
            }
            "getPlatformVersion" -> {
                result.success("This is Android ${Build.VERSION.RELEASE}");
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    /**
     * This `ActivityAware` [io.flutter.embedding.engine.plugins.FlutterPlugin] is now
     * associated with an [android.app.Activity].
     *
     *
     * This method can be invoked in 1 of 2 situations:
     *
     *
     *  * This `ActivityAware` [io.flutter.embedding.engine.plugins.FlutterPlugin] was
     * just added to a [io.flutter.embedding.engine.FlutterEngine] that was already
     * connected to a running [android.app.Activity].
     *  * This `ActivityAware` [io.flutter.embedding.engine.plugins.FlutterPlugin] was
     * already added to a [io.flutter.embedding.engine.FlutterEngine] and
     * that [       ] was just connected to an [       ].
     *
     *
     * The given [ActivityPluginBinding] contains [android.app.Activity]-related
     * references that an `ActivityAware` [ ] may require, such as a reference to the
     * actual [android.app.Activity] in question. The [ActivityPluginBinding] may be
     * referenced until either [.onDetachedFromActivityForConfigChanges] or
     * [ ][.onDetachedFromActivity] is invoked. At the conclusion of either of those methods, the
     * binding is no longer valid. Clear any references to the binding or its resources, and do not
     * invoke any further methods on the binding or its resources.
     */
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    /**
     * The [android.app.Activity] that was attached and made available in [ ][.onAttachedToActivity]
     * has been detached from this `ActivityAware`'s [io.flutter.embedding.engine.FlutterEngine]
     * for the purpose of processing a configuration change.
     *
     *
     * By the end of this method, the [android.app.Activity] that was made available in
     * [.onAttachedToActivity] is no longer valid. Any references to the
     * associated [android.app.Activity] or [ActivityPluginBinding] should be cleared.
     *
     *
     * This method should be quickly followed by [ ][.onReattachedToActivityForConfigChanges],
     * which signifies that a new
     * [android.app.Activity] has been created with the new configuration options. That method
     * provides a new [ActivityPluginBinding], which references the newly created and associated
     * [android.app.Activity].
     *
     *
     * Any `Lifecycle` listeners that were registered in [ ][.onAttachedToActivity] should be
     * deregistered here to avoid a possible
     * memory leak and other side effects.
     */
    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    /**
     * This plugin and its [io.flutter.embedding.engine.FlutterEngine] have been re-attached to
     * an [android.app.Activity] after the [android.app.Activity] was recreated to handle
     * configuration changes.
     *
     *
     * `binding` includes a reference to the new instance of the [ ]. `binding` and its references
     * may be cached and used from now until
     * either [.onDetachedFromActivityForConfigChanges] or [.onDetachedFromActivity]
     * is invoked. At the conclusion of either of those methods, the binding is no longer valid. Clear
     * any references to the binding or its resources, and do not invoke any further methods on the
     * binding or its resources.
     */
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    /**
     * This plugin has been detached from an [android.app.Activity].
     *
     *
     * Detachment can occur for a number of reasons.
     *
     *
     *  * The app is no longer visible and the [android.app.Activity] instance has been
     * destroyed.
     *  * The [io.flutter.embedding.engine.FlutterEngine] that this plugin is connected to
     * has been detached from its [io.flutter.embedding.android.FlutterView].
     *  * This `ActivityAware` plugin has been removed from its [       ].
     *
     *
     * By the end of this method, the [android.app.Activity] that was made available in
     * [ ][.onAttachedToActivity] is no longer valid. Any references to the
     * associated [android.app.Activity] or [ActivityPluginBinding] should be cleared.
     *
     *
     * Any `Lifecycle` listeners that were registered in [ ][.onAttachedToActivity] or
     * [ ][.onReattachedToActivityForConfigChanges] should be deregistered here to
     * avoid a possible memory leak and other side effects.
     */
    override fun onDetachedFromActivity() {
        activity = null
    }
}
