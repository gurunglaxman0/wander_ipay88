package today.wander.acs.wander_i_pay88

import android.os.Handler
import android.os.Looper
import com.ipay.IPayIHResultDelegate
import io.flutter.plugin.common.MethodChannel
import java.io.Serializable

private var methodChannel: MethodChannel? = null

class IPayChannelDelegate(@Transient private var channel: MethodChannel) : IPayIHResultDelegate, Serializable {

    init {
        methodChannel = channel
    }

    override fun onConnectionError(
        transId: String?, refNo: String?, p2: String?, p3: String?, amount: String?, p5: String?, p6: String?
    ) {
//        sendSuccessReponse(transId, refNo)
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onPaymentFailed", mapOf(
                    "transId" to transId,
                    "refNo" to refNo,
                    "amount" to amount,
                    "errDesc" to "",
                )
            )
        }
    }

    override fun onPaymentSucceeded(
        transId: String?, refNo: String?, amount: String?, remark: String?, authCode: String?
    ) {
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onPaymentSucceeded", mapOf(
                    "transId" to transId,
                    "refNo" to refNo,
                    "amount" to amount,
                    "remark" to remark,
                    "authCode" to authCode,
                )
            )
        }
    }

    override fun onPaymentFailed(
        transId: String?, refNo: String?, amount: String?, remark: String?, errDesc: String?
    ) {
//        sendSuccessReponse(transId, refNo)
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onPaymentFailed", mapOf(
                    "transId" to transId,
                    "refNo" to refNo,
                    "amount" to amount,
                    "remark" to remark,
                    "errDesc" to errDesc,
                )
            )
        }
    }

    override fun onPaymentCanceled(
        transId: String?, refNo: String?, amount: String?, remark: String?, errDesc: String?
    ) {
//        sendSuccessReponse(transId, refNo)
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onPaymentCanceled", mapOf(
                    "transId" to transId,
                    "refNo" to refNo,
                    "amount" to amount,
                    "remark" to remark,
                    "errDesc" to errDesc,
                )
            )
        }
    }

    fun onBackPressed() {
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onPaymentCanceled", mapOf(
                    "transId" to "",
                    "refNo" to "",
                    "amount" to "",
                    "errDesc" to "Payment canceled by user",
                )
            )
        }
    }

    override fun onRequeryResult(
        merchantCode: String?, refNo: String?, amount: String?, result: String?
    ) {
//        sendSuccessReponse("onRequeryResult", refNo)
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onRequeryResult", mapOf(
                    "merchantCode" to merchantCode,
                    "refNo" to refNo,
                    "amount" to amount,
                    "result" to result,
                )
            )
        }
    }

    fun sendSuccessReponse(transId: String?, refNo: String?) {
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                "onPaymentSucceeded", mapOf(
                    "transId" to transId,
                    "refNo" to refNo,
                    "amount" to "1.00",
                    "remark" to null,
                    "authCode" to "authcodeasd",
                )
            )
        }
    }
}