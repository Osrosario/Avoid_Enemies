/*
 * A holder class to hold the number of enemies. Methods present are to set and get the needed values.
 */

public class EnemyCounterHolder 
{
	private int enemyCount;
	private int numOfEnemy;
	private int originalNumOfEnemies;

	public void incrementEnemyCount()
	{
		enemyCount++;
	}
	
	public void decrementEnemyCount()
	{
		enemyCount--;
	}
	
	public void setNumOfEnemy(int numOfEnemy)
	{
		this.numOfEnemy = numOfEnemy;
		this.enemyCount = numOfEnemy; 
		this.originalNumOfEnemies = numOfEnemy;
	}
	
	public int getNumOfEnemy()
	{
		return numOfEnemy;
	}
	
	public void setEnemyCount(int enemyCount)
	{
		this.enemyCount = enemyCount;
	}
	
	public int getEnemyCount()
	{
		return enemyCount;
	}
	
	public void resetEnemyCount()
	{
		this.numOfEnemy = originalNumOfEnemies;
		this.enemyCount = originalNumOfEnemies;
	}
}
