/*
 * Copyright 2006-2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// global vars
var $dialog = null;
var jq = jQuery.noConflict();

//BlockUi defaults
var loadingMessage =  '<h1><img src="/kr-dev/krad/images/loading.gif" alt="working..." />Loading...</h1>';
var savingMessage = '<h1><img src="/kr-dev/krad/images/loading.gif" alt="working..." />Saving...</h1>';

// validation init
var pageValidatorReady = false;

//sets up the validator with the necessary default settings and methods
//note the use of onClick and onFocusout for on the fly validation client side
function setupValidator(){
	jq('#kualiForm').dirty_form({changedClass: 'dirty'});

	//Make sure form doesn't have any unsaved data when user clicks on any other portal links, closes browser or presses fwd/back browser button
	jq(window).bind('beforeunload', function(evt){
		var validateDirty = jq("[name='validateDirty']").val();
		if (validateDirty == "true")
		{
			var dirty = jq(".field_attribute").find("input.dirty")
			//methodToCall check is needed to skip from normal way of unloading (cancel,save,close)
			var methodToCall = jq("[name='methodToCall']").val();
			if (dirty.length > 0 && methodToCall == null)
			{
				return "Form has unsaved data. Do you want to leave anyway?";
			}
		}
	});

	jq('#kualiForm').validate(
	{
		onsubmit: false,
		ignore: ".ignoreValid",
		onclick: function(element) {
			jq(element).valid();
			dependsOnCheck(element);
		},
		onfocusout: function(element) {
			jq(element).valid();
			dependsOnCheck(element);
		},
		wrapper: "li",
		highlight: function(element, errorClass, validClass) {
			jq(element).addClass(errorClass).removeClass(validClass);
			applyErrorColors(getAttributeId(element.id, element.type) + "_errors_div", 1, 0, 0, true);
			showFieldIcon(getAttributeId(element.id, element.type) + "_errors_div", 1);
		},
		unhighlight: function(element, errorClass, validClass) {
			jq(element).removeClass(errorClass).addClass(validClass);
			applyErrorColors(getAttributeId(element.id, element.type) + "_errors_div", 0, 0, 0, true);
			showFieldIcon(getAttributeId(element.id, element.type) + "_errors_div", 0);
		},
		errorPlacement: function(error, element) {
			var id = getAttributeId(element.attr('id'), element.attr('type'));
			//check to see if the option to use labels is on
			if (!jq("#" + id + "_errors_div").hasClass("noLabels")) {
				var label = getLabel(id);
				label = jq.trim(label);
				if (label) {
					if (label.charAt(label.length - 1) == ":") {
						label = label.slice(0, -1);
					}
					error.find("label").before(label + " - ");
				}
			}
			jq("#" + id + "_errors_div").show();
			jq("#" + id + "_errors_errorMessages").show();
			var errorList = jq("#" + id + "_errors_errorMessages ul");
			error.appendTo(errorList);
		}
	});

	jq(document).trigger('validationSetup');
	pageValidatorReady = true;
	jq.watermark.showAll();
}

jQuery.validator.addMethod("minExclusive", function(value, element, param){
	if (param.length == 1 || param[1]()) {
		return this.optional(element) || value > param[0];
	}
	else{
		return true;
	}
});
jQuery.validator.addMethod("maxInclusive", function(value, element, param){
	if (param.length == 1 || param[1]()) {
		return this.optional(element) || value <= param[0];
	}
	else{
		return true;
	}
});
jQuery.validator.addMethod("minLengthConditional", function(value, element, param){
	if (param.length == 1 || param[1]()) {
		return this.optional(element) || this.getLength(jq.trim(value), element) >= param[0];
	}
	else{
		return true;
	}
});
jQuery.validator.addMethod("maxLengthConditional", function(value, element, param){
	if (param.length == 1 || param[1]()) {
		return this.optional(element) || this.getLength(jq.trim(value), element) <= param[0];
	}
	else{
		return true;
	}
});

// data table initialize default sorting
jQuery.fn.dataTableExt.oSort['kuali_date-asc']  = function(a,b) {
	var ukDatea = a.split('/');
	var ukDateb = b.split('/');
	var x = (ukDatea[2] + ukDatea[0] + ukDatea[1]) * 1;
	var y = (ukDateb[2] + ukDateb[0] + ukDateb[1]) * 1;
	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
};

jQuery.fn.dataTableExt.oSort['kuali_date-desc'] = function(a,b) {
	var ukDatea = a.split('/');
	var ukDateb = b.split('/');
	var x = (ukDatea[2] + ukDatea[0] + ukDatea[1]) * 1;
	var y = (ukDateb[2] + ukDateb[0] + ukDateb[1]) * 1;
	return ((x < y) ? 1 : ((x > y) ?  -1 : 0));
};

jQuery.fn.dataTableExt.afnSortData['dom-text'] = function  ( oSettings, iColumn )
{
	var aData = [];
	jq( 'td:eq('+iColumn+') input', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
		aData.push( this.value );
	} );
	return aData;
}

/* Create an array with the values of all the select options in a column */
jQuery.fn.dataTableExt.afnSortData['dom-select'] = function  ( oSettings, iColumn )
{
	var aData = [];
	jq( 'td:eq('+iColumn+') select', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
		aData.push( jq(this).val() );
	} );
	return aData;
}

/* Create an array with the values of all the checkboxes in a column */
jQuery.fn.dataTableExt.afnSortData['dom-checkbox'] = function  ( oSettings, iColumn )
{
	var aData = [];
	jq( 'td:eq('+iColumn+') input', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
		aData.push( this.checked==true ? "1" : "0" );
	} );
	return aData;
}

// setup window javascript error handler
window.onerror = errorHandler;

function errorHandler(msg,url,lno)
{
  var context = getContext();
  context.unblockUI();
  showGrowl(msg + '<br/>' + url + '<br/>' + lno, 'Javascript Error', 'errorGrowl');
  return false;
}

// common event registering done here through JQuery ready event
jq(document).ready(function() {
	createLoading(false);
	setPageBreadcrumb();

	// buttons
	jq("input:submit").button();
	jq("input:button").button();
    jq("a.button").button();

    // common ajax setup
	jq.ajaxSetup({
		  beforeSend: function() {
		     createLoading(true);
		  },
		  complete: function(){
			 createLoading(false);
		  },
		  error: function(jqXHR, textStatus, errorThrown){
			 createLoading(false);
			 showGrowl('Status: ' + textStatus + '<br/>' + errorThrown, 'Server Response Error', 'errorGrowl');
		  }
	});

	runHiddenScripts("");
});



