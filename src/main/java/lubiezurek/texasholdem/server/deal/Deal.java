package lubiezurek.texasholdem.server.deal;

import states.IState;

public class Deal{
	IState currentState;

	Deal(){

	}

	IState getState(){
		return currentState;
	}



}