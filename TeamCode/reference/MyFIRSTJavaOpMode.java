package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class MyFIRSTJavaOpMode extends LinearOpMode {
    FTCGamepad gamepad1;
    FTCKeyboard keyboard;
    DcMotor driveLeft;
    DcMotor driveRight;
    DcMotor shootwheel;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    Servo artifactstopper;
    ColorSensor color1;
    DistanceSensor distance1;
    BNO055IMU imu;

    var duration;
    var myVisionPortalBuilder;
    var forward;
    var nArtifacts;
    var turn;
    var myAprilTagDetections;
    var myVisionPortal;
    boolean isShooting;
    var myAprilTagDetection;
    var shootPower;
    var mode;
    var maxDrivePower;
    var strafe;
    var myApriltagProcessor;
    var myAprilTagProcessorBuilder;

    // Describe this function...
    public void inititalSetup(){
        // Put initialization blocks here
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        isShooting = false;
        // Holds back artifacts until we start shooting
        artifactstopper.setPosition(0.2);
    }

    // Describe this function...
    public void initializeVisionPortal(){
        myVisionPortalBuilder = new VisionPortal.Builder();
        myVisionPortal = (myVisionPortalBuilder.build());
        myVisionPortalBuilder.setCamera((hardwareMap.get(WebcamName.class, "webcam")));
        myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        myApriltagProcessor = (myAprilTagProcessorBuilder.build());
        myVisionPortalBuilder.addProcessor(myApriltagProcessor);
    }

    // Describe this function...
    public void pickMode(){
        if (mode == 0) {
            keyboardDrive();
        } else if (mode == 1) {
            gamepadDrive();
        } else if (mode == 2) {
            autoDrive();
        }
    }

    // Describe this function...
    public void keyboardDrive(){
        while (opModeIsActive()) {
            turn = keyboard.isPressed(108) - keyboard.isPressed(106);
            forward = keyboard.isPressed(105) - keyboard.isPressed(107);
            strafe = keyboard.isPressed(111) - keyboard.isPressed(117);
            processDriveInputs();
            if (keyboard.isPressed(112) && !isShooting) {
                shoot();
            }
            displayVisionPortalData();
        }
    }

    // Describe this function...
    public void gamepadDrive(){
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            forward = gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            processDriveInputs();
            if (gamepad1.a && !isShooting) {
                shoot();
            }
            displayVisionPortalData();
        }
    }

    // Describe this function...
    public void autoDrive(){
        driveToGoal();
        shootThreeArtifacts();
        driveToPlayerStationAndBack();
        shootThreeArtifacts();
        // After finishing autonomous, we fall back to drive
        keyboardDrive();
    }

    // Describe this function...
    public void driveToGoal(){
        forward = 1;
        processInputsAndSleep(2300);
        turn = -1;
        processInputsAndSleep(220);
        sleep(500);
    }

    // Describe this function...
    public void driveToPlayerStationAndBack(){
        forward = -1;
        processInputsAndSleep(2800);
        sleep(10000);
        forward = 1;
        processInputsAndSleep(2800);
        sleep(500);
    }

    // Describe this function...
    public void shootThreeArtifacts(){
        nArtifacts = 3;
        while (opModeIsActive() && nArtifacts > 0) {
            if (!isShooting) {
                shoot();
                nArtifacts -= 1;
            }
            displayVisionPortalData();
        }
    }

    // Describe this function...
    public void processInputsAndSleep(String duration){
        // This helper function makes the code a bit cleaner
        processDriveInputs();
        sleep(duration);
        // Stop all movement after sleep
        forward = 0;
        turn = 0;
        strafe = 0;
        processDriveInputs();
    }

    // Describe this function...
    public void processDriveInputs(){
        turn = turn * maxDrivePower;
        forward = forward * maxDrivePower;
        strafe = strafe * maxDrivePower;
        // Combine inputs to create drive and turn (or both!)
        frontLeftDrive.setPower((forward + turn + strafe));
        frontRightDrive.setPower(((forward - turn) - strafe));
        backLeftDrive.setPower(((forward + turn) - strafe));
        backRightDrive.setPower(((forward - turn) + strafe));
    }

    // Describe this function...
    public void shoot(){
        // Don't move while shooting
        isShooting = true;
        // Let one artifact come through
        artifactstopper.setPosition(0);
        shootwheel.setPower(shootPower);
        sleep(250);
        // Stop the next artifact
        artifactstopper.setPosition(0.2);
        sleep(200);
        shootwheel.setPower(0);
        sleep(1500);
        // Allow for a new shot to be triggered
        isShooting = false;
    }

    // Describe this function...
    public void displayVisionPortalData(){
        myAprilTagDetections = (myApriltagProcessor.getDetections());
        for (String myAprilTagDetection2 : myAprilTagDetections) {
            myAprilTagDetection = myAprilTagDetection2;
            telemetry.addData("ID", (myAprilTagDetection.id));
            telemetry.addData("Range", (myAprilTagDetection.ftcPose.range));
            telemetry.addData("Yaw", (myAprilTagDetection.ftcPose.yaw));
        }
        telemetry.update();
    }


    @Override
    public void runOpMode() {
        gamepad1 = new FTCGamepad();
        keyboard = new FTCKeyboard();
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");

        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");

        DcMotor [] motors = {
                backLeftDrive,
                backRightDrive,
                frontLeftDrive,
                frontRightDrive,
        };

        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        inititalSetup();
        initializeVisionPortal();
        shootPower = 0.8;
        maxDrivePower = 1;
        // mode 0 = keyboard, 1 = gamepad, 2 = autonomous
        //mode = 2;
        waitForStart();

        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }


        waitForStart();

        // Move 2000 ticks from the current position.
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

        //pickMode();

    }
    private void forward(double power){
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
    }
    private void turnCC(double power, int target_delta) {
        //CC=Counter Clockwise
        // frontLeftDrive.setPower((-power));
        // frontRightDrive.setPower(power);
        // backLeftDrive.setPower((-power));
        // backRightDrive.setPower(power);
        frontLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);
        frontRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);
        backLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);
        backRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        frontLeftDrive.setPower((-power));
        frontRightDrive.setPower(power);
        backLeftDrive.setPower((-power));
        backRightDrive.setPower(power);
    }
    private void turnC(double power, int target_delta) {
        //C=Clockwise
        frontLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);
        frontRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);
        backLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);
        backRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        frontLeftDrive.setPower(power);
        frontRightDrive.setPower((-power));
        backLeftDrive.setPower(power);
        backRightDrive.setPower((-power));
    }
    private void strafeLeft(double power, int target_delta) {

        frontLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);
        frontRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);
        backLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);
        backRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        frontLeftDrive.setPower(power);
        frontRightDrive.setPower((-power));
        backLeftDrive.setPower(power);
        backRightDrive.setPower((-power));
    }
    private void strafeRight(double power, int target_delta) {

        frontLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);
        frontRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);
        backLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() + target_delta);
        backRightDrive.setTargetPosition(frontLeftDrive.getCurrentPosition() - target_delta);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        frontLeftDrive.setPower(power);
        frontRightDrive.setPower((-power));
        backLeftDrive.setPower(power);
        backRightDrive.setPower((-power));
    }



}
