	$(function() {
		$("#createDateStart").datepicker({
			dateFormat : "yy-mm-dd 00:00:00",
			changeMonth : true,
			changeYear : true,
			showButtonPanel: true,
			showAnim: 'fold'
		}).datepicker("setDate", 'today');
		$("#createDateEnd").datepicker({
			dateFormat : "yy-mm-dd 23:59:59",
			changeMonth : true,
			changeYear : true,
			showButtonPanel: true,
			showAnim: 'fold'
		}).datepicker("setDate", 'today');

		$("#effDate").datepicker({
			dateFormat : "yy-mm-dd 00:00:00",
			changeMonth : true,
			changeYear : true,
			showButtonPanel: true,
			showAnim: 'fold'
		});
		$("#expDate").datepicker({
			dateFormat : "yy-mm-dd 23:59:59",
			changeMonth : true,
			changeYear : true,
			showButtonPanel: true,
			showAnim: 'fold'
		});


		$("#m_effDate").datepicker({
			dateFormat : "yy-mm-dd 00:00:00",
			changeMonth : true,
			changeYear : true
		});
		$("#m_expDate").datepicker({
			dateFormat : "yy-mm-dd 23:59:59",
			changeMonth : true,
			changeYear : true
		});

		$("#ui-datepicker-div").css("font-size", "0.7em");
	});