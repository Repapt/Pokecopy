package pokemon;


public class Pokemon { //creates new pokemon object
	
	public int health;
	public String name;
	public int attack;
	public int defense;
	public int move1;
	public int move2;
	public int move3;
	public int move4;
	public int origHealth;
	public int origAtk;
	public int origDef;
	public int id;
	//battle variables
	public int damage = 0;
	public boolean protect = false;
	public int resting = 0;
	public int webbed = 0;
	public int stunned = 0;
	
	/* move ids
	 * 0-Metal Claw
	 * 1-Hyper Beam
	 * 2-Quick Attack
	 * 3-Thunderbolt
	 * 4-String Shot
	 * 5-Repulsor Blast
	 * 6-Shield Throw
	 * 7-Iron Defense
	 * 8-Flash Cannon
	 * 9-Taser Web
	 * 10-High Jump Kick
	 * 11-Energy Daggers
	 * 12-Body Slam
	 * 13-Super Heal
	 * 14-Dracarys
	 * 15-Iron Tail
	 * 16-Dragon Dance
	 * 
	 * Pokemon ids
	 * 0-Spiderling
	 * 1-Spidermon
	 * 2-Ironmite
	 * 3-Cap
	 * 3-Black Panther
	 * 
	 */
	
	//9,10,13
	//pokemon stats (attack, health, etc)
	private static int[][] stats = {{310,350,250,4,9,10,13}, {400,370,340,1,3,5,7}
	,{350,300,420,14,6,10,12},{350,360,400,0,10,11,2}, {400,410,310,15,16,18,17}};
	//pokemon names
	private static String[] names = {"SPIDERLING", "IRONMITE", "CAPTAIN AMERICA","BLACK PANTHER", "DEADPOOL"};
	
	public Pokemon(int num) {
		
		//assigns variables based on stats array
		id = num;
		health = stats[num][0];
		attack = stats[num][1];
		defense = stats[num][2];
		origHealth = stats[num][0];
		origAtk = stats[num][1];
		origDef = stats[num][2];
		move1 = stats[num][3];
		move2 = stats[num][4];
		move3 = stats[num][5];
		move4 = stats[num][6];
		
		name = names[num];
	
	
	}
	
}
