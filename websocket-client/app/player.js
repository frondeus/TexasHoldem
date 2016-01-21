define(["./random", "./string"], function(random, str) {
    var colors = ["red", "green", "blue", "yellow"];
    var players = {};

    var playerFunc = function(uuid) {
        this.uuid = uuid;
        this.color = random.array(colors);
        this.name = this.color.capitalizeFirstLetter();

        var index = colors.indexOf(this.color);
        if(index > -1) colors.splice(index, 1);
    };

    var getPlayer = function(playerUUID) {
        var player = players[playerUUID];
        if(!player) {
            player = new playerFunc();
            players[playerUUID] = player;
        }
        return player;
    };

    return {
        getPlayer: getPlayer
    };
});
