define(["./random", "./string"], function(random, str) {
    var colors = ["red", "green", "blue", "yellow"];
    var players = {};
    var myUUID = null;

    var playerFunc = function(uuid, local) {
        this.uuid = uuid;
        if(local) {
            this.color = "white";
            this.name = "You";
        }
        else {
            this.color = random.array(colors);
            this.name = this.color.capitalizeFirstLetter();
        }

        var index = colors.indexOf(this.color);
        if(index > -1) colors.splice(index, 1);
    };

    var setUUID = function(_myUUID) {
        myUUID = _myUUID;
    };

    var getPlayer = function(playerUUID) {
        var player = players[playerUUID];
        if(myUUID == playerUUID && !player) {
            player = new playerFunc(playerUUID, true);
            players[playerUUID] = player;
        }
        if(!player) {
            player = new playerFunc(playerUUID);
            players[playerUUID] = player;
        }
        return player;
    };

    return {
        getPlayer: getPlayer,
        setUUID: setUUID
    };
});
