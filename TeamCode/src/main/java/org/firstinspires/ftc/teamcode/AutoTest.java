package org.firstinspires.ftc.teamcode;

import android.util.Log;

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

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoTest", group="Autonomous")
public class AutoTest extends LinearOpMode {
    ExampleBlueVision blueVision = new ExampleBlueVision();

    //declare motors
    private DcMotor frontRightDrive = null;
    private DcMotor frontLeftDrive  = null;
    private DcMotor backRightDrive  = null;
    private DcMotor backLeftDrive   = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //map hardware
        //frontRightDrive = hardwareMap.dcMotor.get("frontRight");
        //frontLeftDrive  = hardwareMap.dcMotor.get("frontLeft");
        //backRightDrive  = hardwareMap.dcMotor.get("backRight");
        //backLeftDrive   = hardwareMap.dcMotor.get("backLeft");

        waitForStart();
        // get a list of contours from the vision system
        //List<MatOfPoint> contours = blueVision.getContours();
        //for (int i = 0; i < contours.size(); i++) {
            // get the bounding rectangle of a single contour, we use it to get the x/y center
            // yes there's a mass center using Imgproc.moments but w/e
            //Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            //telemetry.addData("contour" + Integer.toString(i),
                    //String.format(Locale.getDefault(), "(%d, %d)", (boundingRect.x + boundingRect.width) / 2, (boundingRect.y + boundingRect.height) / 2));
        telemetry.addData("log", "test");
        sleep(5000);
    }
}


