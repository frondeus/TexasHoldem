define(["jquery", "domReady!", "./logger", "./player", "./socket", "./string", "./random", "./card"],
       function($ , doc, logger, player, socket, str, random, card ) {

           var myUUID = null;

           if(!socket) return null;

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

           $(".deck").click(function(event) {
               $(this).toggleClass("hover");
               if($(this).hasClass("hover")) {
                   card.random($(".front", this));

                   var firstBlank = $(".table .card.blank")[0];
                   if(firstBlank) {
                       var firstSlot = $(firstBlank).removeClass("blank").parent();
                       firstSlot.addClass("hover");

                       var firstFront = $(".front", firstSlot);
                       card.random(firstFront);
                   }
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
