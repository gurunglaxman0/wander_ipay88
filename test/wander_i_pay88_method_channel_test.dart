import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:wander_i_pay88/wander_i_pay88.dart';

void main() {
  WanderIPay88 platform = WanderIPay88();
  const MethodChannel channel = MethodChannel('today.wander.acs.ipay88');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
