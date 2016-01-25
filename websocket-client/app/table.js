define(
    ["domReady!",
    "jquery",
    "text!./card.html",
    "./card"
    ],
    function(document, jquery, cardTemplate, Card){
        
        var clearTable = function() {
            $(".card-slot").each(function(){
                $(this).html("").removeClass("hover");
            });
        };

        var addShared = function(color, value) {
            var freeSlot = $(".shared .card-slot:empty:first");
            if(freeSlot.length > 0) {
                var cardHtml = $.parseHTML(cardTemplate);
                Card.create($(".front", cardHtml), value, color);
                freeSlot.append(cardHtml);
                setTimeout(function(){
                    freeSlot.addClass("hover");
                }, 1.5);
            }
        };

        var addHandCard = function(color, value) {
            var freeSlot = $(".player .hand .card-slot:empty:first");
            if(freeSlot.length > 0) {
                var cardHtml = $.parseHTML(cardTemplate);
                Card.create($(".front", cardHtml), value, color);
                freeSlot.append(cardHtml);
            }
        };

        var addOtherHandCard = function(other, color, value) {
            var opponent = $("#"+other);
            var freeSlot = $(".hand .card-slot:empty:first", opponent);
            console.log("Opponent cards: " + other);
            console.log(opponent);
            console.log(freeSlot);
            if(freeSlot.length > 0) {
                console.log("Found slot");
                var cardHtml = $.parseHTML(cardTemplate);
                Card.create($(".front", cardHtml), value, color);
                freeSlot.append(cardHtml);
                setTimeout(function(){
                    freeSlot.addClass("hover");
                }, 1.5);
            }
        };

        //$(".shared .card-slot, .player .hand .card-slot").each(function(){
        $(".card-slot").each(function(){
            var slot = $(this);
            slot.click(function(){
                slot.toggleClass("hover");
            });
        });

        return {
            clear: clearTable,
            addSharedCard: addShared,
            addHandCard: addHandCard,
            addOtherHandCard: addOtherHandCard
        };
    }
);
