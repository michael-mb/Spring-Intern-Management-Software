(function($) {
	"use strict";	
    $('.toggle-menu').click(function(){
        $('.responsive-menu').stop(true,true).slideToggle();
        return false;
    });
})(jQuery);