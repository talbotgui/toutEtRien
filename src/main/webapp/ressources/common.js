// Constantes
var REST_PREFIX = window.location.protocol + "//" + window.location.hostname + ((window.location.port !== "") ? ":" + window.location.port : "") + "/monApp";

/**
 * Pour serialiser un formulaire en objet JS.
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * En cas d'erreur AJAX.
 */
var ajaxFailFunctionToDisplayWarn = function(appelant) {
	alert("Une erreur est arrivée dans la methode '" + appelant + "'");
};

/** 
 * On ready.
 */
$(document).ready(function() {

});