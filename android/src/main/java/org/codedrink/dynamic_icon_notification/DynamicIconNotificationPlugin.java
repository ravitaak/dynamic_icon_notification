package org.codedrink.dynamic_icon_notification;

import javax.management.Notification;
import javax.naming.Context;
import javax.swing.text.View;

import org.w3c.dom.css.Rect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
<<<<<<< HEAD
import android.graphics.Color;
=======
import android.graphics.Rect;
>>>>>>> 5989c97a3d1c95ee3204efb89dee2c8290cef00e
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;

public class DynamicIconNotificationPlugin implements FlutterPlugin, MethodCallHandler {

  private MethodChannel channel;

  private NotificationManager notificationManager;
  private Context context;
  private static final String DRAWABLE = "drawable";
  private static final String CHANNEL_ID = "fixedTemperature";
  private static final int NOTIFICATION_ID = 4616;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "org.codedrink.notification/custom");
    context = flutterPluginBinding.getApplicationContext();
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("sendNotification")) {
      String title = call.argument("title");
      String body = call.argument("body");
      String temp = call.argument("temp");
      result.success(showNotification(title, body, temp));
    } else if (call.method.equals("stopNotification")) {
      stopNotification();
    } else if (call.method.equals("makeToast")) {
      String msg = call.argument("msg");
      makeToast(msg);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(this);
  }

  public Bitmap textAsBitmap(String temp) {
    String text = temp + "°";
    Rect rect = new Rect();
    rect.set(-10, -10, 92, 92);
    Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    TextView view = new TextView(context);
    view.setText(text);
    view.setGravity(Gravity.CENTER);
    view.setTypeface(null, Typeface.BOLD);
    int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY);
    int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY);
    view.setTextSize(25);
    view.measure(widthSpec, heightSpec);
    view.layout(0, 0, rect.width(), rect.height());
    canvas.save();
    view.draw(canvas);
    canvas.restore();
    return bitmap;
  }

  public boolean showNotification(String title, String body, String temp) {
    try {
      NotificationCompat.Builder notificationBuilder = null;
      Intent notificationIntent = new Intent(this.getApplicationContext(), MainActivity.class);
      PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent,
          PendingIntent.FLAG_IMMUTABLE);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        IconCompat icon = IconCompat.createWithBitmap(textAsBitmap(temp));
        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(body)
            .setCategory(Notification.CATEGORY_STATUS)
            .setContentIntent(contentIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true);
      } else {
        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(getDrawableResourceId(context, "ic_launcher"))
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(contentIntent)
            .setOnlyAlertOnce(true)
            .setCategory(Notification.CATEGORY_STATUS)
            .setAutoCancel(true);
      }
      notificationBuilder.setOngoing(true);
      notificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);
      createChannel(notificationManager);
      notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean stopNotification() {
    try {
      if (notificationManager != null) {
        notificationManager.cancel(NOTIFICATION_ID);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void createChannel(NotificationManager notificationManager) {
    if (Build.VERSION.SDK_INT < 26)
      return;
    try {
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "All Time Temperature",
          NotificationManager.IMPORTANCE_HIGH);
      channel.setDescription("Show all time temperature in notification bar.");
      channel.setShowBadge(false);
      notificationManager.createNotificationChannel(channel);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static int getDrawableResourceId(Context context, String name) {
    return context.getResources().getIdentifier(name, DRAWABLE, context.getPackageName());
  }

  private boolean makeToast(String msg) {
    try {
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
