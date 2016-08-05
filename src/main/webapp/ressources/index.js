var ajouteUtilisateur = function(e) {
	e.preventDefault();
	
	var data = $("#formulaireUtilisateur form").serialize();
	
	
	var req = $.ajax({ type: "POST", url: REST_PREFIX + "/utilisateur", data: data});
	req.success(function(dataString) { chargeUtilisateurs(); });
	req.fail(function(jqXHR, textStatus, errorThrown) {ajaxFailFunctionToDisplayWarn("ajouteUtilisateur");});
};


// Chargement des courriers
var donneesDejaChargees = false;
var chargeUtilisateurs = function() {
	if (donneesDejaChargees) {
		$("#utilisateurs").jqxGrid('updatebounddata', 'cells');
	} else {
		// Chargement des courriers
		var dataAdapter = new $.jqx.dataAdapter({
			datatype: "json",
			url: REST_PREFIX + "/utilisateur",
			datafields: [{ name: 'nom', type: 'string' }, { name: 'prenom', type: 'string' }, { name: 'id', type: 'string' }],
			id: 'id'
		});
		$("#utilisateurs").jqxGrid({
			source: dataAdapter,
			columns: [
				{ text: 'Identifiant', datafield: 'id', width: "20%", editable: false },
				{ text: 'Nom', datafield: 'nom', width: "40%", editable: false },
				{ text: 'Prenom', datafield: 'prenom', width: "40%", editable: false }
			],
			sortable: true, filterable: true, autoheight: true, altrows: true,
			width: 950
		});
		donneesDejaChargees = true;
	}
};

$(document).ready(function() {

	// Init
	chargeUtilisateurs();
	
	// Ajout des evenements
	$("#button_ajouter_utilisateur").on("click", ajouteUtilisateur);
});
