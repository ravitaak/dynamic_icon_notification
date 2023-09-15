package org.codedrink.dynamic_icon_notification;

import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.Icon;
import androidx.core.graphics.drawable.IconCompat;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class DynamicIconNotificationPlugin implements FlutterPlugin, MethodCallHandler {

  private MethodChannel channel;
  private NotificationManagerCompat notificationManagerCompat;
  private Notification notification;
  private Context context;
  private static final String MIPMAP = "mipmap";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "org.codedrink.notification/custom");
    context = flutterPluginBinding.getApplicationContext();
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("sendNotification")) {
      result.success(showNotification());
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(this);
  }

  public Bitmap textAsBitmap() {
    Rect rect = new Rect();
    rect.set(-10, -10, 92, 92);
    Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    TextView view = new TextView(context);
    view.setText("40°");
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

  public boolean showNotification() {
    try {
      // IconCompat icon = IconCompat.createWithBitmap(textAsBitmap());
      NotificationCompat.Builder notificationBuilder = null;
      // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      // notificationBuilder = new NotificationCompat.Builder(context, "channelID")
      // .setSmallIcon(icon)
      // .setContentTitle("Notification")
      // .setContentText("Hello! This is a notification.")
      // .setAutoCancel(true);
      // } else {
      int id = getDrawableResourceId(context, "ic_launcher");
      notificationBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
          .setSmallIcon(id)
          .setContentTitle("Notification")
          .setContentText("Hello! This is a notification.")
          .setAutoCancel(true);
      // }
      NotificationManager notificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);
      int notificationId = 1;
      createChannel(notificationManager);
      notificationManager.notify(notificationId, notificationBuilder.build());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public void createChannel(NotificationManager notificationManager) {
    if (Build.VERSION.SDK_INT < 26) {
      return;
    }

    NotificationChannel channel = new NotificationChannel("channelID", "name", NotificationManager.IMPORTANCE_DEFAULT);
    channel.setDescription("Hello! This is a notification.");
    notificationManager.createNotificationChannel(channel);
  }

  private int getDrawableResourceId(Context context, String name) {
    try {
      int ravi = context.getResources().getIdentifier(name, MIPMAP, context.getPackageName());
      Toast.makeText(context, ravi + "", Toast.LENGTH_SHORT).show();
      return ravi;
    } catch (Exception e) {
      Toast.makeText(context, e.getMessage() + " Ravi", Toast.LENGTH_SHORT).show();
      return 0;

    }
  }
}
