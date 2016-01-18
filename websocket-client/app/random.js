define(function(){
    return {
        array: function(items){
            return items[Math.floor(Math.random() * items.length)];  
        }
    };
});
