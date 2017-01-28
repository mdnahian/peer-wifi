require('sugar')

exports.stringifyTraverse = function(subject, level, key){
	var result = '';
	for (var prop in subject) {
		var parent_appendix = key ? key : '' ;
		var parent_appendix = level > 1 && level < 2 ? '['+key+']' : parent_appendix ;
		var child_appendix = level > 0 ? '['+prop+']' : prop ;
		var appendix = parent_appendix + child_appendix;
		var value = subject[prop];
	    if (Object.isObject(value)) {
	        //output(appendix.red, value, level);
	        result += exports.stringifyTraverse(value, level + 1, appendix);
	    } else if (Array.isArray(value)) {
	    	for(var i = 0; i < value.length; i++){
	    		result += appendix+'='+exports.stringifyValue(value[i])+'&';
	    		//console.log((appendix+'='+exports.stringifyValue(value[i])+'&').toString().white);
	    	}
	    	
	    } else {
	        //output(appendix.yellow, value, level);
	        //console.log((appendix +'='+ value).toString().white);
	        result += appendix +'='+ exports.stringifyValue(value) + '&';
	    }
	}
	return result;
}


exports.stringify = function(query){
	//console.log('\n\n============================', query);
	var result = exports.stringifyTraverse(query, 0);
	result = result.substr(0, result.length-1);
	//console.log('RESULT', result.replace(/\&/gi, '\n&'));
	return result;
}
exports.value = function(value){
	if(value == 'true'){ 
		value = true; 	// boolean true
	} else if (value == 'false') {
		value = false; 	// boolean false
	} else if (isset(value) && !isNaN(value)) {
		value = +value; // number
	}
	return value;
}
exports.stringifyValue = function(value){
	if(value === true){ 
		value = 'true'; 	// boolean true
	} else if (value === false) {
		value = 'false'; 	// boolean false
	} else if (isset(value) && !isNaN(value)) {
		value = +value; // number
	}
	return value;
}

exports.traverse = function(subject, key, value){
	
	if(key.indexOf('[') != -1){	
		var split = key.split('[');
		//console.log('\n' + key.yellow)
		//console.log('SPLIT'.red,  split.length, split[1].split(']')[0], (split.length > 2),  isNaN(split[1].split(']')[0]));
		var INT_SUBJECT = subject;
		if(isNaN(split[1].split(']')[0])){
			for(var i = 0; i < split.length; i++){
				var section = split[i].indexOf(']') ? split[i].split(']')[0] : split[i] ;
				//console.log('  ->'.grey, '['+i+'/'+(split.length-1)+'] '+ section);
				
				if(i < split.length-1){
					if(!INT_SUBJECT[section]) INT_SUBJECT[section] = {};
				} else {
					if(INT_SUBJECT[section]) {
						INT_SUBJECT[section] = [INT_SUBJECT[section]];
						INT_SUBJECT[section].push(exports.value(value))
					} else {
						INT_SUBJECT[section] = exports.value(value);
					}
				}
				INT_SUBJECT = INT_SUBJECT[section];
				
				//console.log(' -> INT_SUBJECT: ', INT_SUBJECT);
			}
			subject = INT_SUBJECT;
		} else {
			var parent = split[0];
			var section = split[1].indexOf(']') ? split[1].split(']')[0] : split[1] ;
			if(typeof INT_SUBJECT[parent] != 'object') INT_SUBJECT[parent] = [];
			INT_SUBJECT[parent].push(exports.value(value));
			subject = INT_SUBJECT;
		}
	} else if (subject[key]) {
		if(typeof subject[key] != 'object') subject[key] = [subject[key]];
		subject[key].push(exports.value(value));
	} else {
		subject[key] = exports.value(value);
	}
	return subject;
}
exports.parse = function(body){
	var result = {};
	body = body.toString().replace(/\+/gi,' ')
	//console.log('###ORIGINAL', body);
	var split = body.split(/\&/gi);
	split.forEach(function(string){
		var split = string.split('=');
		var key = decodeURIComponent(split[0]);
		var value = decodeURIComponent(split[1]);
		exports.traverse(result, key, value, split.length)
			//result[key] = exports.value(value);
		
		
	});
	//console.log('PARSE RESULT', result);
	return result;
}

function output(appendix, value, level) {
    console.log('     ' + space(level*4) + '--> ' +level+':'+ appendix +'='+ value.toString().cyan );
}

function space(ed){
	var s = ''; for(var i = 1; i < ed; i++){ s += ' '.grey; } return s;
}

// Isset
isset = function(object){
	return (object != "undefined" && object != undefined && object != null && object != "" && typeof(object) != 'undefined') ? true : false ;
}