import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:wander_i_pay88/src/delegate.dart';
import 'package:wander_i_pay88/src/payment.dart';

abstract class IPay extends PlatformInterface {
  /// Constructs a WanderIPay88Platform.
  IPay() : super(token: _token) {
    channel.setMethodCallHandler((call) async {
      String method = call.method;
      var arguments = call.arguments;
      print("arguments ${arguments.toString()}");
      String? transId, refNo, amount, remark, authCode;
      String? errDesc, merchantCode, result;
      if (arguments is Map) {
        transId = arguments['transId']?.toString();
        refNo = arguments['refNo']?.toString();
        amount = arguments['amount']?.toString();
        remark = arguments['remark']?.toString();
        authCode = arguments['authCode']?.toString();
        errDesc = arguments['errDesc']?.toString();
        merchantCode = arguments['merchantCode']?.toString();
        result = arguments['result']?.toString();
      }
      switch (method) {
        case "onPaymentSucceeded":
          onPaymentSucceeded(transId, refNo, amount, remark, authCode);
          break;
        case "onPaymentFailed":
          onPaymentFailed(transId, refNo, amount, remark, errDesc);
          break;
        case "onPaymentCanceled":
          onPaymentCanceled(transId, refNo, amount, remark, errDesc);
          break;
      }
    });
  }

  static final Object _token = Object();

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [WanderIPay88Platform] when
  /// they register themselves.
  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> _permissionsCheck() async {
    // if (Platform.isAndroid) {
    //   if (!await Permission.phone.request().isGranted) {
    //     throw StateError(
    //       "Phone permission is required for Android",
    //     );
    //   }
    // }
  }

  @mustCallSuper
  Future<void> checkout({required IPayPayment payment}) async {
    await _permissionsCheck();
  }

  void addDelegate(IPayResultDelegate delegate) {
    if (!delegates.contains(
      delegate,
    )) {
      delegates.add(delegate);
    }
  }

  void removeDelegate(IPayResultDelegate delegate) {
    delegates.remove(delegate);
  }

  @protected
  final List<IPayResultDelegate> delegates = [];

  @protected
  @visibleForTesting
  abstract final MethodChannel channel;
}

extension IPayCheckout on IPay {
  @protected
  void onPaymentSucceeded(String? transId, String? refNo, String? amount, String? remark, String? authCode) {
    for (var d in delegates) {
      d.onPaymentSucceeded(transId, refNo, amount, remark, authCode);
    }
  }

  @protected
  void onPaymentFailed(String? transId, String? refNo, String? amount, String? remark, String? errDesc) {
    for (var d in delegates) {
      d.onPaymentFailed(transId, refNo, amount, remark, errDesc);
    }
  }

  @protected
  void onPaymentCanceled(String? transId, String? refNo, String? amount, String? remark, String? errDesc) {
    for (var d in delegates) {
      d.onPaymentCanceled(transId, refNo, amount, remark, errDesc);
    }
  }
}
