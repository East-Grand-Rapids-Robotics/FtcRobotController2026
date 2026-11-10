package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous
public class Thing extends LinearOpMode {
    DcMotor bl;
    DcMotor br;
    DcMotor fl;
    DcMotor fr;
    DcMotor shootLpollen;
    Servo nectaropen;
    Servo openpollen;

    @Override
    public void runOpMode() {

        //telemetry.addData("Status", "Initialized");
        //telemetry.update();

        //TODO: Update the device names below w/ the proper names from the control hub configuration
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        //nectaropen = hardwareMap.get(Servo.class, "necter open");
        openpollen = hardwareMap.get(Servo.class, "open pollen");
        //shootLpollen = hardwareMap.get(DcMotor.class, "shoot L pollen");
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);

        DcMotor[] motors = {
                bl,
                br,
                fl,
                fr,
        };

        waitForStart();
        //((DcMotorEx) shootLpollen).setVelocity(2000);
        openpollen.setPosition(1);
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        int target = motors[0].getCurrentPosition() - 200;
        //24 inches =1000 ticks, 1000 ticks=90 degree turn 1900
        for (DcMotor motor : motors) {
            motor.setTargetPosition(target);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower((-0.5));
        }
        while (opModeIsActive() && motors[0].isBusy()) {
            telemetry.addData("Target", target);
            telemetry.addData("Current", motors[0].getCurrentPosition());
            telemetry.update();
            //idle();
        }
        for (DcMotor motor : motors) {
            motor.setPower(0);
        }
        //sleep(5000);
        //SHOOT!!!!
//        openpollen.setPosition(0);
//        ((DcMotorEx) shootLpollen).setVelocity(2000);
//        sleep(5000);
//        openpollen.setPosition(1);
//        ((DcMotorEx) shootLpollen).setVelocity(1500);
        turnC(0.5, 1800);
        while (opModeIsActive() && motors[0].isBusy()) {
            telemetry.addData("Target", target);
            telemetry.addData("Current", motors[0].getCurrentPosition());
            telemetry.update();
        }
        for (DcMotor motor : motors) {
            motor.setPower(0);
        }
        strafeLeft(0.5, 800);
        while (opModeIsActive() && motors[0].isBusy()) {
                 telemetry.addData("Target", target);
                 telemetry.addData("Current", motors[0].getCurrentPosition());
                 telemetry.update();
        }


        // target = motors[0].getCurrentPosition() + 2000;
        // for (DcMotor motor : motors) {
        //     motor.setTargetPosition(target);
        //     motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //     motor.setPower((0.5));
        // }
        // while (opModeIsActive() && motors[0].isBusy()) {
        //     telemetry.addData("Target", target);
        //     telemetry.addData("Current", motors[0].getCurrentPosition());
        //     telemetry.update();
        // }
        // strafeLeft(0.5, 100);
        // while (opModeIsActive() && motors[0].isBusy()) {
        //     telemetry.addData("Target", target);
        //     telemetry.addData("Current", motors[0].getCurrentPosition());
        //     telemetry.update();
        // }
        // target = motors[0].getCurrentPosition() + 600;
        // for (DcMotor motor : motors) {
        //     motor.setTargetPosition(target);
        //     motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //     motor.setPower((0.5));
        // }
        // while (opModeIsActive() && motors[0].isBusy()) {
        //     telemetry.addData("Target", target);
        //     telemetry.addData("Current", motors[0].getCurrentPosition());
        //     telemetry.update();
        // }


        //strafeRight(1000, 1000);
        //while (opModeIsActive() && motors[0].isBusy()) {
        //telemetry.addData("Target", target);
        //telemetry.addData("Current", motors[0].getCurrentPosition());
        //telemetry.update();
        //}

        // target = motors[0].getCurrentPosition() + 1000;
        // turnCC(0.5, 200);
        // while (opModeIsActive() && motors[0].isBusy()) {
        //     telemetry.addData("Target", target);
        //     telemetry.addData("Current", motors[0].getCurrentPosition());
        //     telemetry.update();
        //     //idle();
        // }

        // target = motors[0].getCurrentPosition() + 1000;

        // for (DcMotor motor : motors) {
        //     motor.setTargetPosition(target);
        //     motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //     motor.setPower(0.5);
        // }
        //stop
        /*if (opModeIsActive()) {
            //telemetry.addData("Status", "Running!");
            //telemetry.update();

            while (opModeIsActive()) {
                idle();
            }
        }*/

    }
    private void forward( double power){
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }
    private void turnCC( double power, int target_delta){
        //CC=Counter Clockwise
        // frontLeftDrive.setPower((-power));
        // frontRightDrive.setPower(power);
        // backLeftDrive.setPower((-power));
        // backRightDrive.setPower(power);
        fl.setPower((-power));
        fr.setPower(power);
        bl.setPower((-power));
        br.setPower(power);

        fl.setTargetPosition(fl.getCurrentPosition() - target_delta);
        fr.setTargetPosition(fr.getCurrentPosition() + target_delta);
        bl.setTargetPosition(bl.getCurrentPosition() - target_delta);
        br.setTargetPosition(br.getCurrentPosition() + target_delta);

//        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }
    private void turnC( double power, int target_delta){
        //C=Clockwise
        fl.setTargetPosition(fl.getCurrentPosition() + target_delta);
        fr.setTargetPosition(fr.getCurrentPosition() - target_delta);
        bl.setTargetPosition(bl.getCurrentPosition() + target_delta);
        br.setTargetPosition(br.getCurrentPosition() - target_delta);

        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
//        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);


//        fl.setPower(power);
//        fr.setPower((-power));
//        bl.setPower(power);
//        br.setPower((-power));
    }
    private void strafeLeft( double power, int target_delta){

        fl.setPower(power);
        fr.setPower((power));
        bl.setPower(power);
        br.setPower((power));

        fl.setTargetPosition(fl.getCurrentPosition() + target_delta);
        fr.setTargetPosition(fr.getCurrentPosition() - target_delta);
        bl.setTargetPosition(bl.getCurrentPosition() - target_delta);
        br.setTargetPosition(br.getCurrentPosition() + target_delta);

//        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);


//        fl.setPower(power);
//        fr.setPower((-power));
//        bl.setPower(power);
//        br.setPower((-power));
    }
    private void strafeRight( double power, int target_delta){

        fl.setTargetPosition(fl.getCurrentPosition() - target_delta);
        fr.setTargetPosition(fr.getCurrentPosition() + target_delta);
        bl.setTargetPosition(bl.getCurrentPosition() + target_delta);
        br.setTargetPosition(br.getCurrentPosition() - target_delta);

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