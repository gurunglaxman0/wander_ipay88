package today.wander.acs.wander_i_pay88

import android.os.Handler
import android.os.Looper
import com.ipay.IPayIHResultDelegate
import io.flutter.plugin.common.MethodChannel
import java.io.Serializable

private var tempChannel: MethodChannel? = null

class IPayChannelDelegate(@Transient private var channel: MethodChannel) : IPayIHResultDelegate, Serializable {
    init {
        tempChannel = channel
    }
    override fun onConnectionError(
        p0: String?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: String?,
        p6: String?
    ) {
        TODO("Not yet implemented")
    }


    override fun onPaymentSucceeded(transId: String?, refNo: String?, amount: String?, remark: String?, authCode: String?) {
        try {
            Handler(Looper.getMainLooper()).post(Runnable {
                channel?.invokeMethod(
                    "onPaymentSucceeded",
                    mapOf(
                        "transId" to transId,
                        "refNo" to refNo,
                        "amount" to amount,
                        "authCode" to authCode,
                    )
                )
            })

        } catch (e: Exception) {
            print("onPaymentCanceled android ${e.toString()}");
        }
    }

    override fun onPaymentFailed(transId: String?, refNo: String?, amount: String?, remark: String?, errDesc: String?) {
        print("onPaymentFailed started $errDesc");

        try {
            Handler(Looper.getMainLooper()).post(Runnable {
                tempChannel?.invokeMethod(
                    "onPaymentFailed",
                    mapOf(
                        "transId" to transId,
                        "refNo" to refNo,
                        "amount" to amount,
                        "errDesc" to errDesc,
                    )
                )
            })

        } catch (e: Exception) {
            print("onPaymentCanceled android ${e.toString()}");
        }


    }

    override fun onPaymentCanceled(transId: String?, refNo: String?, amount: String?, remark: String?, errDesc: String?) {
        print("onPaymentCanceled started");
        try {
            Handler(Looper.getMainLooper()).post(Runnable {
                tempChannel?.invokeMethod(
                    "onPaymentCanceled",
                    mapOf(
                        "transId" to transId,
                        "refNo" to refNo,
                        "amount" to amount,
                        "errDesc" to errDesc,
                    )
                )
            })

       } catch (e: Exception) {
           print("onPaymentCanceled android ${e.toString()}");
       }

    }

    override fun onRequeryResult(merchantCode: String?, refNo: String?, amount: String?, result: String?) {
        try {
            Handler(Looper.getMainLooper()).post(Runnable {
                 channel?.invokeMethod(
                        "onRequeryResult",
                mapOf(
                    "merchantCode" to merchantCode,
                    "refNo" to refNo,
                    "amount" to amount,
                    "result" to result,
                )
                )
            })

        } catch (e: Exception) {
            print("onPaymentCanceled android ${e.toString()}");
        }

    }
}