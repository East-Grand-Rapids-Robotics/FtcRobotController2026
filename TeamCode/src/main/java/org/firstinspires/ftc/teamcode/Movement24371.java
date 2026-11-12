package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "24371Movement (Blocks to Java)")
public class Movement24371 extends LinearOpMode {

  private Servo necteropen;
  private Servo openpollen;
  private DcMotor bl;
  private DcMotor fl;
  private DcMotor intake;
  private DcMotor shootLpollen;
  private DcMotor shootRnectar;
  private CRServo rollerL;
  private CRServo rollerR;
  private DcMotor br;
  private DcMotor fr;

  /**
   * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
   * Comment Blocks show where to place Initialization code (runs once, after touching the
   * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
   * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
   * Stopped).
   */
  @Override
  public void runOpMode() {
    double motor_multiplier;

    necteropen = hardwareMap.get(Servo.class, "necter open");
    openpollen = hardwareMap.get(Servo.class, "open pollen");
    bl = hardwareMap.get(DcMotor.class, "bl");
    fl = hardwareMap.get(DcMotor.class, "fl");
    intake = hardwareMap.get(DcMotor.class, "intake");
    shootLpollen = hardwareMap.get(DcMotor.class, "shoot L pollen");
    shootRnectar = hardwareMap.get(DcMotor.class, "shoot R nectar");
    rollerL = hardwareMap.get(CRServo.class, "roller L");
    rollerR = hardwareMap.get(CRServo.class, "roller R");
    br = hardwareMap.get(DcMotor.class, "br");
    fr = hardwareMap.get(DcMotor.class, "fr");

    necteropen.setPosition(-1);
    openpollen.setPosition(1);
    bl.setDirection(DcMotor.Direction.REVERSE);
    fl.setDirection(DcMotor.Direction.REVERSE);
    intake.setDirection(DcMotor.Direction.REVERSE);
    // Put initialization blocks here.
    waitForStart();
    motor_multiplier = 0.8;
    if (opModeIsActive()) {
      // Put run blocks here.
    }
    while (opModeIsActive()) {
      telemetry.addData("T on", Double.parseDouble(JavaUtil.formatNumber(gamepad2.right_trigger, (int) shootLpollen.getPower())));
      telemetry.addData("Intake RPM", Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) intake).getVelocity(), 2)));
      telemetry.addData("Shooter rpmL", Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) shootLpollen).getVelocity(), 2)));
      telemetry.addData("Shooter rpmR", Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) shootRnectar).getVelocity(), 2)));
      telemetry.addData("LSY", Double.parseDouble(JavaUtil.formatNumber(gamepad1.left_stick_y, 2)));
      telemetry.addData("RSX", Double.parseDouble(JavaUtil.formatNumber(gamepad1.right_stick_x, 2)));
      // Put loop blocks here.
      if (gamepad1.right_trigger == 1) {
        intake.setPower(1);
        rollerL.setPower(1);
        rollerR.setPower(1);
      } else {
        if (gamepad1.left_trigger == 1) {
          intake.setPower(-0.5);
        } else {
          intake.setPower(0);
          rollerL.setPower(0);
          rollerR.setPower(0);
        }
      }
      if (gamepad2.right_trigger == 1) {
        ((DcMotorEx) shootRnectar).setVelocity(-2050);
      } else {
        ((DcMotorEx) shootRnectar).setVelocity(-1500);
      }
      if (gamepad2.left_trigger == 1) {
        ((DcMotorEx) shootLpollen).setVelocity(2000);
      } else {
        ((DcMotorEx) shootLpollen).setVelocity(1500);
      }
      if (gamepad2.b) {
        necteropen.setPosition(1);
      } else {
        necteropen.setPosition(-1);
      }
      if (gamepad2.a) {
        openpollen.setPosition(0);
      } else {
        openpollen.setPosition(1);
      }
      if (gamepad1.left_stick_y != 24371) {
        bl.setPower(motor_multiplier * gamepad1.left_stick_x * -1 + motor_multiplier * gamepad1.right_stick_x + motor_multiplier * gamepad1.left_stick_y * -1);
        br.setPower(motor_multiplier * gamepad1.left_stick_x + motor_multiplier * gamepad1.right_stick_x * -1 + motor_multiplier * gamepad1.left_stick_y * -1);
        fl.setPower(motor_multiplier * gamepad1.left_stick_x + motor_multiplier * gamepad1.right_stick_x + motor_multiplier * gamepad1.left_stick_y * -1);
        fr.setPower(motor_multiplier * gamepad1.left_stick_x * -1 + motor_multiplier * gamepad1.right_stick_x * -1 + motor_multiplier * gamepad1.left_stick_y * -1);
        telemetry.update();
      }
    }
  }
}
