package lubiezurek.texasholdem.server.deal;


import lubiezurek.texasholdem.server.IState;

public class Deal{
	IState currentState;

	Deal(){
		
	}

	IState getState(){ return currentState; }

	void setState(IState newState){ currentState = newState; }



}
