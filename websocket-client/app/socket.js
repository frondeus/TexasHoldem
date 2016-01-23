define(["domReady!", "./logger"], function(doc, logger){
    if(!window.WebSocket) {
        alert("Websocket is not supported");
        return null;
    } 

    var eventHandlers = {};

    var init  = function(_eventHandlers) {
        console.log("Set Handlers");
        eventHandlers = _eventHandlers;

        var socket = new WebSocket("ws://localhost:7777");
        socket.onopen = function() {
            logger.log("Connected");
        };

        socket.onclose = function() {
            logger.log("Disconnected");
        };

        socket.onerror = function(event) {
            console.log(event);
            logger.error("A socket error ocurred");
        };


        var onServerEvent = function(event) {

            var eventHandler = eventHandlers["on" + event.type];
            if(eventHandler) 
                eventHandlers["on" + event.type](event);
            else 
                console.error("Unhandled event.type: " + event.type);
        };

        var onServerResponse = function(response) {
            if(response.status != "Ok")
                logger.error(response.message);
            else {
            }
        };

        socket.onmessage = function(event) {
            var message = JSON.parse(event.data);

            switch(message.messageType) {
            case "ServerEvent":
                onServerEvent(message);
                break;
            case "ServerResponse":
                onServerResponse(message);
                break;
            default:
                console.err("Unknown message type: " + message.messageType);
            }

            console.log(message);
        };
        return socket;
    };

    return init;
});
