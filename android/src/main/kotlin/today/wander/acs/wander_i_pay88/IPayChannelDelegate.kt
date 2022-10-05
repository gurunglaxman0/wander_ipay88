package today.wander.acs.wander_i_pay88

import android.os.Handler
import android.os.Looper
import com.ipay.IPayIHResultDelegate
import io.flutter.plugin.common.MethodChannel
import java.io.Serializable

private var methodChannel: MethodChannel? = null

class IPayChannelDelegate(@Transient private var channel: MethodChannel)
    : IPayIHResultDelegate, Serializable {

    init {
        methodChannel = channel
    }

    override fun onConnectionError(
            transId: String?,
            refNo: String?,
            p2: String?,
            p3: String?,
            amount: String?,
            p5: String?,
            p6: String?
    ) {
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                    "onPaymentSucceeded",
                    mapOf(
                            "transId" to transId,
                            "refNo" to refNo,
                            "amount" to amount,
                            "errDesc" to "",
                    )
            )
        }
    }

    override fun onPaymentSucceeded(transId: String?,
                                    refNo: String?,
                                    amount: String?,
                                    remark: String?,
                                    authCode: String?) {
        try {
            Handler(Looper.getMainLooper()).post {
                channel?.invokeMethod(
                        "onPaymentSucceeded",
                        mapOf(
                                "transId" to transId,
                                "refNo" to refNo,
                                "amount" to amount,
                                 "remark" to remark,
                                "authCode" to authCode,
                        )
                )
            }
        } catch (e: Exception) {
            print("onPaymentCanceled android $e");
        }
    }

    override fun onPaymentFailed(transId: String?,
                                 refNo: String?,
                                 amount: String?,
                                 remark: String?,
                                 errDesc: String?) {
        print("onPaymentFailed started $errDesc");
        try {
            Handler(Looper.getMainLooper()).post {
                methodChannel?.invokeMethod(
                        "onPaymentSucceeded",
                        mapOf(
                                "transId" to transId,
                                "refNo" to refNo,
                                "amount" to amount,
                                 "remark" to remark,
                                "errDesc" to errDesc,
                        )
                )
            }
        } catch (e: Exception) {
            print("onPaymentCanceled android $e");
        }
    }

    override fun onPaymentCanceled(transId: String?,
                                   refNo: String?,
                                   amount: String?,
                                   remark: String?,
                                   errDesc: String?) {
        print("onPaymentCanceled started");
        try {
            Handler(Looper.getMainLooper()).post {
                methodChannel?.invokeMethod(
                        "onPaymentSucceeded",
                        mapOf(
                                "transId" to transId,
                                "refNo" to refNo,
                                "amount" to amount,
                                 "remark" to remark,
                                "errDesc" to errDesc,
                        )
                )
            }
        } catch (e: Exception) {
            print("onPaymentCanceled android $e");
        }
    }

    fun onBackPressed() {
        Handler(Looper.getMainLooper()).post {
            methodChannel?.invokeMethod(
                    "onPaymentSucceeded",
                    mapOf(
                            "transId" to "",
                            "refNo" to "",
                            "amount" to "",
                            "errDesc" to "",
                    )
            )
        }
    }

    override fun onRequeryResult(merchantCode: String?,
                                 refNo: String?,
                                 amount: String?,
                                 result: String?) {
        try {
            Handler(Looper.getMainLooper()).post {
                channel?.invokeMethod(
                        "onRequeryResult",
                        mapOf(
                                "merchantCode" to merchantCode,
                                "refNo" to refNo,
                                "amount" to amount,
                                "result" to result,
                        )
                )
            }
        } catch (e: Exception) {
            print("onPaymentCanceled android $e");
        }
    }
}