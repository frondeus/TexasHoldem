define(
    ["jquery",
        "domReady!",
        "./logger",
        "./player",
        "./socket",
        "./random",
        "./card",
        "text!./opponent.html",
        "text!./card.html",
        "text!./command.html"],
        function($ , doc, logger, Player, Socket, random, card , opponentTemplate, cardTemplate, commandTemplate ) {

            var addPlayer = function(playerUUID) {
                var currentPlayer = Player.getPlayer(playerUUID);
                logger.log(currentPlayer.name + " joined to the table", currentPlayer.color);

                var opponentHtml = $.parseHTML(opponentTemplate);
                $(opponentHtml).attr('id', playerUUID);
                $(".info" , opponentHtml).addClass(currentPlayer.color).text(currentPlayer.name);
                $(".table > .opponents").append(opponentHtml);
            };

            var addHandCard = function(color, value){
            
                var myHand = $(".player .hand");
                var freeSlot = $(".card-slot:empty:first", myHand);
                var hover = "hover"

                //if(freeSlot.length <= 0) {
                    //freeSlot = $(".shared .card-slot:empty:first");
                    //hover = "hover";
                //}

                if(freeSlot.length > 0) {
                    var cardHtml = $.parseHTML(cardTemplate);

                    card.create($(".front", cardHtml), value,color );
                    freeSlot.append(cardHtml);
                    freeSlot.addClass(hover);
                }
            };

            var socket = Socket({
                onConnected: function(event) {
                    Player.setUUID(event.arguments.shift());
                    for(var p in event.arguments) {
                        var playerUUID = event.arguments[p];
                        addPlayer(playerUUID);
                    }
                },

                onClientConnect: function(event) {
                    var playerUUID = event.arguments.shift();
                    addPlayer(playerUUID);
                },

                onClientDisconnect: function(event) {
                    var playerUUID = event.arguments.shift();
                    var currentPlayer = Player.getPlayer(playerUUID);
                    logger.log(currentPlayer.name + " left the table", currentPlayer.color);

                    var html = $("#"+playerUUID);
                    if(html) html.remove();
                },

                onChat: function(event) {
                    var playerUUID = event.arguments.shift();
                    var currentPlayer = Player.getPlayer(playerUUID);
                    logger.log(currentPlayer.name + " said: " + event.arguments.join(" "), currentPlayer.color);
                },

                onTurn: function(event) {
                    var playerUUID = event.arguments.shift();

                    if(Player.myUUID() == playerUUID) logger.log("Yours turn");
                    else {
                        var currentPlayer = Player.getPlayer(playerUUID);
                        logger.log(currentPlayer.name + "s turn", currentPlayer.color);
                        $(".commands button").each(function(){
                            $(this).prop("disabled", true);
                        });
                    }
                },

                onCommands: function(event) {
                    logger.log("Available commands: " + event.arguments.join(" "));
                    $(".commands").html("");
                    for(var i in event.arguments) {
                        var command = event.arguments[i];

                        var commandHtml = $($.parseHTML(commandTemplate));
                        commandHtml.text(command).data("command", command).click(function(event){
                            var value = $("#text-input").val();
                            var message = {
                                command: $(this).data("command"),
                                arguments: value.split(" ")
                            };

                            socket.send(JSON.stringify(message));

                            $("#text-input").val("");
                            event.preventDefault();
                        });

                        $(".commands").append(commandHtml);
                    }
                },

                onHand: function(event) {
                    console.table(event);
                    addHandCard(event.arguments[0], event.arguments[1]);
                    addHandCard(event.arguments[2], event.arguments[3]);
                },

                onChangeState: function(event) {
                    logger.log("Changed state into: " + event.arguments[0]);
                }
            });

            if(!socket) return null;


            $(".deck").click(function(event) {
            });

        });
