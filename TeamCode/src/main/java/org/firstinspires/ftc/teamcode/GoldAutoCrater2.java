package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.sun.tools.javac.util.ArrayUtils;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.List;


import static org.opencv.imgproc.Imgproc.contourArea;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="GoldAutoCrater2", group="Autonomous")
public class GoldAutoCrater2 extends LinearOpMode {
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
        //setup goldVision
        goldVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        goldVision.setShowCountours(true);
        goldVision.enable();
        sleep(3000);

        int goldPosition = 1;

        double expectedGoldY = 0;
        double expectedGoldArea = 250;
        // get a list of contours from the vision system
        List<MatOfPoint> contours = goldVision.getContours();
        int bestContourIndex = 0;
        double bestContourScore = Double.MAX_VALUE;
        int maxTurnIterations = 10;
        for (int i = 0; i < maxTurnIterations; i++) {
            for (int j = 0; j < contours.size(); j++) {
                // get the bounding rectangle of a single contour, we use it to get the x/y center
                // yes there's a mass center using Imgproc.moments but w/e
                Rect boundingRect = Imgproc.boundingRect(contours.get(j));
                telemetry.addData("contour" + Integer.toString(j), Float.toString(boundingRect.y + boundingRect.height / 2));
                if (Math.abs(expectedGoldY - (boundingRect.y + boundingRect.height / 2)) + Math.abs(expectedGoldArea - contourArea(contours.get(j))) < bestContourScore) {
                    bestContourScore = Math.abs(expectedGoldY - (boundingRect.y + boundingRect.height / 2)) + Math.abs(expectedGoldArea - contourArea(contours.get(j)));
                    bestContourIndex = j;
                }
            }
            double goldX = Imgproc.boundingRect(contours.get(bestContourIndex)).x + Imgproc.boundingRect(contours.get(bestContourIndex)).width/2;
            if(goldX > 400 && goldX < 460)
                break;
            else if(goldX < 400){
                frontRightDrive.setPower(-0.3);
                backRightDrive.setPower(-0.3);
                frontLeftDrive.setPower(0.3);
                backLeftDrive.setPower(0.3);
                sleep(100);
                frontRightDrive.setPower(0);
                backRightDrive.setPower(0);
                frontLeftDrive.setPower(0);
                backLeftDrive.setPower(0);
            }
            else{
                frontRightDrive.setPower(0.3);
                backRightDrive.setPower(0.3);
                frontLeftDrive.setPower(-0.3);
                backLeftDrive.setPower(-
                        0.3);
                sleep(100);
                frontRightDrive.setPower(0);
                backRightDrive.setPower(0);
                frontLeftDrive.setPower(0);
                backLeftDrive.setPower(0);
            }
        }
        //find position of gold mineral
        double goldX = Imgproc.boundingRect(contours.get(bestContourIndex)).x + Imgproc.boundingRect(contours.get(bestContourIndex)).width/2;
        if(goldX < 200)
            goldPosition = 0;
        else if(goldX < 280)
            goldPosition = 1;
        else
            goldPosition = 2;
        telemetry.addData("goldPos", goldPosition);
        //if(goldPosition==0)
        //sleep(0);
        //else if(goldPosition==1)
        //sleep(5000);
        //else
        //sleep(15000);

        //adjust after test
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
        if(goldPosition == 2){
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

