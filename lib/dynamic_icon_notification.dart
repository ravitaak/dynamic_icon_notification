import 'dart:async';
import 'package:flutter/services.dart';

class DynamicIconNotification {
  static const MethodChannel methodChannel = MethodChannel('org.codedrink.notification/custom');

  Future<bool?> sendNotification(String title, String body, String temp) async {
    try {
      final res = await methodChannel.invokeMethod('sendNotification', {"title": title, "body": body, "temp": temp});
      return res;
    } on PlatformException catch (e) {
      print(e.message);
      return null;
    }
  }

  Future<bool?> stopNotification() async {
    try {
      final res = await methodChannel.invokeMethod('stopNotification');
      return res;
    } on PlatformException catch (e) {
      print(e.message);
      return null;
    }
  }
}
