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

import static org.opencv.imgproc.Imgproc.contourArea;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="GoldAuto", group="Autonomous")
public class GoldAuto extends LinearOpMode {
    GoldVision goldVision = new GoldVision();

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

        int goldPosition = 1;

        double expectedGoldY = 0;
        double expectedGoldArea = 250;
        // get a list of contours from the vision system
        List<MatOfPoint> contours = goldVision.getContours();
        double[] contourFitnessArray = new double[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            // get the bounding rectangle of a single contour, we use it to get the x/y center
            // yes there's a mass center using Imgproc.moments but w/e
            Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            telemetry.addData("contour" + Integer.toString(i), Float.toString(boundingRect.y + boundingRect.height/2));
            contourFitnessArray[i] = Math.abs(expectedGoldY-(boundingRect.y+boundingRect.height/2)) + Math.abs(expectedGoldArea-contourArea(contours.get(i)));
        }
        int turnTime = 500;
        int driveTime = 1000;
        if(goldPosition == 0){
            frontRightDrive.setPower(-0.4);
            backRightDrive.setPower(-0.4);
            frontLeftDrive.setPower(-0.4);
            backLeftDrive.setPower(-0.4);
            sleep(turnTime);
            frontLeftDrive.setPower(0.4);
            backLeftDrive.setPower(0.4);
            sleep(driveTime);
            frontRightDrive.setPower(0);
            frontLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            backLeftDrive.setPower(0);
        }
        if(goldPosition == 1){
            frontRightDrive.setPower(-0.4);
            backRightDrive.setPower(-0.4);
            frontLeftDrive.setPower(0.4);
            backLeftDrive.setPower(0.4);
            sleep(driveTime);
            frontRightDrive.setPower(0);
            frontLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            backLeftDrive.setPower(0);
        }
        if(goldPosition == 0){
            frontRightDrive.setPower(0.4);
            backRightDrive.setPower(0.4);
            frontLeftDrive.setPower(0.4);
            backLeftDrive.setPower(0.4);
            sleep(turnTime);
            frontRightDrive.setPower(-0.4);
            backRightDrive.setPower(-0.4);
            sleep(driveTime);
            frontRightDrive.setPower(0);
            frontLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            backLeftDrive.setPower(0);
        }
    }
}

