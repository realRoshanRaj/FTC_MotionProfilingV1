public class Generator {

	/**
	 * @param config
	 * @param distance (in inches)
	 * @return
	 */
	public Trajectory generateTrajectory(Config config, double distance) {

		double time = config.max_velocity / config.max_acceleration;
		double area = time * config.max_velocity * 2;
		if (distance <= area) {
			return generateTriangular(config, distance);
		} else {
			return generateTrapizoidal(config, distance);
		}

	}

	public Trajectory generateTriangular(Config config, double distance) {
		Segment[] traj;
		double maxAccel = config.max_acceleration;
		double maxVel = config.max_velocity;
		double totalTime = 0;
		double currMaxVel, currAccel;

		totalTime = 2 * distance / maxVel;
		currAccel = maxVel * maxVel / distance;
		if (currAccel > maxAccel) {
			totalTime = Math.sqrt(4 * distance / maxAccel);
			currMaxVel = Math.sqrt(distance * maxAccel);
			currAccel = maxAccel;
			System.out.println("Changed VEL " + currMaxVel);
		} else {
			currMaxVel = maxVel;
		}
		traj = new Segment[(int) (totalTime / config.dt)];
		System.out.println(traj.length);

		double dt = 0;
		for (int i = 0; i < traj.length; i++) {
			if (dt < totalTime / 2) {
				traj[i] = new Segment(dt, 0, 0, currAccel * dt, currAccel);
			} else {
				// y = -currAccel + currMaxVel
				traj[i] = new Segment(dt, 0, 0, -currAccel * (dt - totalTime / 2) + currMaxVel, -currAccel);
			}
			dt += config.dt;
		}
		Trajectory trajectory = new Trajectory(traj);
//		Trajectory.printContent(trajectory);
		double[] vel = new double[traj.length], time = new double[traj.length];
		for (int i = 0; i < vel.length; i++) {
			vel[i] = trajectory.get(i).velocity;
			time[i] = trajectory.get(i).dt;
		}

		new Graph("Time", "Velocity", time, vel).start();
		
		return trajectory;
	}

	public Trajectory generateTrapizoidal(Config config, double distance) {
		Segment[] traj;
		double maxAccel = config.max_acceleration;
		double maxVel = config.max_velocity;
		double totalTime = 0;
		double currVel, currAccel;

		double time = maxVel / maxAccel;
		double area = time * maxVel * 2;
		if (area < distance) {

			totalTime = (2 * time) + (distance - area) / maxVel;

			traj = new Segment[(int) (totalTime / config.dt)];

			double dt = 0;
			for (int i = 0; i < traj.length; i++) {
				System.out.println(dt);
				if (dt < time) {
					currAccel = maxAccel;
					traj[i] = new Segment(dt, 0, 0, currAccel * dt, currAccel);
				} else if (dt < (totalTime - time)) {
					currAccel = 0.0;
					currVel = maxVel;
					traj[i] = new Segment(dt, 0, 0, currVel, currAccel);
				} else {
					currAccel = maxAccel;
					currVel = maxVel;
					traj[i] = new Segment(dt, 0, 0, -currAccel * (dt - (totalTime - time)) + currVel, -currAccel);
				}
				dt += config.dt;
			}

			Trajectory trajectory = new Trajectory(traj);
//			Trajectory.printContent(trajectory);
			double[] vel = new double[traj.length], timeGraph = new double[traj.length];
			for (int i = 0; i < vel.length; i++) {
				vel[i] = trajectory.get(i).velocity;
				timeGraph[i] = trajectory.get(i).dt;
			}

			new Graph("Time", "Velocity", timeGraph, vel).start();

		} else {
			return generateTriangular(config, distance);
		}
		return null;
	}
}