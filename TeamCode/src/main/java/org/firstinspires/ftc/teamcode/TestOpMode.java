package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Test OpMode")
public class TestOpMode extends LinearOpMode {
    DcMotor bl;
    DcMotor br;
    DcMotor fl;
    DcMotor fr;

    @Override
    public void runOpMode() {

        //telemetry.addData("Status", "Initialized");
        //telemetry.update();

        //TODO: Update the device names below w/ the proper names from the control hub configuration
        bl = hardwareMap.get(DcMotor.class, "backLeftDrive");
        br = hardwareMap.get(DcMotor.class, "backRightDrive");
        fl = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        fr = hardwareMap.get(DcMotor.class, "frontRightDrive");

        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);

        DcMotor [] motors = {
                bl,
                br,
                fl,
                fr,
        };

        waitForStart();

        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        int target = motors[0].getCurrentPosition() + 2000;

        for (DcMotor motor : motors) {
            motor.setTargetPosition(target);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.5);
        }
        while (opModeIsActive() && motors[0].isBusy()) {
            telemetry.addData("Target", target);
            telemetry.addData("Current", motors[0].getCurrentPosition());
            telemetry.update();
            //idle();
        }
        target = motors[0].getCurrentPosition() + 1000;
        turnCC(0.5, 200);
        while (opModeIsActive() && motors[0].isBusy()) {
            telemetry.addData("Target", target);
            telemetry.addData("Current", motors[0].getCurrentPosition());
            telemetry.update();
            //idle();
        }

        target = motors[0].getCurrentPosition() + 1000;

        for (DcMotor motor : motors) {
            motor.setTargetPosition(target);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.5);
        }

        for (DcMotor motor : motors) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        /*if (opModeIsActive()) {
            //telemetry.addData("Status", "Running!");
            //telemetry.update();

            while (opModeIsActive()) {
                idle();
            }
        }*/
    }

    private void forward(double power){
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }
    private void turnCC(double power, int target_delta) {
        //CC=Counter Clockwise
        // frontLeftDrive.setPower((-power));
        // frontRightDrive.setPower(power);
        // backLeftDrive.setPower((-power));
        // backRightDrive.setPower(power);
        fl.setTargetPosition(fl.getCurrentPosition() - target_delta);
        fr.setTargetPosition(fl.getCurrentPosition() + target_delta);
        bl.setTargetPosition(fl.getCurrentPosition() - target_delta);
        br.setTargetPosition(fl.getCurrentPosition() + target_delta);

        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        fl.setPower((-power));
        fr.setPower(power);
        bl.setPower((-power));
        br.setPower(power);
    }
    private void turnC(double power, int target_delta) {
        //C=Clockwise
        fl.setTargetPosition(fl.getCurrentPosition() + target_delta);
        fr.setTargetPosition(fl.getCurrentPosition() - target_delta);
        bl.setTargetPosition(fl.getCurrentPosition() + target_delta);
        br.setTargetPosition(fl.getCurrentPosition() - target_delta);

        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        fl.setPower(power);
        fr.setPower((-power));
        bl.setPower(power);
        br.setPower((-power));
    }
    private void strafeLeft(double power, int target_delta) {

        fl.setTargetPosition(fl.getCurrentPosition() + target_delta);
        fr.setTargetPosition(fl.getCurrentPosition() - target_delta);
        bl.setTargetPosition(fl.getCurrentPosition() - target_delta);
        br.setTargetPosition(fl.getCurrentPosition() + target_delta);

        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        fl.setPower(power);
        fr.setPower((-power));
        bl.setPower(power);
        br.setPower((-power));
    }
    private void strafeRight(double power, int target_delta) {

        fl.setTargetPosition(fl.getCurrentPosition() - target_delta);
        fr.setTargetPosition(fl.getCurrentPosition() + target_delta);
        bl.setTargetPosition(fl.getCurrentPosition() + target_delta);
        br.setTargetPosition(fl.getCurrentPosition() - target_delta);

        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        fl.setPower(power);
        fr.setPower((-power));
        bl.setPower(power);
        br.setPower((-power));
    }
}
