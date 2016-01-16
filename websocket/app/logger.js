define(["jquery"], function($) {
    var logger = {};
    logger.log = function(text, _class) {
        var date = new Date();
        var time = "";
        if(date.getHours() < 10) time += "0";
        time += date.getHours();
        time += ":";
        if(date.getMinutes() < 10) time += "0";
        time += date.getMinutes();


        var li = $("<li>").text(time + " " + text);
        if(_class) li.addClass(_class);
        $("#log").append(li);
        console.log(text);
    };

    logger.error = function(text) {
        this.log(text, "error");
    };
    

    return logger;
});
