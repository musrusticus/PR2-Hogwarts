package a11807184;

public enum MagicLevel {
	NOOB(50),
	ADEPT(100),
	STUDENT(200),
	EXPERT(500),
	MASTER(1000);
	
	private final int basicMana;
	
    MagicLevel(int basicMana) {
	   this.basicMana = basicMana;
    }
   
    public int toMana() {
	   return basicMana;
    }
   
    @Override
    public String toString() {
	   return "*".repeat(this.ordinal() +1); //returns position in enum, thus we add +1
    }
}