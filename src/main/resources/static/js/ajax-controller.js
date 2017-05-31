$(document).ready(
	function($) {

		$("#botao-relatorio").click(function(event) {
			
			var data = {}
			data["dataFinal"] 	= $("#data-final").val();
			data["dataInicio"] = $("#data-inicial").val();
			data["marca"] 		= $("#marca").val();
			
			if(data["dataFinal"] == null || data["dataFinal"] == "" || 
					data["dataInicio"] == null || data["dataInicio"] == "" ||
					data["marca"] == null || data["marca"] == ""){
					return;
			}

			$("#botao-relatorio").prop("disabled", true);
			$("#img-loading").prop("hidden", false);
			$("#msg-erro").prop("hidden", true);
			$("#msg-sucesso").prop("hidden", true);
			
			$.ajax({
		             type: "POST",
		             contentType: "application/json",
		             url: "buscar",
		             data: JSON.stringify(data),
		             dataType: 'text',
		             timeout: 600000,
		             success: function (data) {
		                 $("#botao-relatorio").prop("disabled", false);
		     			 $("#img-loading").prop("hidden", true);
		     			 $("#msg-erro").prop("hidden", true);
		     			 $("#msg-sucesso").prop("hidden", false);
		             },
		             error: function (data) {
		            	 $("#botao-relatorio").prop("disabled", false);
		     			 $("#img-loading").prop("hidden", true);
		     			 $("#msg-erro").prop("hidden", false);
		     			 $("#msg-sucesso").prop("hidden", true);
		             }
			});


		});

	});


function ocultaMensagem(){
	$("#msg-sucesso").prop("hidden", true);
}