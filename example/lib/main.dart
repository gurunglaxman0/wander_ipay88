import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:wander_i_pay88/wander_i_pay88.dart';
import 'package:wander_i_pay88/wander_i_pay88_platform_interface.dart';

const String MERCHENT_KEY = "ji0lA3osG";
const String MERCHENT_CODE = "M14753";

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> implements IPayResultDelegate {
  String _platformVersion = 'Unknown';
  final _wanderIPay88Plugin = WanderIPay88();
  String _iPayResponse = "Unknown";

  @override
  void initState() {
    super.initState();
    initPlatformState();
    _wanderIPay88Plugin.addDelegate(this);
  }

  @override
  void dispose() {
    _wanderIPay88Plugin.removeDelegate(this);
    super.dispose();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion = await _wanderIPay88Plugin.getPlatformVersion() ??
          'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(children: [
            Text('Running on: $_platformVersion\n'),
            Text('Response: $_iPayResponse\n'),
          ]),
        ),
        floatingActionButton: FloatingActionButton(
          child: const Icon(Icons.help),
          onPressed: () {
            initiatePayment();
          },
        ),
      ),
    );
  }

  void initiatePayment() {
// Create an empty IPay88 Payment Object and assign some values to it's fields
    IPayPayment payment = IPayPayment()
      ..refNo = "123"
      ..amount = "1.0"
      ..merchantCode = MERCHENT_CODE
      ..merchantKey = MERCHENT_KEY
      ..currency = "MYR"
      ..country = "MY"
      ..prodDesc = "AEON TOP UP"
      ..backendPostURL = "https://www.aeonwallet.com.my/app/ipay/successack"
      ..paymentId = "6";

    // debugPrint("payment ${payment.toArguments().toString()}");

// Checking out with the Payment Object
    _wanderIPay88Plugin.checkout(payment: payment);
  }

  @override
  void onPaymentCanceled(String? transId, String? refNo, String? amount,
      String? remark, String? errDesc) {
    setState(() {
      _iPayResponse = "Payment Cancelled $errDesc";
    });
  }

  @override
  void onPaymentFailed(String? transId, String? refNo, String? amount,
      String? remark, String? errDesc) {
    setState(() {
      _iPayResponse = "Payment Failed $errDesc";
    });
  }

  @override
  void onPaymentSucceeded(String? transId, String? refNo, String? amount,
      String? remark, String? authCode) {
    setState(() {
      _iPayResponse = "Payment Succeeded";
    });
  }
}
