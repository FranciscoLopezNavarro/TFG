"use strict";
$(document).ready(function(){
	ConnectionManager.isConnected();
	setTimeout(function() {
        if(flag === "false"){
            console.log("Waiting for response...");
        }else{
            console.log("Connected");
        	if(connected === false){
        		MenuManager.LoadMenuNoConnection();
        	}else{
            		MenuManager.LoadMenuWithConnection();
        	}
        }   
    }, 500);

});
