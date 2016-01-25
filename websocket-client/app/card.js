define(["./random"], function(random) {
    var cardColors = ["spades", "diamonds", "hearts", "clubs"];
    var cardValues = ["A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" ];
    var cardValuesNames = ["ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
    "TEN", "JACK", "QUEEN", "KING"];
    


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

    var createCard = function(card, value, color) {
        clearCard(card);
        color = color.toLowerCase();
        var index = cardValuesNames.indexOf(value);
        var val = cardValues[index];

        card.addClass(color).text(val);


    };

    return {
        randomColor: randomColor,
        randomValue: randomValue,
        clear: clearCard,
        random: randomCard,
        create: createCard
    };
});
