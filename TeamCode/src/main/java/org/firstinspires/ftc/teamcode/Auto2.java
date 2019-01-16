package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Auto2", group="Autonomous")
public class Auto2 extends LinearOpMode {
    ExampleBlueVision blueVision = new ExampleBlueVision();

    //declare motors
    private DcMotor frontRightDrive = null;
    private DcMotor frontLeftDrive  = null;
    private DcMotor backRightDrive  = null;
    private DcMotor backLeftDrive   = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //map hardware
        frontRightDrive = hardwareMap.dcMotor.get("frontRightDrive");
        frontLeftDrive  = hardwareMap.dcMotor.get("frontLeftDrive");
        backRightDrive  = hardwareMap.dcMotor.get("backRightDrive");
        backLeftDrive   = hardwareMap.dcMotor.get("backLeftDrive");

        waitForStart();
        // get a list of contours from the vision system
        /*
        List<MatOfPoint> contours = blueVision.getContours();
        for (int i = 0; i < contours.size(); i++) {
            // get the bounding rectangle of a single contour, we use it to get the x/y center
            // yes there's a mass center using Imgproc.moments but w/e
            Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            telemetry.addData("contour" + Integer.toString(i),
                    String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));

        }*/
        frontRightDrive.setPower(1);
        frontLeftDrive.setPower(-1);
        backRightDrive.setPower(1);
        backLeftDrive.setPower(-1);
        sleep(1750);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
        sleep(400);
        frontLeftDrive.setPower(-1);
        frontRightDrive.setPower(-1);
        sleep(3500);
        frontRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        backLeftDrive.setPower(0);
    }
}

