import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wander_i_pay88/wander_i_pay88_platform_interface.dart';

class WanderIPay88 extends IPay {
  /// The method channel used to interact with the native platform.

  WanderIPay88._();
  static final WanderIPay88 _instance = WanderIPay88._();

  factory WanderIPay88() => _instance;
  @override
  @visibleForTesting
  final channel = const MethodChannel('today.wander.acs.ipay88');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await channel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> checkout({
    required IPayPayment payment,
  }) async {
    await super.checkout(payment: payment);
    if (payment.currency.trim().isEmpty) {
      payment.currency = "MYR";
    }
    if (payment.country.trim().isEmpty) {
      payment.country = "MY";
    }
    channel.invokeMethod("checkout", payment.toArguments());
  }
}
