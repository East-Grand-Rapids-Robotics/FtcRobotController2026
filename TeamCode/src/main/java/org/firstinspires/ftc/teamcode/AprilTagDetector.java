package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

/*
 * Reusable helper that uses the webcam to find AprilTags.
 * Based on the SDK sample ConceptAprilTag.
 *
 * Use it from any OpMode:
 *
 *     AprilTagDetector tags = new AprilTagDetector(hardwareMap);
 *     ...
 *     AprilTagSingleDetection tag = tags.getTag(20);
 *     if (tag != null && tag.metadata != null) {
 *         double inchesAway = tag.ftcPose.range;
 *         double degreesLeftRight = tag.ftcPose.bearing;
 *     }
 *     ...
 *     tags.close();   // when the OpMode is done with the camera
 *
 * The webcam must be in the robot configuration on the Control Hub. The default
 * name is "Webcam 1"; pass a different name to the constructor if yours differs.
 *
 * ftcPose (distances in inches, angles in degrees) is only filled in for tags in
 * the SDK's tag library (this season's field tags plus some test tags). For any
 * other tag, metadata and ftcPose are null and only the id and pixel center are known.
 */
public class AprilTagDetector {

    public static final String DEFAULT_WEBCAM_NAME = "Cam1";

    private final AprilTagProcessor aprilTag;
    private final VisionPortal visionPortal;

    public AprilTagDetector(HardwareMap hardwareMap) {
        this(hardwareMap, DEFAULT_WEBCAM_NAME);
    }

    public AprilTagDetector(HardwareMap hardwareMap, String webcamName) {
        aprilTag = new AprilTagProcessor.Builder()
                // Defaults are fine to start with. See ConceptAprilTag for the other options
                // (tag library, output units, lens intrinsics for camera calibration).
                .build();

        // Decimation trades range for speed: 3 (default) is fast, 1 sees small tags farther away.
        // aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, webcamName))
                .addProcessor(aprilTag)
                .build();
    }

    /** Every tag (and tag cluster) seen in the most recent camera frame. */
    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

    /** The tag with this id if the camera sees it right now, otherwise null. */
    public AprilTagSingleDetection getTag(int id) {
        for (AprilTagDetection detection : aprilTag.getDetections()) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection single = (AprilTagSingleDetection) detection;
                if (single.id == id) {
                    return single;
                }
            }
        }
        return null;
    }

    /**
     * The closest scoring hive the camera sees right now ("RED SCORING" or "BLUE SCORING"),
     * otherwise null. Its ftcPose is measured to the center of the hive, not to one tag,
     * and it still works when only some of the hive's four tags are visible.
     */
    public AprilTagClusterDetection getScoringCluster() {
        AprilTagClusterDetection closest = null;
        for (AprilTagDetection detection : aprilTag.getDetections()) {
            if (detection instanceof AprilTagClusterDetection && detection.ftcPose != null) {
                AprilTagClusterDetection cluster = (AprilTagClusterDetection) detection;
                if (cluster.metadata.name.contains("SCORING")
                        && (closest == null || cluster.ftcPose.range < closest.ftcPose.range)) {
                    closest = cluster;
                }
            }
        }
        return closest;
    }

    /** True if the camera sees the tag with this id right now. */
    public boolean seesTag(int id) {
        return getTag(id) != null;
    }

    /** Adds a line per detected tag to the Driver Station telemetry. Call telemetry.update() afterwards. */
    public void addTelemetry(Telemetry telemetry) {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", detections.size());

        for (AprilTagDetection detection : detections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection single = (AprilTagSingleDetection) detection;
                if (single.metadata != null) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", single.id, single.metadata.name));
                    addPose(telemetry, detection);
                } else {
                    telemetry.addLine(String.format("\n==== (ID %d) Unknown", single.id));
                    telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", single.center.x, single.center.y));
                }
            } else {
                AprilTagClusterDetection cluster = (AprilTagClusterDetection) detection;
                telemetry.addLine(String.format("\n==== Tag Cluster (%s)", cluster.metadata.name));
                telemetry.addLine(String.format("Percent tags found: %d", cluster.percentClusterFound));
                addPose(telemetry, detection);
            }
        }

        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");
    }

    private void addPose(Telemetry telemetry, AprilTagDetection detection) {
        telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
        telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
        telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
    }

    /** Pauses the camera stream to save CPU. Detections stop updating until resumeStreaming(). */
    public void stopStreaming() {
        visionPortal.stopStreaming();
    }

    public void resumeStreaming() {
        visionPortal.resumeStreaming();
    }

    /** Releases the camera. Call once when the OpMode no longer needs it. */
    public void close() {
        visionPortal.close();
    }
}
