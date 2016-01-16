define(["jquery", "domReady!", "./logger", "./player", "./socket", "./string"], function($ , doc, logger, player, socket ) {
    var myUUID = null;

    if(!socket) return null;

    logger.log("Connecting...");

    var onChat = function(event) {

        var playerUUID = event.arguments.shift();
        var currentPlayer = player.getPlayer(playerUUID);
        logger.log(currentPlayer.color.capitalizeFirstLetter() + " said: " + event.arguments.join(" "), currentPlayer.color);
    };

    var onClientConnect = function(event) {

        var playerUUID = event.arguments.shift();
        var currentPlayer = player.getPlayer(playerUUID);
        logger.log(currentPlayer.color.capitalizeFirstLetter() + " joined to the table");
    };

    socket.setHandlers({
        onChat: onChat,
        onClientConnect: onClientConnect
    });

    $("#form").submit(function(event){
        var value = $("#text-input").val();
        var message = {
            command: "chat",
            arguments: value.split(" ")
        };

        socket.send(JSON.stringify(message));

        $("#text-input").val("");
        $("#log").animate({ scrollTop: $("#log")[0].scrollHeight}, 1000);
        event.preventDefault();
    });
});
