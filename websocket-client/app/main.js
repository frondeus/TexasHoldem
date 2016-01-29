define(
    ["jquery",
        "domReady!",
        "./logger",
        "./player",
        "./socket",
        "./random",
        "./table",
        "text!./opponent.html",
        "text!./command.html",
        "text!./bet.html"
    ],
        function($ , doc, logger, Player, Socket, random, Table, opponentTemplate, commandTemplate, betTemplate ) {

            var state = "";
            var socket;

            var addPlayer = function(playerUUID) {
                var currentPlayer = Player.getPlayer(playerUUID);
                logger.log(currentPlayer.name + " joined to the table", currentPlayer.color);

                var opponentHtml = $.parseHTML(opponentTemplate);
                $(opponentHtml).addClass(playerUUID);
                //$(opponentHtml).attr('id', playerUUID);
                $(".info" , opponentHtml).addClass(currentPlayer.color).text(currentPlayer.name);
                $(".table > .opponents").append(opponentHtml);
            };

            var addCommands = function(commands) {
                logger.log("Available commands: " + commands.join(" "));
                $(".commands").html("");
                for(var i in commands) {
                    var command = commands[i];

                    var commandHtml = $($.parseHTML(commandTemplate));
                    commandHtml.text(command).data("command", command).click(function(event){
                        var value= $("#text-input").val();
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
            };

            $("#login-form button").click(function(){
                var address = $("#address-input").val();

                socket = Socket(address, {
                    onConnected: function(event) {
                        Player.setUUID(event.arguments.shift());
                        for(var p in event.arguments) {
                            var playerUUID = event.arguments[p];
                            addPlayer(playerUUID);
                        }

                        addCommands(["chat"]);
                    },

                    onClientConnect: function(event) {
                        var playerUUID = event.arguments.shift();
                        addPlayer(playerUUID);
                    },

                    onClientDisconnect: function(event) {
                        var playerUUID = event.arguments.shift();
                        var currentPlayer = Player.getPlayer(playerUUID);
                        logger.log(currentPlayer.name + " left the table", currentPlayer.color);

                        var html = $("."+playerUUID);
                        html.each(function(){
                            $(this).remove();
                        });
                    },

                    onChat: function(event) {
                        var playerUUID = event.arguments.shift();
                        var currentPlayer = Player.getPlayer(playerUUID);
                        logger.log(currentPlayer.name + " said: " + event.arguments.join(" "), currentPlayer.color);
                    },

                    onTurn: function(event) {
                        var playerUUID = event.arguments.shift();

                        if(Player.myUUID() == playerUUID){
                            switch(state) {
                            case "LicitationNoLimit":
                                addCommands(["Bet", "Check", "Fold"]);
                                break
                            default:
                                console.err("Undefined state!");
                                break;
                            }
                            logger.log("Yours turn");
                        } 
                        else {
                            var currentPlayer = Player.getPlayer(playerUUID);
                            logger.log(currentPlayer.name + "s turn", currentPlayer.color);
                            $(".commands button").each(function(){
                                $(this).prop("disabled", true);
                            });
                        }
                    },

                    onHand: function(event) {
                        console.log("On Hand");
                        console.log(event);
                        Table.addHandCard(event.arguments[0], event.arguments[1]);
                        Table.addHandCard(event.arguments[2], event.arguments[3]);
                    },

                    onSharedCard: function(event) {
                        console.log("On SharedCard");
                        console.log(event);
                        Table.addSharedCard(event.arguments[0], event.arguments[1]);
                    },

                    onOtherHand: function(event) {
                        var playerUUID = event.arguments.shift();
                        console.log("On other hand");
                        console.log(event);
                        Table.addOtherHandCard(playerUUID , event.arguments[0], event.arguments[1]);
                        Table.addOtherHandCard(playerUUID, event.arguments[2], event.arguments[3]);
                    },

                    onBet: function(event) {
                        var playerUUID = event.arguments.shift();
                        var currentPlayer = Player.getPlayer(playerUUID);

                        var tableBets = $(".table .bets");
                        var betHtml = $("."+ playerUUID, tableBets);
                        if(betHtml.length <= 0) {
                            betHtml = $($.parseHTML(betTemplate));
                            betHtml.addClass(playerUUID)
                                .addClass(currentPlayer.color);
                            $(".table .bets").append(betHtml);
                        }

                        betHtml.text(currentPlayer.name + " " + event.arguments[0]+"/"+event.arguments[1]+"$");
                    },

                    onChangeState: function(event) {
                        logger.log("Changed state into: " + event.arguments[0]);
                        state = event.arguments[0];
                        switch(event.arguments[0]) {
                            case "LicitationNoLimit":
                                Table.clear();
                                break;
                        }
                    }
                });

                if(!socket) return null;

                send_message = function(msg) {
                    var command = msg.shift();
                    var message = {
                        command: command,
                        arguments: msg
                    };
                    socket.send(JSON.stringify(message));
                    return JSON.stringify(message);
                }

                $("#login-form").removeClass("visible");
            });


        });
