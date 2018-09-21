
public class Trajectory {

	public Segment[] segments;

	public Trajectory(Segment[] seg) {
		segments = seg;
	}

	public Segment get(int index) {
		return segments[index];
	}

	public int length() {
		return segments.length;
	}

	public static void printContent(Trajectory traj) {
		for (Segment i : traj.segments) {
			System.out.print("Velocity " + i.velocity);
			System.out.println("Accel " + i.acceleration);
		}
	}
	
	public static void printContent(Segment[] segs) {
		printContent(new Trajectory(segs));
	}

}