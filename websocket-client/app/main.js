define(["jquery", "domReady!", "./logger", "./player", "./socket",  
       "./random", "./card", "text!./opponent.html", "text!./card.html"],
       function($ , doc, logger, Player, Socket, random, card , opponentTemplate, cardTemplate ) {

           var addPlayer = function(playerUUID) {
               var currentPlayer = Player.getPlayer(playerUUID);
               logger.log(currentPlayer.name + " joined to the table", currentPlayer.color);

               var opponentHtml = $.parseHTML(opponentTemplate);
               $(opponentHtml).attr('id', playerUUID);
               $(".info" , opponentHtml).addClass(currentPlayer.color).text(currentPlayer.name);
               $(".table > .opponents").append(opponentHtml);
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

               onChangeState: function(event) {
                   logger.log("Changed state into: " + event.arguments[0]);
               }
           });

           if(!socket) return null;


           $(".deck").click(function(event) {
               var myHand = $(".player .hand");
               var freeSlot = $(".card-slot:empty:first", myHand);
               var hover = "";

               if(freeSlot.length <= 0) {
                   freeSlot = $(".shared .card-slot:empty:first");
                   hover = "hover";
               }

               if(freeSlot.length > 0) {
                   var cardHtml = $.parseHTML(cardTemplate);
                   card.random($(".front", cardHtml));
                   freeSlot.append(cardHtml);
                   freeSlot.addClass(hover);
               }
           });

           $("#form").submit(function(event){
               var value = $("#text-input").val();
               var message = {
                   command: "chat",
                   arguments: value.split(" ")
               };

               socket.send(JSON.stringify(message));

               $("#text-input").val("");
               event.preventDefault();
           });
       });
