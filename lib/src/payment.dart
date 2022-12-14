import 'dart:convert';

class IPayPayment {
  IPayPayment({
    this.paymentId = "",
    this.merchantKey = "",
    this.merchantCode = "",
    this.refNo = "",
    this.amount = "1.00",
    this.currency = "",
    this.prodDesc = "",
    this.userName = "",
    this.userEmail = "",
    this.userContact = "",
    this.remark,
    this.lang,
    this.country = "",
    this.backendPostURL = "",
    this.timeoutInMinutes = "",
  });

  String paymentId;
  String merchantKey;
  String merchantCode;
  String refNo;
  String amount;
  String currency;
  String prodDesc;
  String userName;
  String userEmail;
  String userContact;
  String? remark;
  String? lang;
  String country;
  String backendPostURL;

  /// Not available in Indonesian implementation
  String? actionType;
  String? appDeepLink;
  String timeoutInMinutes;

  Map<String, dynamic> toArguments() {
    return {
      'paymentId': paymentId,
      'merchantKey': merchantKey,
      'merchantCode': merchantCode,
      'refNo': refNo,
      'amount': amount,
      'currency': currency,
      'prodDesc': prodDesc,
      'username': userName,
      'userEmail': userEmail,
      'userContact': userContact,
      'remark': remark,
      'lang': lang,
      'country': country,
      'backEndPostURL': backendPostURL,
      'actionType': actionType,
      'appDeepLink': appDeepLink,
      'timeoutInMinutes': timeoutInMinutes,
    };
  }

  @override
  String toString() => const JsonEncoder.withIndent('\t').convert(
        toArguments(),
      );
}
