define(["./random"], function(random) {
    var cardColors = ["spade", "diamond", "heart", "club"];
    var cardValues = ["A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" ];

    var randomColor = function() {
        return random.array(cardColors);
    };

    var randomValue = function() {
        return random.array(cardValues);
    };

    var clearCard = function(card) {
        for(var c in cardColors)
            card.removeClass(cardColors[c]);

        card.text("");
    };

    var randomCard = function(card) {
        clearCard(card);
        card.addClass(randomColor()).text(randomValue());
    };

    return {
        randomColor: randomColor,
        randomValue: randomValue,
        clear: clearCard,
        random: randomCard
    };
});
