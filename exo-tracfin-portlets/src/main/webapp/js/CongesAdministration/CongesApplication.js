$(document).ready(function() {
	$("#searchFilter").keyup(function () {
		var data = this.value.split(" ");
		//create a jquery object of the rows
		var jo = $("#congesTable").find('tr.userLine');
		$('table#test tr#3').remove();
		if (this.value == "") {
		    jo.show();
		    return;
		}
		//hide all the rows
		jo.hide();

		//Recusively filter the jquery object to get results.
		jo.filter(function (i, v) {
		    var $t = $(this);
		    var matched = true;
		    for (var d = 0; d < data.length; ++d) {
		        if (data[d].match(/^\s*$/)) {
		            continue;
		        }

		        var regex = new RegExp(data[d].toLowerCase());
		        if ($t.text().toLowerCase().replace(/(manual|auto)/g,"").match(regex) === null) {
		            matched = false;
		        }
		    }
		    return matched;
		})

		//show the rows that match.
		.show();
		})

		.focus(function () {
		this.value = "";
		$(this).css({
		    "color": "black"
		});
		$(this).unbind('focus');
		}).css({
		"color": "#C0C0C0"
		});
});