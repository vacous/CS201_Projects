import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBody {
	
	
	public static void main(String[] args){
		double T = 200000000.0;
		double dt = 25000.0;
		String pfile = "data/planets.txt";
		if (args.length > 2) {
			T = Double.parseDouble(args[0]);
			dt = Double.parseDouble(args[1]);
			pfile = args[2];
		}	
		Planet[] planets = readPlanets(pfile);
		double radius = readRadius(pfile);
		
		// create animation
		double time = 0;
		int num_planet = planets.length;
		while (time < T)
		{
			double[] x_force = new double[num_planet];
			double[] y_force = new double[num_planet];
			// net force 
			for (int idx =0; idx < num_planet; idx += 1)
			{	
				Planet each_planet = planets[idx]; 
				double current_net_force_x = each_planet.calcNetForceExertedByX(planets);
				x_force[idx] = current_net_force_x;
				double current_net_force_y = each_planet.calcNetForceExertedByY(planets);
				y_force[idx] = current_net_force_y;
			}	
			// update all planet 
			for (int idx = 0; idx < num_planet; idx +=1)
			{
				Planet each_planet = planets[idx];
				each_planet.update(dt, x_force[idx], y_force[idx]);
			}
			// display images
			StdDraw.setScale(-radius, radius);
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for (Planet each_planet: planets)
			{
				each_planet.draw();
				
			}
			StdDraw.show(10);
			time += dt;
		}
		System.out.printf("%d\n", planets.length);
		System.out.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
		    System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		   		              planets[i].myXPos, planets[i].myYPos, 
		                      planets[i].myXVel, planets[i].myYVel, 
		                      planets[i].myMass, planets[i].myFileName);	
		}

		
	}
	
	public static double readRadius(String f_name)
	{
		File input_file = new File(f_name);
		Scanner file = null;
		try {
			file = new Scanner(input_file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.nextDouble();
		double radius = file.nextDouble();
		return radius;
	}
	
	public static Planet[] readPlanets(String f_name)
	{
		File input_file = new File(f_name);
		Scanner file = null;
		try {
			file = new Scanner(input_file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num_planet = file.nextInt();
		Planet[] planet_array = new Planet[num_planet];
		file.next(); // skip the radius 
		for (int idx = 0; idx < num_planet; idx += 1)
		{
			double current_x_pos = file.nextDouble();
			double current_y_pos = file.nextDouble();
			double current_x_vel = file.nextDouble();
			double current_y_vel = file.nextDouble();
			double current_mass = file.nextDouble();
			String current_img = file.next();
			Planet current_planet = new Planet(current_x_pos,current_y_pos,
					current_x_vel,current_y_vel, current_mass,
					current_img);
			planet_array[idx] = current_planet;
		}
		
		return planet_array;
	}
}
