define(["jquery", "domReady!", "./logger", "./player", "./socket",  
       "./random", "./card", "text!./opponent.html", "text!./card.html"],
       function($ , doc, logger, player, socket, random, card , opponentTemplate, cardTemplate ) {

           var myUUID = null;

           if(!socket) return null;

           var onChat = function(event) {

               var playerUUID = event.arguments.shift();
               var currentPlayer = player.getPlayer(playerUUID);
               logger.log(currentPlayer.name + " said: " + event.arguments.join(" "), currentPlayer.color);
           };

           var onClientConnect = function(event) {

               var playerUUID = event.arguments.shift();
               var currentPlayer = player.getPlayer(playerUUID);
               logger.log(currentPlayer.name + " joined to the table");


               var opponentHtml = $.parseHTML(opponentTemplate);
               $(".info" , opponentHtml).addClass(currentPlayer.color).text(currentPlayer.name);
               $(".table > .opponents").append(opponentHtml);
           };

           var onChangeState = function(event) {
               logger.log("Changed state into: " + event.arguments[0]);
           };

           socket.setHandlers({
               onChat: onChat,
               onClientConnect: onClientConnect,
               onChangeState: onChangeState
           });


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
