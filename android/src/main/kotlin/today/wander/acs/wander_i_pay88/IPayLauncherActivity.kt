package today.wander.acs.wander_i_pay88

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.ipay.IPayIH
import com.ipay.IPayIHActivity
import com.ipay.IPayIHPayment

class IPayLauncherActivity : Activity() {
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var iPayActivity: Activity? = null
    private var iPaySessionTimedOut = false
    private var refNo: String = ""
    private var timeoutInMinutes: Int = 0
    private var defaultTimeoutInMinutes: Int = 25

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchIPay()
        setIPaySessionTimer()
    }

    private fun setIPaySessionTimer() {
        application
            .registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    if (activity is IPayIHActivity) {
                        Log.d("IPayLauncherActivity", "----->timer started")
                        iPayActivity = activity
                        mainThreadHandler.postDelayed(
                            iPayFinishRunnable,
                            timeoutInMinutes.toLong() * 60000
                        )
                    }
                }

                override fun onActivityStarted(activity: Activity) {
                    // Empty
                }

                override fun onActivityResumed(activity: Activity) {
                    // Empty
                }

                override fun onActivityPaused(activity: Activity) {
                    // Empty
                }

                override fun onActivityStopped(activity: Activity) {
                    if (activity is IPayIHActivity) {
                        Log.d("IPayLauncherActivity", "----->Activity stopped")
                        mainThreadHandler.removeCallbacks(iPayFinishRunnable)
                    }
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
                ) {
                    // Empty
                }

                override fun onActivityDestroyed(activity: Activity) {
                    // Empty
                }
            })
    }

    private val iPayFinishRunnable: Runnable =
        Runnable {
            iPaySessionTimedOut = true
            Log.d("IPayLauncherActivity", "----->iPay session timeout")
            iPayActivity?.finish()
        }

    private fun launchIPay() {
        val bundle = intent.extras
        val params = bundle?.getParcelable<IPayParams>(IPAY_PARAMS)
        val channelDelegate = bundle?.getSerializable(IPAY_DELEGATE) as IPayChannelDelegate
        val iPayIHPayment = IPayIHPayment()
        iPayIHPayment.refNo = params?.refNo ?: ""
        refNo = params?.refNo ?: ""
        iPayIHPayment.paymentId = params?.paymentId ?: ""
        iPayIHPayment.merchantKey = params?.merchantKey ?: ""
        iPayIHPayment.merchantCode = params?.merchantCode ?: ""
        iPayIHPayment.backendPostURL = params?.backendPostUrl ?: ""
        iPayIHPayment.amount = params?.amount ?: "1.00"
        iPayIHPayment.currency = params?.currency ?: "MYR"
        iPayIHPayment.prodDesc = params?.prodDesc ?: ""
        iPayIHPayment.userName = params?.userName ?: ""
        iPayIHPayment.userEmail = params?.userEmail ?: ""
        iPayIHPayment.remark = params?.remark
        iPayIHPayment.actionType = params?.actionType
        iPayIHPayment.country = params?.country ?: "MY"
        iPayIHPayment.lang = params?.lang
        timeoutInMinutes = (params?.timoutInMinutes ?: defaultTimeoutInMinutes.toString()).toInt()
        if (timeoutInMinutes == 0) {
            Log.d("IPayLauncherActivity", "--------------------->default timeout to 25")
            timeoutInMinutes = defaultTimeoutInMinutes
        }

        val iPayIntent = IPayIH.getInstance().checkout(
            iPayIHPayment,
            this,
            channelDelegate,
            IPayIH.PAY_METHOD_CREDIT_CARD
        )
        startActivityForResult(iPayIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val channelDelegate = intent.extras?.getSerializable(IPAY_DELEGATE) as IPayChannelDelegate
        if (resultCode == RESULT_CANCELED) {
            channelDelegate.onBackPressed(iPaySessionTimedOut, refNo)
        }
        finish()
    }

    companion object {
        const val IPAY_PARAMS = "ipayParams"
        const val IPAY_DELEGATE = "ipayDelegate"

        fun create(
            context: Context, params: IPayParams,
            channelDelegate: IPayChannelDelegate
        ): Intent {
            val intent = Intent(context, IPayLauncherActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(IPAY_PARAMS, params)
            bundle.putSerializable(IPAY_DELEGATE, channelDelegate)
            intent.putExtras(bundle)
            return intent
        }
    }
}

class IPayParams() : Parcelable {
    var refNo: String? = null
    var paymentId: String? = null
    var merchantKey: String? = null
    var merchantCode: String? = null
    var backendPostUrl: String? = null
    var amount: String? = null
    var currency: String? = null
    var prodDesc: String? = null
    var userName: String? = null
    var userEmail: String? = null
    var remark: String? = null
    var actionType: String? = null
    var country: String? = null
    var lang: String? = null
    var timoutInMinutes: String? = null

    constructor(parcel: Parcel) : this() {
        refNo = parcel.readString()
        paymentId = parcel.readString()
        merchantKey = parcel.readString()
        merchantCode = parcel.readString()
        backendPostUrl = parcel.readString()
        amount = parcel.readString()
        currency = parcel.readString()
        prodDesc = parcel.readString()
        userName = parcel.readString()
        userEmail = parcel.readString()
        remark = parcel.readString()
        actionType = parcel.readString()
        country = parcel.readString()
        lang = parcel.readString()
        timoutInMinutes = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(refNo)
        parcel.writeString(paymentId)
        parcel.writeString(merchantKey)
        parcel.writeString(merchantCode)
        parcel.writeString(backendPostUrl)
        parcel.writeString(amount)
        parcel.writeString(currency)
        parcel.writeString(prodDesc)
        parcel.writeString(userName)
        parcel.writeString(userEmail)
        parcel.writeString(remark)
        parcel.writeString(actionType)
        parcel.writeString(country)
        parcel.writeString(lang)
        parcel.writeString(timoutInMinutes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IPayParams> {
        override fun createFromParcel(parcel: Parcel): IPayParams {
            return IPayParams(parcel)
        }

        override fun newArray(size: Int): Array<IPayParams?> {
            return arrayOfNulls(size)
        }
    }

}