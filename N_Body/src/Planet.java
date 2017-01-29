
public class Planet {
	double myXPos;
	double myYPos;
	double myXVel;
	double myYVel;
	double myMass;
	String myFileName;
	public Planet(double x_pos, double y_pos,
			double x_vel, double y_vel,
			double m, String img)
	{
		myXPos = x_pos;
		myYPos = y_pos;
		myXVel = x_vel;
		myYVel = y_vel;
		myMass = m;
		myFileName = img;
	}
	
	public Planet(Planet P)
	{
		this.myXPos = P.myXPos;
		this.myYPos = P.myYPos;
		this.myXVel = P.myXVel;
		this.myYVel = P.myYVel;
		this.myMass = P.myMass;
		this.myFileName = P.myFileName;
	}
	
	public double calcDistance(Planet otherPlanet)
	{
		double x_difference = myXPos - otherPlanet.myXPos;
		double y_difference = myYPos - otherPlanet.myYPos;
		double distance_sq = Math.pow(x_difference, 2) + Math.pow(y_difference, 2);
		double distance = Math.pow(distance_sq, 0.5);
		return distance;
	}
	
	
	public double calcForceExertedBy(Planet otherPlanet)
	{
		double CONSTANT_G = 6.67 * Math.pow(10, -11);
		double distance = calcDistance(otherPlanet);
		double sum_force = CONSTANT_G * myMass * otherPlanet.myMass/ Math.pow(distance, 2);
		return sum_force;
	}
	
	public double calcForceExertedByX(Planet otherPlanet)
	{
		double sum_force = calcForceExertedBy(otherPlanet);
		double distance = calcDistance(otherPlanet);
		double force_x = sum_force * (otherPlanet.myXPos - myXPos)/distance;
		return force_x;
	}
	
	public double calcForceExertedByY(Planet otherPlanet)
	{
		double sum_force = calcForceExertedBy(otherPlanet);
		double distance = calcDistance(otherPlanet);
		double force_y = sum_force * (otherPlanet.myYPos - myYPos)/distance;
		return force_y;
	}
	
	public double calcNetForceExertedByX(Planet[] allPlanets)
	{	
		double net_force_x = 0;
		for (Planet each_planet: allPlanets)
		{
			if ( !each_planet.equals(this) )
			{
				net_force_x += calcForceExertedByX(each_planet);
			}
		}
		return net_force_x;
	}
	
	public double calcNetForceExertedByY(Planet[] allPlanets)
	{
		double net_force_y = 0;
		for (Planet each_planet: allPlanets)
		{
			if ( !each_planet.equals(this) )
			{
				net_force_y += calcForceExertedByY(each_planet);
			}
		}
		return net_force_y;
	}
	
	public void update(double time_duraion, double add_x_force, double add_y_force)
	{
		myXVel = myXVel + time_duraion * add_x_force/myMass;
		myYVel = myYVel + time_duraion * add_y_force/myMass;
		myXPos = myXPos + myXVel * time_duraion;
		myYPos = myYPos + myYVel * time_duraion;  
	}
	
	public void draw()
	{	
		String picture_address = "images/" + myFileName;
		StdDraw.picture(myXPos, myYPos, picture_address);
	}
	
}
