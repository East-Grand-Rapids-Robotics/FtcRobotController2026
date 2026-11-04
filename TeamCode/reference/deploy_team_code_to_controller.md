# Deploying code to controller 

## Step 1: Connect your Mac to the Control Hub
1. Power on your REV Control Hub.
2. On your Mac, open System Settings → Wi-Fi.
3. Connect to the Control Hub's Wi-Fi network, typically named something like FTC-XXXXX.
4. Enter the Control Hub's Wi-Fi password.


## Step 2: Connect Android Studio using ADB
Android Studio uses the Android Debug Bridge (ADB) to communicate with the Control Hub.

Open Terminal on your Mac and run:
~/Library/Android/sdk/platform-tools/adb connect 192.168.43.1:5555


You should see:
connected to 192.168.43.1:5555


Then verify the connection:
~/Library/Android/sdk/platform-tools/adb devices


Expected output:
List of devices attached
192.168.43.1:5555    device


The Control Hub supports wireless ADB on port 5555 by default, so you don't need to enable wireless debugging manually.


## Step 3: Deploy from Android Studio
Once ADB is connected:
1. Return to Android Studio.
2. In the toolbar, select the TeamCode run configuration.
3. Select the REV Control Hub from the device dropdown (just to the left of the TeamCode run configuration)
4. Click the green Run triangle (or press Control-R on your Mac.

Android Studio will build the application if necessary, transfer the APK to the Control Hub, and install it.

## Step 4: Verify your TestOpMode
On the REV Driver Hub:
1. Connect to your robot.
2. Open the TeleOp program selection menu.
3. Look for Test OpMode.
4. Select it, initialize it, and press Start.

If it runs, you should see your Running! telemetry message.


An alternative: Install the APK manually
Since you've already built:

TeamCode/build/outputs/apk/debug/TeamCode-debug.apk

You can also install it from Terminal once ADB is connected:
~/Library/Android/sdk/platform-tools/adb install -r \
  TeamCode/build/outputs/apk/debug/TeamCode-debug.apk


The -r option requests replacement of an existing installation while retaining its data, when Android permits it. If the APK is signed with a different certificate, installation may fail and require a separate replacement procedure.

# Resetting ADB connection

## 1. Reset the ADB connection
In Terminal, run these commands in order:
~/Library/Android/sdk/platform-tools/adb disconnect
~/Library/Android/sdk/platform-tools/adb kill-server
~/Library/Android/sdk/platform-tools/adb start-server
~/Library/Android/sdk/platform-tools/adb connect 192.168.43.1:5555




