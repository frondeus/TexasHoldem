define(["domReady!", "./logger"], function(doc, logger){
    if(!window.WebSocket) {
        alert("Websocket is not supported");
        return null;
    } 


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

    var eventHandlers = {};

    socket.setHandlers = function(_eventHandlers) {
        eventHandlers = _eventHandlers;
    };

    var onServerEvent = function(event) {
        console.log("Event");

        var eventHandler = eventHandlers["on" + event.type];
        if(eventHandler) 
            eventHandlers["on" + event.type](event);
        else 
            console.error("Unhandled event.type: " + event.type);
    };

    var onServerResponse = function(response) {
        console.log("Response");
        if(response.status != "Ok")
            logger.error(response.message);
        else {
        }
    };

    socket.onmessage = function(event) {
        var message = JSON.parse(event.data);

        if(message.messageType.endsWith("ServerEvent"))
            onServerEvent(message);
        else if(message.messageType.endsWith("ServerResponse"))
            onServerResponse(message);
        else 
            console.err("Unknown message type: " + message.messageType);

        console.log(message);
    };


    return socket;
});
