import java.util.Random;

class Mutex
{
	static int semaphore;
	Mutex()
	{
		semaphore = 1;
	}
	public static void WaitNow()
	{
		semaphore--;
		//System.out.println("Wait");
		while(semaphore < 0); 
		//System.out.println("WaitNow"+semaphore);
		return;
	}
	public static void SignalNow()
	{
		semaphore++;
		//System.out.println("SIGNAL");
		//System.out.println("SignalNow"+semaphore);
		return;
	}
}

class WaterMolecule
{
	public static void bond()
	{
		System.out.println("\nH2O molecule was formed!");
		Oxygen.DecrementO();
		Hydrogen.DecrementH();
		Hydrogen.DecrementH();
		System.out.println("Number of Hydrogen atoms left : " + Hydrogen.ReturnH() + "\tNumber of Oxygen atoms left : " + Oxygen.ReturnO() + "\n\n");
	}
}

class Oxygen extends Thread
{
	public static int nOxy; //Number of oxygen threads
	public void run()
	{
		Mutex.WaitNow();
		//CS Start
		nOxy++;
		System.out.println("OXYGEN");	
		if(Hydrogen.ReturnH() >= 2)
			WaterMolecule.bond();
		//CS End
		Mutex.SignalNow();
	}
	public static int ReturnO()
	{
		return nOxy;
	}
	public static void DecrementO()
	{
		nOxy--;
	}
}

class Hydrogen extends Thread
{
	public static int nHydro; //Number of hydrogen threads
	public void run()
	{
		Mutex.WaitNow();
		//CS Start
		nHydro++;
		System.out.println("HYDROGEN");
		if(nHydro >= 2 && Oxygen.ReturnO() >= 1)
			WaterMolecule.bond();
		//CS End
		Mutex.SignalNow();
	}
	public static int ReturnH()
	{
		return nHydro;
	}
	public static void DecrementH()
	{
		nHydro--;
	}
}

class MakeHydro extends Thread
{
	public void run()
		{
			Random randomGenerator = new Random();
			for(int i=0;i<10;i++)
			{
				new Hydrogen().start();
				try
				{
					sleep(randomGenerator.nextInt(30));
				}
				catch(Exception E){}
			}
		}
}

class MakeOxy extends Thread
{
	public void run()
	{
		for(int i=0;i<5;i++)
		{
			Random randomGenerator = new Random();
			new Oxygen().start();
			try
			{
				sleep(randomGenerator.nextInt(30));
			}
			catch(Exception E){}
		}
	}
}

class H2O
{
	public static void main(String args[])
	{
		System.out.println("\nRandom string of atoms generated is : ");
		new Mutex();
		new MakeHydro().start();
		new MakeOxy().start();
	}
}