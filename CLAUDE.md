# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A fork of the official FIRST Tech Challenge `FtcRobotController` SDK (season 2026–2027, SDK 12.0.0) holding this FTC team's robot code. It builds one Android app that runs on the robot's Control Hub.

- **`TeamCode/`**: the team's code. Almost all work happens in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`.
- **`FtcRobotController/`**: the SDK's app shell and its sample OpModes (`.../robotcontroller/external/samples/`). Treat it as read-only. To use a sample, copy it into `teamcode/` and remove its `@Disabled` annotation.
- **`build.common.gradle`, `build.dependencies.gradle`, root `build.gradle`**: owned by the SDK and replaced when it is updated. Put build customizations in `TeamCode/build.gradle`.
- **`TeamCode/reference/`**: sits outside `src`, so it is not compiled.

## Commands

```bash
./gradlew assembleDebug                 # build the APK
./gradlew :TeamCode:installDebug        # build and install on a connected Control Hub (USB or adb over Wi-Fi)
```

The project has no unit tests and no linter. To check a change, compile it, then run the OpMode on the robot from the Driver Station. The code is Java 8 (`sourceCompatibility 1.8`). The README says Android Studio Narwhal 3 Feature Drop or later is needed.

## How OpModes work here

- Each OpMode is a class that extends `LinearOpMode` and is labeled `@Autonomous` or `@TeleOp(name = "...")`. The label is what makes it appear on the Driver Station. A class without one, like `TroubleShootAndSpike`, won't show up.
- Hardware is looked up by **string name** with `hardwareMap.get(...)`. Each name must exactly match the robot configuration on the Control Hub, including spaces. The names the current robot uses:
  - Drive: `fl`, `fr`, `bl`, `br` (mecanum). `fl` and `bl` are set to `REVERSE`.
  - Shooters: `shoot L pollen` and `shoot R nectar` (velocity control through `DcMotorEx`).
  - Intake motor `intake`, continuous servos `roller L` and `roller R`.
  - Gate servos `open pollen` and `necter open` (spelled "necter").
  - `TestOpMode` still uses the old SDK sample names (`frontLeftDrive` etc.), so it fails on the current robot configuration.
- There is no shared hardware or drive class yet. Each OpMode declares its own motors and keeps its own copy of the `forward`, `turnC`, `turnCC`, `strafeLeft` and `strafeRight` helpers. These copies have drifted apart, so `TestOpMode`'s helpers behave differently from those in `Thing` and `CornerParkingBasic`. If you change movement logic, check every copy, or move them into a shared class. The SDK sample `RobotHardware.java` shows that pattern.
- **Autonomous movement** (`Thing`, `CornerParkingBasic`) uses encoders. After resetting the encoders, the code sets a target on each motor, switches it to `RUN_TO_POSITION`, sets power, and then waits in `while (opModeIsActive() && motors[0].isBusy())`. The helpers in `Thing` set targets but don't change the run mode. They only work because an earlier straight move already put the motors in `RUN_TO_POSITION`. In-code calibration notes: about 1000 ticks per 24 inches, and 1800–1900 ticks for a 90° turn.
- **TeleOp** (`Movement24371`) was converted from Blocks (note the `JavaUtil` calls). Gamepad 1 drives the mecanum base at a 0.8 multiplier and runs the intake with its triggers. Gamepad 2 controls shooter velocity with its triggers and the gate servos with A and B.
