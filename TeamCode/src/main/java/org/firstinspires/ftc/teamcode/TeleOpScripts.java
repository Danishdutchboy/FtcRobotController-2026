package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Main TeleOp", group = "Linear OpMode")
public class TeleOpScripts extends LinearOpMode {

    @Override
    public void runOpMode() {
        //Motor declarations: Left front, Left back, Right front and Right back
        DcMotor leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        DcMotor rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        // 3. Set motor directions (Reverse one side so positive power moves both forward, leftBack motor is just being fussy about it)
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        ElapsedTime teleTime = new ElapsedTime();
        boolean warned = false;
        boolean aimMode = false;
        float speed = 1.5f;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Initialized. Waiting for start...");
        telemetry.update();

        // 4. Wait for the driver to press PLAY on the Driver Station
        waitForStart();

        // 5. Run until the driver presses STOP
        while (opModeIsActive()) {
            if (!warned && teleTime.seconds() >= 60) {  // TELEOP is 2:00, so 1:00 left
                gamepad1.rumble(500); gamepad2.rumble(500); warned = true;
            }
            telemetry.addData("Flowers open", teleTime.seconds() >= 60);

            if (gamepad1.triangleWasPressed()) {
                aimMode = !aimMode;
                switch (aimMode) {
                    case true:
                        speed = 0.25f;
                        break;
                    default:
                        speed = 1f;
                        break;
                }
            }

            // 6. Read gamepad joystick values (-1.0 to 1.0)
            // Note: Joysticks point down for positive, so we negate them to make "up" positive power
            double leftFrontPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x) / 3 * speed;
            double rightFrontPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x) / 3 * speed;
            double leftBackPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x) / 3 * speed;
            double rightBackPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x) / 3 * speed;


            // 7. Send calculated power to wheels
            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftBack.setPower(leftBackPower);
            rightBack.setPower(rightBackPower);
            if (aimMode) {

            }
            // 8. Send telemetry data back to the Driver Station screen
            telemetry.addData("Status", "Running");
            telemetry.addData("Motor Power", "Left front: %.2f  Right front: %.2f, Left back: %.2f  Right back: %.2f", leftFrontPower, rightFrontPower, leftBackPower, rightBackPower);
            telemetry.update();
        }
    }
}
