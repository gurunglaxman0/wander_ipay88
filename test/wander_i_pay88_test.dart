import 'package:flutter/src/services/platform_channel.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:wander_i_pay88/wander_i_pay88.dart';
import 'package:wander_i_pay88/wander_i_pay88_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockWanderIPay88Platform with MockPlatformInterfaceMixin implements IPay {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  // TODO: implement channel
  MethodChannel get channel => throw UnimplementedError();
}

void main() {
  final IPay initialPlatform = IPay.instance;

  test('$IPay is the default instance', () {
    expect(initialPlatform, isInstanceOf<IPay>());
  });

  test('getPlatformVersion', () async {
    WanderIPay88 wanderIPay88Plugin = WanderIPay88();
    MockWanderIPay88Platform fakePlatform = MockWanderIPay88Platform();
    IPay.instance = fakePlatform;

    expect(await wanderIPay88Plugin.getPlatformVersion(), '42');
  });
}
