package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
 * Shows every AprilTag the webcam sees on the Driver Station.
 * Use it to check that the camera works and to read tag ids and distances.
 *
 * To see the camera image: before pressing START, open the Driver Station's
 * 3-dot menu and choose "Camera Stream".
 *
 * Gamepad 1: dpad down pauses the camera, dpad up resumes it.
 */
@TeleOp(name = "AprilTag Test", group = "Vision")
public class AprilTagTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        AprilTagDetector tags = new AprilTagDetector(hardwareMap);

        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch START to start OpMode");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            tags.addTelemetry(telemetry);
            telemetry.update();

            if (gamepad1.dpad_down) {
                tags.stopStreaming();
            } else if (gamepad1.dpad_up) {
                tags.resumeStreaming();
            }

            sleep(20);
        }

        tags.close();
    }
}
