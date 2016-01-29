package lubiezurek.texasholdem.server.model.card;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

public enum CardValue{
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10),
	JACK(11),
	QUEEN(12),
	KING(13),
	ACE(14);

	private int value;

	CardValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	@Override
	public String toString(){
		if(value <= 10) return Integer.toString(value);
		switch (value){
			case 11: 	return "J";
			case 12: 	return "Q";
			case 13:	return "K";
			case 14:	return "A";
		}
        return null;
    }
}