package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

import static org.opencv.imgproc.Imgproc.contourArea;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="GoldAutoDepot", group="Autonomous")
public class GoldAutoDepot extends LinearOpMode {
    GoldVision goldVision = new GoldVision();

    //declare motors
    private DcMotor frontRightDrive = null;
    private DcMotor frontLeftDrive  = null;
    private DcMotor backRightDrive  = null;
    private DcMotor backLeftDrive   = null;

    private DcMotor arm  = null;

    private Servo rightClaw = null;
    private Servo leftClaw  = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //map hardware
        frontRightDrive = hardwareMap.dcMotor.get("frontRight");
        frontLeftDrive  = hardwareMap.dcMotor.get("frontLeft");
        backRightDrive  = hardwareMap.dcMotor.get("backRight");
        backLeftDrive   = hardwareMap.dcMotor.get("backLeft");

        arm = hardwareMap.dcMotor.get("arm");

        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        leftClaw  = hardwareMap.get(Servo.class, "leftClaw");

        waitForStart();

        int goldPosition = 1;

        double expectedGoldY = 0;
        double expectedGoldArea = 250;
        // get a list of contours from the vision system
        List<MatOfPoint> contours = goldVision.getContours();
        double[] contourFitnessArray = new double[contours.size()];
        int bestContourIndex = 0;
        double bestContourScore = 100000;
        for (int i = 0; i < contours.size(); i++) {
            // get the bounding rectangle of a single contour, we use it to get the x/y center
            // yes there's a mass center using Imgproc.moments but w/e
            Rect boundingRect = Imgproc.boundingRect(contours.get(i));
            telemetry.addData("contour" + Integer.toString(i), Float.toString(boundingRect.y + boundingRect.height / 2));
            if (Math.abs(expectedGoldY - (boundingRect.y + boundingRect.height / 2)) + Math.abs(expectedGoldArea - contourArea(contours.get(i))) < bestContourScore) {
                bestContourScore = Math.abs(expectedGoldY - (boundingRect.y + boundingRect.height / 2)) + Math.abs(expectedGoldArea - contourArea(contours.get(i)));
                bestContourIndex = i;
            }
        }
        double goldX = Imgproc.boundingRect(contours.get(bestContourIndex)).x + Imgproc.boundingRect(contours.get(bestContourIndex)).width/2;

        if(goldX < 390)
            goldPosition = 0;
        else if(goldX < 470)
            goldPosition = 1;
        else
            goldPosition = 2;

        int turnTime = 500;
        int driveToMineralTime = 1000;
        int armTime = 250;
        if(goldPosition == 0){
            //turn to face gold
            frontRightDrive.setPower(-0.4);
            backRightDrive.setPower(-0.4);
            frontLeftDrive.setPower(-0.4);
            backLeftDrive.setPower(-0.4);
            sleep(turnTime);
            //drive to gold
            frontLeftDrive.setPower(0.4);
            backLeftDrive.setPower(0.4);
            sleep(driveToMineralTime);
            //turn to depot
            frontRightDrive.setPower(0.4);
            backRightDrive.setPower(0.4);
            sleep(turnTime);
            //drive to depot
            frontRightDrive.setPower(-0.4);
            backRightDrive.setPower(-0.4);
            sleep(driveToMineralTime);
            //stop and put cube in depot
            frontRightDrive.setPower(0);
            frontLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            arm.setPower(0.3);
            sleep(armTime);
            rightClaw.setPosition(0);
            leftClaw.setPosition(1);
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
            sleep(driveToMineralTime);
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
            sleep(driveToMineralTime);
            frontRightDrive.setPower(0);
            frontLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            backLeftDrive.setPower(0);
        }
    }
}

