import 'dart:async';
import 'package:flutter/services.dart';

class DynamicIconNotification {
  static const MethodChannel methodChannel = MethodChannel('org.codedrink.notification/custom');

  Future<bool?> sendNotification() async {
    try {
      final res = await methodChannel.invokeMethod('sendNotification');
      return res;
    } on PlatformException catch (e) {
      print(e.message);
      return null;
    }
  }
}
