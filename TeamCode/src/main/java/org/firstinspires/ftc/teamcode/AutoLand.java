package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.corningrobotics.enderbots.endercv.CameraViewDisplay;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

import static org.opencv.imgproc.Imgproc.contourArea;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoLand", group="Autonomous")
public class AutoLand extends LinearOpMode {
    GoldVision goldVision = new GoldVision();

    //declare motors
    private DcMotor frontRightDrive = null;
    private DcMotor frontLeftDrive  = null;
    private DcMotor backRightDrive  = null;
    private DcMotor backLeftDrive   = null;

    private DcMotor rightLift = null;
    private DcMotor leftLift  = null;

    private DcMotor hook = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //map hardware
        frontRightDrive = hardwareMap.dcMotor.get("frontRightDrive");
        frontLeftDrive  = hardwareMap.dcMotor.get("frontLeftDrive");
        backRightDrive  = hardwareMap.dcMotor.get("backRightDrive");
        backLeftDrive   = hardwareMap.dcMotor.get("backLeftDrive");

        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift  = hardwareMap.dcMotor.get("leftLift");

        hook = hardwareMap.dcMotor.get("hook");


        waitForStart();
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //land
        hook.setTargetPosition(-3900);
        hook.setPower(-0.5);
        while(opModeIsActive() && hook.isBusy()){
            idle();
        }
        hook.setPower(0);
        leftLift.setPower(-0.3);
        rightLift.setPower(0.3);
        while(opModeIsActive() && (leftLift.getCurrentPosition()<1300)){
            telemetry.addData("leftLift", Integer.toString(leftLift.getCurrentPosition()));
            idle();
        }
        telemetry.addData("main", "loop finished");
        rightLift.setPower(0);
        leftLift.setPower(0);
        telemetry.addData("main", "power 0");
        //turn out of hook
        frontRightDrive.setPower(0.3);
        backRightDrive.setPower(0.3);
        frontLeftDrive.setPower(-0.3);
        backLeftDrive.setPower(-0.3);
        sleep(300);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        //move forward to get out of way
        frontRightDrive.setPower(0.3);
        backRightDrive.setPower(0.3);
        frontLeftDrive.setPower(0.3);
        backLeftDrive.setPower(0.3);
        sleep(300);
        //turn back to face minerals
        frontRightDrive.setPower(0.3);
        backRightDrive.setPower(0.3);
        frontLeftDrive.setPower(-0.3);
        backLeftDrive.setPower(-0.3);
        sleep(1300);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);

        goldVision.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        goldVision.setShowCountours(true);
        goldVision.enable();
        sleep(3000);

        int goldPosition = 1;

        double expectedGoldY = 0;
        double expectedGoldArea = 250;
        // get a list of contours from the vision system
        int bestContourIndex = 0;
        double bestContourScore = Double.MAX_VALUE;
        int maxTurnIterations = 10;
        for (int i = 0; i < maxTurnIterations; i++) {
            List<MatOfPoint> contours = goldVision.getContours();
            if(contours.size()>1) {
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
                double goldX = Imgproc.boundingRect(contours.get(bestContourIndex)).x + Imgproc.boundingRect(contours.get(bestContourIndex)).width / 2;
                if (goldX > 400 && goldX < 460)
                    break;
                else if (goldX < 400) {
                    frontRightDrive.setPower(0.1);
                    backRightDrive.setPower(0.1);
                    frontLeftDrive.setPower(-0.1);
                    backLeftDrive.setPower(-0.1);
                    sleep(100);
                    frontRightDrive.setPower(0);
                    backRightDrive.setPower(0);
                    frontLeftDrive.setPower(0);
                    backLeftDrive.setPower(0);
                } else {

                    frontRightDrive.setPower(-0.1);
                    backRightDrive.setPower(-0.1);
                    frontLeftDrive.setPower(0.1);
                    backLeftDrive.setPower(0.1);
                    sleep(100);
                    frontRightDrive.setPower(0);
                    backRightDrive.setPower(0);
                    frontLeftDrive.setPower(0);
                    backLeftDrive.setPower(0);
                }
            }
        }
        frontRightDrive.setPower(-0.3);
        backRightDrive.setPower(-0.3);
        frontLeftDrive.setPower(-0.3);
        backLeftDrive.setPower(-0.3);
        sleep(1500);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        //find position of gold mineral
        //rightLift.setPower(1);
        //leftLift.setPower(-1);
        //sleep(750);

        //turn out of hook and align
        /*rightLift.setPower(0);
        leftLift.setPower(0);
        frontRightDrive.setPower(0.5);
        backRightDrive.setPower(0.5);
        frontLeftDrive.setPower(0.5);
        backLeftDrive.setPower(0.5);
        sleep(300);
        frontRightDrive.setPower(-0.5);
        backRightDrive.setPower(-0.5);
        sleep(300);
        frontLeftDrive.setPower(-0.5);
        backLeftDrive.setPower(-0.5);
        sleep(600);
        frontLeftDrive.setPower(0.5);
        backLeftDrive.setPower(0.5);
        sleep(300);
        frontRightDrive.setPower(0.5);
        backRightDrive.setPower(0.5);
        sleep(300);*/

    }
}

