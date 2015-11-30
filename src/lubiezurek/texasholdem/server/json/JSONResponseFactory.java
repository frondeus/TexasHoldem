package lubiezurek.texasholdem.server.json;

import lubiezurek.texasholdem.server.IResponse;
import lubiezurek.texasholdem.server.IResponseFactory;

public class JSONResponseFactory implements IResponseFactory {

	@Override
	public IResponse CreateResponse() {
		return new JSONResponse();
	}

}
